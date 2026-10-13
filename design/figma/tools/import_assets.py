"""
Throwaway asset importer for the Figma export (not part of the app).

  set SVG2VECTOR_CP=<classpath: sdk-common, common, annotations, layoutlib-api, guava, failureaccess, kxml2, kotlin-stdlib>
  python -I import_assets.py

Reads  design/figma/source-evidence/{<screen>/context.md, assets/, assets/manifest.json}
Writes app/src/main/res/drawable/ic_*.xml          (SVG -> VectorDrawable via AGP Svg2Vector)
       app/src/main/res/drawable-nodpi/img_*.{png,jpg}  (raster, extension fixed by magic bytes)
       design/figma/tools/asset-map.json

Naming: <ic|img>_<snake(const name without 'img' prefix)>_<first 8 hex of the asset uuid>.
Assets with identical bytes (sha256) share ONE resource (the first one in screen order);
every original path is still listed in asset-map.json.
"""
import glob
import hashlib
import json
import os
import re
import shutil
import subprocess

ROOT = os.path.abspath(os.path.join(os.path.dirname(__file__), '..', '..', '..'))
EV = os.path.join(ROOT, 'design', 'figma', 'source-evidence')
RES = os.path.join(ROOT, 'app', 'src', 'main', 'res')
TOOLS = os.path.join(ROOT, 'design', 'figma', 'tools')
TMP = os.environ.get('SVG_TMP') or os.path.join(TOOLS, '_tmp')
SCREENS = ['splash', 'signin', 'signup', 'home', 'categories', 'productlist', 'details', 'cart', 'wishlist', 'account']
# Consts that belong to the iOS Status Bar (node 17:156): system chrome, not implemented.
STATUS_BAR_CONSTS = {'imgRectangle', 'imgCombinedShape', 'imgWiFi'}


def sha(p):
    return hashlib.sha256(open(p, 'rb').read()).hexdigest()


def snake(name):
    s = re.sub(r'^img', '', name)
    s = re.sub(r'(?<=[a-z])(?=[A-Z])', '_', s)
    s = re.sub(r'[^A-Za-z0-9_]', '_', s).lower()
    return re.sub(r'_+', '_', s).strip('_')


const_re = re.compile(r'^const (img\w+) = "\.\./assets/([0-9a-f-]+\.(?:svg|png))";', re.M)
use = {}  # path -> list of (screen, const, used)
for sc in SCREENS:
    txt = open(os.path.join(EV, sc, 'context.md'), encoding='utf-8').read()
    for m in const_re.finditer(txt):
        c, f = m.group(1), m.group(2)
        used = len(re.findall(r'\b' + c + r'\b', txt)) > 1
        use.setdefault('assets/' + f, []).append((sc, c, used))

manifest = json.load(open(os.path.join(EV, 'assets', 'manifest.json')))['assets']
node_of = {}
for a in manifest:
    node_of.setdefault(a['path'], []).append(a['nodeId'])
all_files = sorted('assets/' + os.path.basename(p) for p in glob.glob(os.path.join(EV, 'assets', '*'))
                   if not p.endswith('manifest.json'))

entries = {}
for p in all_files:
    refs = use.get(p, [])
    e = dict(path=p, sha256=sha(os.path.join(EV, p)), nodeIds=sorted(set(node_of.get(p, []))),
             screens=sorted({r[0] for r in refs}, key=SCREENS.index), consts=sorted({r[1] for r in refs}))
    if not refs:
        e['status'] = 'skipped'
        e['reason'] = 'not referenced by any context.md'
    elif all(r[1] in STATUS_BAR_CONSTS for r in refs):
        e['status'] = 'skipped'
        e['reason'] = 'iOS status bar artwork (Status Bar node 17:156, excluded; app uses the real system status bar)'
    elif not any(r[2] for r in refs):
        e['status'] = 'skipped'
        e['reason'] = 'declared but unused in context.md'
    else:
        e['status'] = 'pending'
    entries[p] = e


def order(p):
    e = entries[p]
    return (SCREENS.index(e['screens'][0]) if e['screens'] else 99, p)


canon = {}
for p in sorted([p for p in entries if entries[p]['status'] == 'pending'], key=order):
    canon.setdefault(entries[p]['sha256'], p)

