package emulator.vfs;

import emulator.Check;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * Тесты загрузки VFS из CSV.
 */
public final class VfsLoaderTest {

    private static final String HEADER = "path,type,content";
    private static final int MIN_EXAMPLE_DIRECTORIES = 3;

    private VfsLoaderTest() {
    }

    /**
     * Запускает все тесты класса.
     */
    public static void run() {
        loadsNestedStructure();
        decodesBase64();
        emptyVfs();
        badHeader();
        badType();
        badBase64();
        undeclaredParent();
        duplicatePath();
        missingFile();
        loadsExampleFile();
    }

    private static void loadsNestedStructure() {
        Vfs vfs = parse(HEADER, "/a,dir,", "/a/b,dir,", "/a/b/c.txt,file,aGk=");
        VfsDirectory a = (VfsDirectory) vfs.root().get("a");
        VfsDirectory b = (VfsDirectory) a.get("b");
        Check.isTrue("vfs: nested file exists", b.get("c.txt") != null);
        Check.equal("vfs: directories", 2, vfs.root().countDirectories());
        Check.equal("vfs: files", 1, vfs.root().countFiles());
    }

    private static void decodesBase64() {
        Vfs vfs = parse(HEADER, "/hi.txt,file,aGkK");
        VfsFile file = (VfsFile) vfs.root().get("hi.txt");
        Check.equal("vfs: base64 content", "hi\n",
                new String(file.data(), StandardCharsets.UTF_8));
    }

    private static void emptyVfs() {
        Vfs vfs = parse(HEADER);
        Check.equal("vfs: empty", 0, vfs.root().children().size());
    }

    private static void badHeader() {
        Check.contains("vfs: bad header", error("a,b,c", "/x,dir,"), "first line");
        Check.contains("vfs: no lines", error(), "first line");
    }

    private static void badType() {
        Check.contains("vfs: bad type", error(HEADER, "/x,folder,"), "unknown type");
        Check.contains("vfs: bad fields", error(HEADER, "/x,dir"), "expected 3 fields");
    }

    private static void badBase64() {
        Check.contains("vfs: bad base64", error(HEADER, "/x,file,@@@"), "invalid base64");
    }

    private static void undeclaredParent() {
        String message = error(HEADER, "/a/b.txt,file,");
        Check.contains("vfs: undeclared parent", message, "line 2");
        Check.contains("vfs: undeclared parent text", message, "is not declared");
    }

    private static void duplicatePath() {
        Check.contains("vfs: duplicate", error(HEADER, "/a,dir,", "/a,dir,"), "duplicate");
    }

    private static void missingFile() {
        try {
            VfsLoader.load("no/such/file.csv");
            Check.isTrue("vfs: missing file must fail", false);
        } catch (VfsException e) {
            Check.equal("vfs: missing file", "file not found", e.getMessage());
        }
    }

    private static void loadsExampleFile() {
        try {
            Vfs vfs = VfsLoader.load("examples/vfs/deep.csv");
            Check.isTrue("vfs: example has 3+ levels", vfs.root().countDirectories()
                    >= MIN_EXAMPLE_DIRECTORIES);
        } catch (VfsException e) {
            Check.isTrue("vfs: example loads (" + e.getMessage() + ")", false);
        }
    }

    private static Vfs parse(String... lines) {
        try {
            return VfsLoader.parse(List.of(lines), "test");
        } catch (VfsException e) {
            throw new AssertionError("unexpected error: " + e.getMessage());
        }
    }

    private static String error(String... lines) {
        try {
            VfsLoader.parse(List.of(lines), "test");
            return "no error";
        } catch (VfsException e) {
            return e.getMessage();
        }
    }
}
