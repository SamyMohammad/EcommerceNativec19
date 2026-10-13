# مراجعة Figma قبل التنفيذ

التنفيذ متوقف قبل تعديل التطبيق، لحين الموافقات المطلوبة واستكمال الأدلة. التقرير لا يعتبر الشاشات منفذة أو المطابقة البصرية ناجحة.

## المشروع والبيئة

- module واحد: `:app`، Kotlin وJetpack Compose وMaterial 3.
- الحزمة: `com.example.ecommercenativec19`.
- الواجهة الحالية: Greeting في MainActivity؛ ثيم افتراضي بألوان بنفسجية وDynamic Color مفعّل وخط النظام.
- لا توجد navigation أو DI أو ViewModels أو backend contracts. أي دخول ناجح في prototype Figma ليس إثباتًا لوجود authentication backend.
- لا توجد ملفات AGENTS.md أو CLAUDE.md أو README محلية؛ تم اتباع التعليمات المرسلة في المحادثة.
- لا يوجد `graphify-out/graph.json`؛ لم يتم تشغيل query أو update.
- المسار الحالي ليس Git repository؛ لم يتم إنشاء Git repository أو عمل commit/push.
- Python venv موجود: `<local Figma workflow venv>`، Python 3.14.2؛ pip check وimports نجحوا.
- locate_core أعاد مسار figma-core المثبت؛ figma-compose وfigma-core متاحان.
- Gradle 9.6.0 وAndroid SDK متاحان. نجح `:app:assembleDebug :app:testDebugUnitTest` دون تعديل toolchain. احتاج Gradle صلاحية الكتابة في كاش `D:/dev/gradle`؛ تم تشغيله بالصلاحية اللازمة.
- اختبار الوحدة الموجود: اختبار واحد، صفر failures/errors؛ هذا اختبار المشروع الابتدائي فقط.
- دليل الاختبار: `../../app/build/test-results/testDebugUnitTest/TEST-com.example.ecommercenativec19.ExampleUnitTest.xml`.
- APK الحالي: `../../app/build/outputs/apk/debug/app-debug.apk`، وهو التطبيق الابتدائي.

## المصدر والميزانية

أدوات Figma متاحة وwhoami نجح؛ حساب Figma متصل بخطة Starter ومقعد View. نجح الوصول لكل الشاشات المطلوبة.

الحد المنشور لمقعد View هو 20 قراءة شهريًا: https://developers.figma.com/docs/figma-mcp-server/rate-limits-access/

تم تسجيل 14 قراءة في `source-evidence/budget.sqlite3`، تشمل استدعاءين رجعوا خطأ بسبب حجم النتيجة. استخدام الحساب في جلسات سابقة غير معلوم؛ السجل لا يدّعي معرفة الرصيد المتبقي عالميًا أو موعد تجدد نافذة الحساب. لم تحدث rate-limit errors.

تم حفظ السياق والصورة لكل شاشة، و152 أصلًا غير فارغ مع SHA-256. كل الروابط المؤقتة في السياق تحولت لمسارات محلية. رابط الصورة التوضيحي في شرح الأداة لم يكن أصل تصميم وتم استبعاده من تقرير الفشل.

كل الشاشات 430×932؛ layoutMode للمستوى الأعلى هو NONE. لم يتم افتراض Auto Layout أو dark mode. لا توجد local variable collections أو variables في المصدر الذي تم فحصه. `light` اسم وضع مقترح للـadapter، وليس اسم mode أصلي مكتشف في Figma.

`sourceRevision` لم توفره الأدوات. لذلك freshness غير متحقق، والشاشات في manifest تظل pending. لا يجوز استخدام وقت الاستخراج أو hash محلي كبديل مخترع لنسخة Figma.

## الـtokens والمقترحات للموافقة

القيم الكاملة والقنوات العشرية الأصلية محفوظة في `source-evidence/source-inventory.json` و`source-evidence/tokens.json`. HEX في الجدول تمثيل عرض 8-bit؛ لا يحل محل القنوات الأصلية. لا يوجد دمج أو تقريب تلقائي للقيم.

| المصدر | القيمة الأصلية/تمثيل العرض | المقترح في Compose | التعارض الحالي |
|---|---|---|---|
| main color، node 1:2 | #004182؛ alpha 1 | primary + brand background | primary حاليًا #6650A4 وDynamic Color قد يغيره |
| text color، node 38:139 | #06004F؛ alpha 1 | onSurface/onBackground | ألوان Material الافتراضية |
| white، node 14:48 | #FFFFFF؛ alpha 1 | surface/background/onPrimary | خلفيات Material الافتراضية |
| borders، node 60:32 | #004182؛ alpha 0.30000001192092896 | shared outline token | لا يوجد token مخصص |
| description، node 18:189 | #06004F؛ alpha 0.6000000238418579 | shared muted text token | لا يوجد token مخصص |
| category rail، node 601:799 | RGB (0.859519362449646, 0.8943430185317993, 0.9291666746139526)، alpha 0.5؛ عرض #DBE4ED | shared category surface token | لا يوجد token مخصص |
| header 1، node 14:48 | Poppins Medium، 500، 18px، lineHeight 18px | titleMedium override | خط النظام وتدرج Material الافتراضي |
| general text، node I34:127;32:19 | Poppins Regular، 400، 14px، lineHeight 18px | bodyMedium override | القيم الافتراضية مختلفة |
| small text، node 57:162 | Poppins Regular، 400، 12px، lineHeight 18px | bodySmall override | القيم الافتراضية مختلفة |
| old price، node 529:295 | Poppins Regular، 400، 11px، lineHeight 18px | custom oldPrice style | لا يقابلها slot دقيق |
| button، node I34:127;31:18 | Poppins Medium، 500، 20px، lineHeight 18px | custom button style | Material label الافتراضي مختلف |
| login، node 14:53 | Poppins SemiBold، 600، 20px، lineHeight 18px | custom authButton style | يختلف عن button 500؛ لن يتم دمجهما |
| welcome، node 14:74 | Poppins SemiBold، 600، 24px، lineHeight 18px | custom authHeading style | lineHeight أقل من الحجم؛ يحتاج تحقق clipping |
| typography spacing | -0.16500000655651093px | حفظ القيمة في TextStyle | bodyLarge الحالي +0.5sp |
| radii | 0، 5، 10، 15، 20، 25، 30px؛ تفاصيل status bar 1.2000000476837158 و1.600000023841858px | shape tokens لكل استخدام | لا يوجد تطابق مضمون مع Material defaults |