svgs = [p for p in canon.values() if p.endswith('.svg')]
stage = os.path.join(TMP, 'svg')
outx = os.path.join(TMP, 'xml')
shutil.rmtree(TMP, ignore_errors=True)
os.makedirs(stage)
os.makedirs(outx)
for p in svgs:
    shutil.copy(os.path.join(EV, p), os.path.join(stage, os.path.basename(p)))
cp = os.environ['SVG2VECTOR_CP']
subprocess.check_call(['javac', '-cp', cp, '-d', TMP, os.path.join(TOOLS, 'Svg2VectorBatch.java')])
subprocess.check_call(['java', '-cp', cp + os.pathsep + TMP, 'Svg2VectorBatch', stage, outx])

for d in ('drawable', 'drawable-nodpi'):
    for old in glob.glob(os.path.join(RES, d, 'ic_*_????????.xml')) + glob.glob(os.path.join(RES, d, 'img_*_????????.*')):
        os.remove(old)
os.makedirs(os.path.join(RES, 'drawable'), exist_ok=True)
os.makedirs(os.path.join(RES, 'drawable-nodpi'), exist_ok=True)


def res_name(p):
    e = entries[p]
    uuid8 = os.path.basename(p)[:8]
    c = e['consts'][0]
    firstscreen = e['screens'][0]
    for sc, cc, used in use[p]:
        if sc == firstscreen and used:
            c = cc
            break
    prefix = 'ic' if p.endswith('.svg') else 'img'
    return f'{prefix}_{snake(c)}_{uuid8}'


for sh, p in canon.items():
    e = entries[p]
    n = res_name(p)
    src = os.path.join(EV, p)
    if p.endswith('.svg'):
        base = os.path.basename(p)[:-4]
        xml = open(os.path.join(outx, base + '.xml'), encoding='utf-8').read()
        log = open(os.path.join(outx, base + '.log'), encoding='utf-8').read().strip()
        svg_txt = open(src, encoding='utf-8').read()
        notes = []
        if log:
            notes.append('converter: ' + log.replace('\n', ' | '))
        if '<filter' in svg_txt:
            notes.append('SVG contains a <filter> (' + ','.join(sorted(set(re.findall(r'<(feGaussianBlur|feOffset)', svg_txt)))) +
                         '); VectorDrawable cannot express it, geometry imported WITHOUT the filter effect')
        if '<mask' in svg_txt:
            notes.append('contains <mask> (unsupported)')
        if 'Gradient' in svg_txt:
            notes.append('contains gradient')
        if '<clipPath' in svg_txt:
            notes.append('contains clipPath (converter output kept)')
        e['converterNotes'] = notes
        e['fidelity'] = 'geometry-only (filter dropped)' if '<filter' in svg_txt else ('check-notes' if notes else 'ok')
        open(os.path.join(RES, 'drawable', n + '.xml'), 'w', encoding='utf-8', newline='\n').write(xml)
        e['resource'] = 'R.drawable.' + n
        e['file'] = f'app/src/main/res/drawable/{n}.xml'
    else:
        head = open(src, 'rb').read(4)
        ext = 'png' if head == b'\x89PNG' else ('jpg' if head[:2] == b'\xff\xd8' else None)
        if ext is None:
            raise SystemExit('unknown raster format ' + p)
        e['rasterFormat'] = ext
        if ext == 'jpg':
            e['note'] = 'Figma export named it .png but the bytes are JPEG; imported as .jpg'
        shutil.copy(src, os.path.join(RES, 'drawable-nodpi', f'{n}.{ext}'))
        e['resource'] = 'R.drawable.' + n
        e['file'] = f'app/src/main/res/drawable-nodpi/{n}.{ext}'
    e['status'] = 'imported'

for p, e in entries.items():
    if e['status'] == 'pending':
        c = canon[e['sha256']]
        e['status'] = 'imported-duplicate'
        e['duplicateOf'] = c
        e['resource'] = entries[c]['resource']
        e['file'] = entries[c]['file']

out = dict(
    generatedBy='design/figma/tools/import_assets.py (SVG via com.android.ide.common.vectordrawable.Svg2Vector, sdk-common 32.0.1)',
    summary={k: sum(1 for e in entries.values() if e['status'] == k) for k in ('imported', 'imported-duplicate', 'skipped')},
    assets=sorted(entries.values(), key=lambda e: e['path']))
json.dump(out, open(os.path.join(TOOLS, 'asset-map.json'), 'w', encoding='utf-8'), indent=2, ensure_ascii=False)
print(out['summary'], 'total', len(entries))
shutil.rmtree(TMP, ignore_errors=True)
