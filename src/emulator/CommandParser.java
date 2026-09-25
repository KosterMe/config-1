package emulator;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * Простой парсер строки ввода.
 *
 * <p>Разделяет строку на команду и аргументы по пробельным символам.
 * Первое слово считается именем команды, остальные — аргументами.
 */
public final class CommandParser {

    private static final String WHITESPACE = "\\s+";

    /**
     * Разбирает строку ввода.
     *
     * @param line строка, введённая пользователем
     * @return разобранная команда или пустой {@link Optional}, если строка пустая
     */
    public Optional<ParsedCommand> parse(String line) {
        String trimmed = line == null ? "" : line.strip();
        if (trimmed.isEmpty()) {
            return Optional.empty();
        }
        List<String> tokens = Arrays.asList(trimmed.split(WHITESPACE));
        return Optional.of(new ParsedCommand(tokens.get(0), tokens.subList(1, tokens.size())));
    }
}
