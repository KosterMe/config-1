package emulator;

/**
 * Ошибка выполнения команды эмулятора.
 *
 * <p>Сообщение исключения выводится пользователю в формате
 * {@code команда: сообщение}.
 */
public class ShellException extends Exception {

    private static final long serialVersionUID = 1L;

    /**
     * Создаёт исключение с сообщением для пользователя.
     *
     * @param message текст ошибки
     */
    public ShellException(String message) {
        super(message);
    }
}
