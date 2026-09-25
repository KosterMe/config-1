package emulator;

/**
 * Тесты команд ls, cd, rev и clear.
 */
public final class CommandsTest {

    private CommandsTest() {
    }

    /**
     * Запускает все тесты класса.
     */
    public static void run() {
        lsCurrentDirectory();
        lsPaths();
        lsErrors();
        cdMoves();
        cdErrors();
        revReversesLines();
        revErrors();
        clearScreen();
    }

    private static void lsCurrentDirectory() {
        TestShell shell = new TestShell(TestVfs.sample());
        Check.equal("ls: status", 0, shell.run("ls"));
        Check.equal("ls: root", "etc  home\n", shell.out());
        shell.run("ls home/user/docs");
        Check.equal("ls: empty directory", "", shell.out());
    }

    private static void lsPaths() {
        TestShell shell = new TestShell(TestVfs.sample());
        shell.run("ls /home/user");
        Check.equal("ls: absolute", "docs  empty.txt  notes.txt\n", shell.out());
        shell.run("ls /etc/hostname");
        Check.equal("ls: file", "/etc/hostname\n", shell.out());
        shell.run("ls /etc /home");
        Check.equal("ls: several", "/etc:\nhostname\n\n/home:\nuser\n", shell.out());
        shell.run("ls /etc/hostname /home");
        Check.equal("ls: file and directory", "/etc/hostname\n\n/home:\nuser\n", shell.out());
    }

    private static void lsErrors() {
        TestShell shell = new TestShell(TestVfs.sample());
        Check.equal("ls missing: status", 1, shell.run("ls /nope /etc"));
        Check.contains("ls missing: message", shell.err(), "cannot access '/nope'");
        Check.contains("ls missing: others listed", shell.out(), "hostname");
        Check.equal("ls option: status", 1, shell.run("ls -l"));
        Check.contains("ls option: message", shell.err(), "invalid option '-l'");
    }

    private static void cdMoves() {
        TestShell shell = new TestShell(TestVfs.sample());
        shell.run("cd home/user/docs");
        Check.equal("cd: relative", "/home/user/docs", shell.session().cwd().path());
        shell.run("cd ../..");
        Check.equal("cd: dot-dot", "/home", shell.session().cwd().path());
        shell.run("cd /etc");
        Check.equal("cd: absolute", "/etc", shell.session().cwd().path());
        shell.run("cd ~/home");
        Check.equal("cd: tilde", "/home", shell.session().cwd().path());
        shell.run("cd");
        Check.equal("cd: no args", "/", shell.session().cwd().path());
        shell.run("cd ..");
        Check.equal("cd: above root", "/", shell.session().cwd().path());
    }

    private static void cdErrors() {
        TestShell shell = new TestShell(TestVfs.sample());
        Check.equal("cd missing: status", 1, shell.run("cd /nope"));
        Check.contains("cd missing: message", shell.err(), "/nope: No such file or directory");
        shell.run("cd /etc/hostname");
        Check.contains("cd file: message", shell.err(), "Not a directory");
        shell.run("cd /etc /home");
        Check.contains("cd many: message", shell.err(), "too many arguments");
        Check.equal("cd errors: stays", "/", shell.session().cwd().path());
    }

    private static void revReversesLines() {
        TestShell shell = new TestShell(TestVfs.sample());
        Check.equal("rev: status", 0, shell.run("rev /home/user/notes.txt"));
        Check.equal("rev: output", "cba\n321\n", shell.out());
        shell.run("rev /etc/hostname /home/user/empty.txt");
        Check.equal("rev: several files", "rotalume\n", shell.out());
    }

    private static void revErrors() {
        TestShell shell = new TestShell(TestVfs.sample());
        Check.equal("rev no args: status", 1, shell.run("rev"));
        Check.contains("rev no args: message", shell.err(), "missing file operand");
        Check.equal("rev missing: status", 1, shell.run("rev nope.txt"));
        Check.contains("rev missing: message", shell.err(), "cannot open nope.txt");
        shell.run("rev /home");
        Check.contains("rev directory: message", shell.err(), "/home: Is a directory");
    }

    private static void clearScreen() {
        TestShell shell = new TestShell(TestVfs.sample());
        Check.equal("clear: status", 0, shell.run("clear"));
        Check.contains("clear: ansi", shell.out(), "\033[2J");
        Check.equal("clear args: status", 1, shell.run("clear now"));
    }
}
