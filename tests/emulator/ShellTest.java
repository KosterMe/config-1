package emulator;

/**
 * Тесты REPL: приглашение, exit и обработка ошибок.
 */
public final class ShellTest {

    private ShellTest() {
    }

    /**
     * Запускает все тесты класса.
     */
    public static void run() {
        promptUsesUserAndHost();
        unknownCommandIsError();
        promptShowsCurrentDirectory();
        exitWithCode();
        exitWithoutCodeUsesLastStatus();
        exitWithBadArgument();
        vfsInfo();
        vfsInfoTree();
    }

    private static void promptUsesUserAndHost() {
        TestShell shell = new TestShell();
        Check.equal("prompt", "tester@testhost:~$ ", shell.session().prompt());
    }

    private static void unknownCommandIsError() {
        TestShell shell = new TestShell();
        Check.equal("unknown: status", 1, shell.run("foo bar"));
        Check.contains("unknown: message", shell.err(), "foo: command not found");
    }

    private static void promptShowsCurrentDirectory() {
        TestShell shell = new TestShell(TestVfs.sample());
        shell.run("cd /home/user");
        Check.equal("prompt: subdirectory", "tester@testhost:~/home/user$ ",
                shell.session().prompt());
    }

    private static void exitWithCode() {
        TestShell shell = new TestShell();
        shell.run("exit 3");
        Check.isTrue("exit: requested", shell.session().isExitRequested());
        Check.equal("exit: code", 3, shell.session().exitCode());
    }

    private static void exitWithoutCodeUsesLastStatus() {
        TestShell shell = new TestShell();
        shell.run("foo");
        shell.run("exit");
        Check.equal("exit: last status", 1, shell.session().exitCode());
    }

    private static void exitWithBadArgument() {
        TestShell shell = new TestShell();
        Check.equal("exit abc: status", 1, shell.run("exit abc"));
        Check.isTrue("exit abc: not exited", !shell.session().isExitRequested());
        Check.contains("exit abc: message", shell.err(), "numeric argument required");
        Check.equal("exit many args", 1, shell.run("exit 1 2"));
    }

    private static void vfsInfo() {
        TestShell shell = new TestShell();
        Check.equal("vfs-info: status", 0, shell.run("vfs-info"));
        Check.contains("vfs-info: source", shell.out(), "source: (empty)");
        Check.contains("vfs-info: counts", shell.out(), "0 directories, 0 files");
        Check.equal("vfs-info: extra args", 1, shell.run("vfs-info x"));
    }

    private static void vfsInfoTree() {
        TestShell shell = new TestShell(TestVfs.sample());
        shell.run("vfs-info");
        String expected = String.join("\n",
                "source: test",
                "/",
                "|-- etc/",
                "|   `-- hostname (8 B)",
                "`-- home/",
                "    `-- user/",
                "        |-- docs/",
                "        |-- empty.txt (0 B)",
                "        `-- notes.txt (8 B)",
                "",
                "4 directories, 3 files",
                "");
        Check.equal("vfs-info: tree", expected, shell.out());
    }
}
