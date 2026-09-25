package emulator.config;

import java.util.HashMap;
import java.util.Map;

/**
 * Разбор параметров командной строки эмулятора.
 *
 * <p>Поддерживаемые параметры: {@code --vfs <путь>} и {@code --script <путь>}.
 */
public final class ArgsParser {

    /** Подсказка по использованию. */
    public static final String USAGE = "usage: emulator [--vfs <path>] [--script <path>]";

    private static final String VFS = "--vfs";
    private static final String SCRIPT = "--script";

    private ArgsParser() {
    }

    /**
     * Разбирает аргументы командной строки.
     *
     * @param args аргументы из {@code main}
     * @return параметры запуска
     * @throws ConfigException если параметр неизвестен, повторяется или не имеет значения
     */
    public static Config parse(String[] args) throws ConfigException {
        Map<String, String> values = new HashMap<>();
        for (int i = 0; i < args.length; i += 2) {
            String option = args[i];
            if (!option.equals(VFS) && !option.equals(SCRIPT)) {
                throw new ConfigException("unknown option '" + option + "'");
            }
            if (values.containsKey(option)) {
                throw new ConfigException("option '" + option + "' is specified twice");
            }
            int valueIndex = i + 1;
            if (valueIndex >= args.length) {
                throw new ConfigException("option '" + option + "' requires a value");
            }
            values.put(option, args[valueIndex]);
        }
        return new Config(values.get(VFS), values.get(SCRIPT));
    }
}
