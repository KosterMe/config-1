package emulator;

import java.util.List;

/**
 * Тесты выполнения стартового скрипта.
 */
public final class ScriptTest {

    private ScriptTest() {
    }

    /**
     * Запускает все тесты класса.
     */
    public static void run() {
        echoesCommands();
        stopsAtFirstError();
        exitInScript();
    }

    private static void echoesCommands() {
        TestShell shell = new TestShell();
        boolean ok = shell.runScript(List.of("# comment", "", "ls", "cd"));
        Check.isTrue("script: success", ok);
        Check.contains("script: echo ls", shell.out(), "tester@testhost:~$ ls\n");
        Check.contains("script: echo cd", shell.out(), "tester@testhost:~$ cd\n");
        Check.isTrue("script: comment skipped", !shell.out().contains("# comment"));
    }

    private static void stopsAtFirstError() {
        TestShell shell = new TestShell();
        boolean ok = shell.runScript(List.of("# comment", "ls", "unknown", "cd never"));
        Check.isTrue("script error: result", !ok);
        Check.contains("script error: echo", shell.out(), "tester@testhost:~$ unknown\n");
        Check.contains("script error: output", shell.err(), "unknown: command not found");
        Check.contains("script error: line number", shell.err(), "script error: line 3");
        Check.isTrue("script error: stopped", !shell.out().contains("cd never"));
    }

    private static void exitInScript() {
        TestShell shell = new TestShell();
        boolean ok = shell.runScript(List.of("exit 4", "ls"));
        Check.isTrue("script exit: no error", ok);
        Check.equal("script exit: code", 4, shell.session().exitCode());
        Check.isTrue("script exit: stopped", !shell.out().contains("$ ls"));
    }
}