الحفاظ على قيم typography الأصلية يتطلب تحويل px التصميم إلى sp في المقاس المرجعي ثم اختبار font scaling. التحويل لا يصرح بتغيير lineHeight أو letterSpacing.

المقترح: استخدام palette ثابتة لهذه الشاشات مع تعطيل Dynamic Color، واستخدام المصدر الفاتح فقط دون اختراع palette داكنة. تبقى ألوان swatches وstar/icon داخل أصولها أو tokens مخصصة، وليست ألوان Material دلالية عامة.

تم تجهيز Poppins بأوزان 300/400/500/600 من Google Fonts مع OFL.txt وبصمات الملفات: https://github.com/google/fonts/tree/main/ofl/poppins

هذه الملفات مرشحة للاستخدام، ولم يثبت تطابق نسخة binary مع الخط الذي استُخدم في رندر Figma. حقل license في inventory لم يُعلّم approved تلقائيًا. SF Pro Text Semibold 600/15px مستخدم في شريط حالة iOS، ولا يوجد ملف محلي له. القرار المقترح: شريط حالة Android الأصلي مع تسجيل انحراف الـ44px العليا، بعد موافقة منفصلة، بدل افتراض خط بديل.

## المكونات والتفاعلات

تم اكتشاف 15 dependency node؛ تفاصيل variants المستقلة لم تُستخرج بالكامل. القائمة في source-inventory.json. المكونات المشتركة المقترحة بعد الموافقة: logo، حقول auth، أزرار auth/action، search header، product card، favorite control، quantity selector، product carousel، category tile، bottom navigation، cart/wishlist item، account field.

المصدر يتضمن prototype reactions؛ محفوظة في `source-evidence/interactions.json`. توجد وجهات خارج الشاشات العشرة، مسجلة في excludedNodes بالـmanifest. لن يتم تنفيذ هذه الشاشات تلقائيًا أو اعتبار كل destination عقد production navigation.

بيانات المنتجات والأسعار والاسم والبريد في المرجع بيانات تصميم/preview، وليست بيانات backend فعلية. لا توجد عقود مصادقة أو استرجاع كلمة المرور أو persistence أو checkout في المشروع.

## نتائج الـworkflow وما يمنع الاستكمال

- token audit: passed، دون issues/proposals؛ هذا تحقق بنية وقيم وليس موافقة بشرية أو مطابقة بصرية.
- validate: blocked، bundleStatus partial، freshness unverified.
- adapter-export: blocked، `BUNDLE_INVALID: Adapter export requires a complete bundle`؛ لم يصدر receipt.
- complete-node يتطلب sourceRevision غير null ومطابقًا للـmanifest؛ توجد علة معروفة قبل تشغيله، ولا توجد metadata مكتملة تسمح به الآن.
- theme dry run، generation، compile للشاشات الجديدة، compose-capture، visual-verify، compose-review، human visual approval: not_run.
- الصور المرجعية العشرة محفوظة مع referenceHash في reference-inventory.json. metaHash غير متاح؛ لذلك لم يتم طلب أو تسجيل reference approval شكلي.
- comparison-policy.json مجرد مسودة strict صفر tolerance، ليست سياسة مقارنة معتمدة أو وعدًا بإمكان مطابقة raster تام؛ ستعرض سياسة المقارنة قبل اعتمادها.

مصدر منع التصدير: [adapter-export SKILL.md](<local plugin cache>/claude-figma-workflows/figma-core/0.1.1/skills/adapter-export/SKILL.md): “Require a complete bundle with current human token approval; never create approval automatically.”

لاستكمال العمل: موافقة صريحة على tokens/وضع light/المقترحات أعلاه؛ قرار شريط الحالة؛ وتوفير دليل نسخة Figma مطابق للتصميم المطلوب (version ID أو export bundle موثق بsourceRevision). بعد حل ربط النسخة، تُستكمل أدلة variants والـmetadata، ثم تُطلب الموافقة المنفصلة على referenceHash وmetaHash قبل التصحيح البصري.

لم يتم تعديل ملفات التطبيق أو toolchain أو إدخال business logic أو backend وهمي. التغييرات الحالية أدلة تصميم وتقارير فقط تحت design/figma.
