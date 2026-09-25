package emulator.config;

import emulator.Check;

/**
 * Тесты разбора параметров командной строки.
 */
public final class ArgsParserTest {

    private ArgsParserTest() {
    }

    /**
     * Запускает все тесты класса.
     */
    public static void run() {
        noArguments();
        allArguments();
        unknownOption();
        missingValue();
        duplicateOption();
    }

    private static void noArguments() {
        Config config = parse();
        Check.equal("args: no vfs", null, config.vfsPath());
        Check.equal("args: no script", null, config.scriptPath());
    }

    private static void allArguments() {
        Config config = parse("--script", "start.txt", "--vfs", "fs.csv");
        Check.equal("args: vfs", "fs.csv", config.vfsPath());
        Check.equal("args: script", "start.txt", config.scriptPath());
    }

    private static void unknownOption() {
        Check.contains("args: unknown option", error("--foo", "x"), "unknown option '--foo'");
    }

    private static void missingValue() {
        Check.contains("args: missing value", error("--vfs"), "requires a value");
    }

    private static void duplicateOption() {
        String message = error("--vfs", "a.csv", "--vfs", "b.csv");
        Check.contains("args: duplicate", message, "specified twice");
    }

    private static Config parse(String... args) {
        try {
            return ArgsParser.parse(args);
        } catch (ConfigException e) {
            throw new AssertionError("unexpected error: " + e.getMessage());
        }
    }

    private static String error(String... args) {
        try {
            ArgsParser.parse(args);
            return "no error";
        } catch (ConfigException e) {
            return e.getMessage();
        }
    }
}
