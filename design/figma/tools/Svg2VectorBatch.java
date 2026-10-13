import com.android.ide.common.vectordrawable.Svg2Vector;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Throwaway batch driver around AGP's Svg2Vector (com.android.ide.common.vectordrawable).
 * Usage: java Svg2VectorBatch <svgDir> <outDir>
 * Writes <outDir>/<name>.xml and <outDir>/<name>.log (the converter's error/warning text, may be empty).
 * Not part of the app.
 */
public class Svg2VectorBatch {
    public static void main(String[] args) throws Exception {
        File in = new File(args[0]);
        File out = new File(args[1]);
        out.mkdirs();
        File[] files = in.listFiles((d, n) -> n.endsWith(".svg"));
        java.util.Arrays.sort(files);
        for (File f : files) {
            String base = f.getName().substring(0, f.getName().length() - 4);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            String err = Svg2Vector.parseSvgToXml(f.toPath(), bos);
            Files.write(Paths.get(out.getPath(), base + ".xml"), bos.toByteArray());
            Files.write(Paths.get(out.getPath(), base + ".log"), (err == null ? "" : err).getBytes(StandardCharsets.UTF_8));
        }
    }
}
