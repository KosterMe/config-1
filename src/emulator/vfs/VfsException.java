package emulator.vfs;

/**
 * Ошибка загрузки VFS: файл не найден или имеет неверный формат.
 */
public class VfsException extends Exception {

    private static final long serialVersionUID = 1L;

    /**
     * Создаёт исключение с описанием ошибки.
     *
     * @param message текст ошибки
     */
    public VfsException(String message) {
        super(message);
    }
}
