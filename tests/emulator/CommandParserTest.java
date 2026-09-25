package emulator;

import java.util.List;
import java.util.Optional;

/**
 * Тесты парсера команд.
 */
public final class CommandParserTest {

    private static final CommandParser PARSER = new CommandParser();

    private CommandParserTest() {
    }

    /**
     * Запускает все тесты класса.
     */
    public static void run() {
        splitsBySpaces();
        ignoresExtraSpaces();
        emptyLineGivesNothing();
    }

    private static void splitsBySpaces() {
        ParsedCommand cmd = PARSER.parse("ls -l /home").orElseThrow();
        Check.equal("parser: command name", "ls", cmd.name());
        Check.equal("parser: arguments", List.of("-l", "/home"), cmd.args());
    }

    private static void ignoresExtraSpaces() {
        ParsedCommand cmd = PARSER.parse("   cd    docs   ").orElseThrow();
        Check.equal("parser: extra spaces name", "cd", cmd.name());
        Check.equal("parser: extra spaces args", List.of("docs"), cmd.args());
    }

    private static void emptyLineGivesNothing() {
        Check.equal("parser: empty line", Optional.empty(), PARSER.parse("    "));
    }
}
