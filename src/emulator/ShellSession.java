package emulator;

import emulator.vfs.Vfs;
import emulator.vfs.VfsDirectory;
import java.io.PrintStream;

/**
 * Состояние оболочки: VFS, текущий каталог, потоки вывода, данные
 * пользователя и признак выхода.
 */
public class ShellSession {

    private final Vfs vfs;
    private VfsDirectory cwd;
    private final PrintStream out;
    private final PrintStream err;
    private final String user;
    private final String host;
    private boolean exitRequested;
    private int exitCode;
    private int lastStatus;

    /**
     * Создаёт сессию оболочки.
     *
     * @param vfs виртуальная файловая система
     * @param out поток для обычного вывода
     * @param err поток для сообщений об ошибках
     * @param user имя пользователя для приглашения
     * @param host имя компьютера для приглашения
     */
    public ShellSession(Vfs vfs, PrintStream out, PrintStream err, String user, String host) {
        this.vfs = vfs;
        this.cwd = vfs.root();
        this.out = out;
        this.err = err;
        this.user = user;
        this.host = host;
    }

    /**
     * Формирует приглашение к вводу вида {@code user@host:~/docs$ }.
     * Домашний каталог {@code ~} совпадает с корнем VFS.
     *
     * @return строка приглашения
     */
    public String prompt() {
        String path = cwd.path();
        String shown = path.equals("/") ? Vfs.HOME : Vfs.HOME + path;
        return user + "@" + host + ":" + shown + "$ ";
    }

    /**
     * Выводит сообщение об ошибке в поток ошибок.
     *
     * @param message текст ошибки
     */
    public void printError(String message) {
        out.flush();
        err.println(message);
        err.flush();
    }

    /**
     * Запрашивает завершение работы оболочки.
     *
     * @param code код завершения
     */
    public void requestExit(int code) {
        exitRequested = true;
        exitCode = code;
    }

    /**
     * Возвращает виртуальную файловую систему.
     *
     * @return VFS
     */
    public Vfs vfs() {
        return vfs;
    }

    /**
     * Возвращает текущий каталог.
     *
     * @return текущий каталог
     */
    public VfsDirectory cwd() {
        return cwd;
    }

    /**
     * Меняет текущий каталог.
     *
     * @param cwd новый текущий каталог
     */
    public void setCwd(VfsDirectory cwd) {
        this.cwd = cwd;
    }

    /**
     * Возвращает поток обычного вывода.
     *
     * @return поток вывода
     */
    public PrintStream out() {
        return out;
    }

    /**
     * Проверяет, был ли запрошен выход.
     *
     * @return {@code true}, если была выполнена команда {@code exit}
     */
    public boolean isExitRequested() {
        return exitRequested;
    }

    /**
     * Возвращает код, переданный команде {@code exit}.
     *
     * @return код завершения
     */
    public int exitCode() {
        return exitCode;
    }

    /**
     * Возвращает код завершения последней выполненной команды.
     *
     * @return код завершения
     */
    public int lastStatus() {
        return lastStatus;
    }

    /**
     * Сохраняет код завершения последней выполненной команды.
     *
     * @param status код завершения
     */
    public void setLastStatus(int status) {
        lastStatus = status;
    }
}
