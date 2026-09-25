package emulator.config;

/**
 * Ошибка в параметрах командной строки.
 */
public class ConfigException extends Exception {

    private static final long serialVersionUID = 1L;

    /**
     * Создаёт исключение с описанием ошибки.
     *
     * @param message текст ошибки
     */
    public ConfigException(String message) {
        super(message);
    }
}
