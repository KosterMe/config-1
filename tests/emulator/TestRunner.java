package emulator;

import emulator.config.ArgsParserTest;
import emulator.vfs.VfsLoaderTest;

/**
 * Запуск всех тестов проекта.
 */
public final class TestRunner {

    private TestRunner() {
    }

    /**
     * Выполняет тесты и печатает итог. Запускать из корня репозитория.
     *
     * @param args не используются
     */
    public static void main(String[] args) {
        CommandParserTest.run();
        ShellTest.run();
        ScriptTest.run();
        ArgsParserTest.run();
        VfsLoaderTest.run();
        System.out.println("Tests passed: " + Check.passed() + ", failed: " + Check.failed());
        if (Check.failed() > 0) {
            System.exit(1);
        }
    }
}
