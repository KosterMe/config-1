package emulator;

import java.util.List;

/**
 * Разобранная строка ввода: имя команды и её аргументы.
 *
 * @param name имя команды
 * @param args аргументы команды
 */
public record ParsedCommand(String name, List<String> args) {
}
