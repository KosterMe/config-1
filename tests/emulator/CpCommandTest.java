package emulator;

import emulator.vfs.VfsDirectory;
import emulator.vfs.VfsException;
import emulator.vfs.VfsLoader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

/**
 * Тесты команды cp.
 */
public final class CpCommandTest {

    private CpCommandTest() {
    }

    /**
     * Запускает все тесты класса.
     */
    public static void run() {
        copiesFileToNewName();
        copiesFileIntoDirectory();
        overwritesFile();
        copiesDirectoryRecursively();
        copyIsIndependent();
        mergesDirectories();
        operandErrors();
        sourceErrors();
        destinationErrors();
        csvFileIsNotChanged();
    }

    private static void copiesFileToNewName() {
        TestShell shell = new TestShell(TestVfs.sample());
        Check.equal("cp file: status", 0, shell.run("cp /etc/hostname /home/user/host.txt"));
        shell.run("rev /home/user/host.txt");
        Check.equal("cp file: content", "rotalume\n", shell.out());
        shell.run("ls /etc");
        Check.equal("cp file: source kept", "hostname\n", shell.out());
    }

    private static void copiesFileIntoDirectory() {
        TestShell shell = new TestShell(TestVfs.sample());
        shell.run("cd /home/user");
        Check.equal("cp into dir: status", 0, shell.run("cp notes.txt docs"));
        shell.run("ls docs");
        Check.equal("cp into dir: listed", "notes.txt\n", shell.out());
    }

    private static void overwritesFile() {
        TestShell shell = new TestShell(TestVfs.sample());
        Check.equal("cp overwrite: status", 0,
                shell.run("cp /etc/hostname /home/user/notes.txt"));
        shell.run("rev /home/user/notes.txt");
        Check.equal("cp overwrite: content", "rotalume\n", shell.out());
    }

    private static void copiesDirectoryRecursively() {
        TestShell shell = new TestShell(TestVfs.sample());
        Check.equal("cp -r: status", 0, shell.run("cp -r /home/user /etc/backup"));
        shell.run("ls /etc/backup");
        Check.equal("cp -r: content", "docs  empty.txt  notes.txt\n", shell.out());
        shell.run("cp -r /home /etc");
        shell.run("ls /etc/home/user");
        Check.equal("cp -r into dir", "docs  empty.txt  notes.txt\n", shell.out());
        Check.equal("cp -rR: status", 0, shell.run("cp -rR /home/user/docs /etc/docs"));
        Check.equal("cp --recursive: status", 0, shell.run("cp --recursive /etc /backup"));
    }

    private static void copyIsIndependent() {
        TestShell shell = new TestShell(TestVfs.sample());
        shell.run("cp -r /home/user /backup");
        shell.run("cp /etc/hostname /home/user/notes.txt");
        shell.run("rev /backup/notes.txt");
        Check.equal("cp -r: copy is independent", "cba\n321\n", shell.out());
        VfsDirectory backup = (VfsDirectory) shell.session().vfs().root().get("backup");
        Check.equal("cp -r: parent is set", "/backup/docs", backup.get("docs").path());
    }

    private static void mergesDirectories() {
        TestShell shell = new TestShell(TestVfs.sample());
        shell.run("cp /etc/hostname /home/user/docs");
        shell.run("cp -r /home/user /backup");
        shell.run("cp /home/user/notes.txt /home/user/docs");
        shell.run("cp /etc/hostname /backup/docs/extra.txt");
        Check.equal("cp -r merge: status", 0, shell.run("cp -r /home/user/docs /backup"));
        shell.run("ls /backup/docs");
        Check.equal("cp -r merge", "extra.txt  hostname  notes.txt\n", shell.out());
    }

    private static void operandErrors() {
        TestShell shell = new TestShell(TestVfs.sample());
        Check.equal("cp no args: status", 1, shell.run("cp"));
        Check.contains("cp no args", shell.err(), "missing file operand");
        shell.run("cp /etc/hostname");
        Check.contains("cp no dest", shell.err(), "missing destination file operand after");
        shell.run("cp a b c");
        Check.contains("cp many", shell.err(), "too many arguments");
        shell.run("cp -x a b");
        Check.contains("cp option", shell.err(), "invalid option '-x'");
    }

    private static void sourceErrors() {
        TestShell shell = new TestShell(TestVfs.sample());
        Check.equal("cp missing: status", 1, shell.run("cp /nope /tmp"));
        Check.contains("cp missing", shell.err(), "cannot stat '/nope'");
        shell.run("cp /home /tmp");
        Check.contains("cp dir without -r", shell.err(), "-r not specified");
        shell.run("cp -r /home /home/user/docs");
        Check.contains("cp into itself", shell.err(), "into itself");
        shell.run("cp /etc/hostname /etc");
        Check.contains("cp same file", shell.err(), "are the same file");
    }

    private static void destinationErrors() {
        TestShell shell = new TestShell(TestVfs.sample());
        Check.equal("cp no parent: status", 1, shell.run("cp /etc/hostname /nope/x.txt"));
        Check.contains("cp no parent", shell.err(), "cannot create '/nope/x.txt'");
        shell.run("cp -r /home/user/docs /etc/hostname");
        Check.contains("cp dir over file", shell.err(), "types differ");
        shell.run("cp -r /home/user/docs /hostname");
        Check.equal("cp file over dir: status", 1, shell.run("cp /etc/hostname /"));
        Check.contains("cp file over dir", shell.err(), "types differ");
    }

    private static void csvFileIsNotChanged() {
        Path csv = Path.of("examples/vfs/deep.csv");
        try {
            byte[] before = Files.readAllBytes(csv);
            TestShell shell = new TestShell(VfsLoader.load(csv.toString()));
            shell.run("cp -r /home /tmp");
            shell.run("cp /etc/hostname /bin");
            Check.isTrue("cp: csv not changed", Arrays.equals(before, Files.readAllBytes(csv)));
        } catch (IOException | VfsException e) {
            Check.isTrue("cp: csv test failed (" + e.getMessage() + ")", false);
        }
    }
}
