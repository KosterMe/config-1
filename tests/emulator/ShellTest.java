package emulator;

/**
 * Тесты REPL: приглашение, команды-заглушки, exit и обработка ошибок.
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
        stubsPrintNameAndArguments();
        cdStubChecksArguments();
        exitWithCode();
        exitWithoutCodeUsesLastStatus();
        exitWithBadArgument();
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

    private static void stubsPrintNameAndArguments() {
        TestShell shell = new TestShell();
        Check.equal("ls stub: status", 0, shell.run("ls -l /home"));
        Check.equal("ls stub: output", "ls: stub, arguments: [-l, /home]\n", shell.out());
        shell.run("ls");
        Check.equal("ls stub: no arguments", "ls: stub, arguments: []\n", shell.out());
        Check.equal("cd stub: status", 0, shell.run("cd docs"));
        Check.equal("cd stub: output", "cd: stub, arguments: [docs]\n", shell.out());
    }

    private static void cdStubChecksArguments() {
        TestShell shell = new TestShell();
        Check.equal("cd stub many args: status", 1, shell.run("cd a b"));
        Check.contains("cd stub many args: message", shell.err(), "cd: too many arguments");
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
}
