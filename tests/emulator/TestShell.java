package emulator;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * Оболочка для тестов: весь вывод сохраняется в памяти.
 *
 * <p>Переводы строк в выводе приводятся к {@code \n}, чтобы тесты одинаково
 * работали в Windows ({@code \r\n}) и в UNIX-подобных ОС ({@code \n}).
 */
public class TestShell {

    private final ByteArrayOutputStream outBuffer = new ByteArrayOutputStream();
    private final ByteArrayOutputStream errBuffer = new ByteArrayOutputStream();
    private final ShellSession session;
    private final Shell shell;

    /**
     * Создаёт тестовую оболочку.
     */
    public TestShell() {
        PrintStream out = new PrintStream(outBuffer, true, StandardCharsets.UTF_8);
        PrintStream err = new PrintStream(errBuffer, true, StandardCharsets.UTF_8);
        session = new ShellSession(out, err, "tester", "testhost");
        shell = new Shell(session);
    }

    /**
     * Выполняет команду и очищает ранее накопленный вывод.
     *
     * @param line строка команды
     * @return код завершения
     */
    public int run(String line) {
        outBuffer.reset();
        errBuffer.reset();
        return shell.execute(line);
    }

    /**
     * Выполняет стартовый скрипт и очищает ранее накопленный вывод.
     *
     * @param lines строки скрипта
     * @return {@code true}, если скрипт выполнен без ошибок
     */
    public boolean runScript(List<String> lines) {
        outBuffer.reset();
        errBuffer.reset();
        return shell.runScript(lines);
    }

    /**
     * Возвращает обычный вывод последней команды.
     *
     * @return текст вывода
     */
    public String out() {
        return normalize(outBuffer);
    }

    /**
     * Возвращает вывод ошибок последней команды.
     *
     * @return текст ошибок
     */
    public String err() {
        return normalize(errBuffer);
    }

    /**
     * Возвращает сессию оболочки.
     *
     * @return сессия
     */
    public ShellSession session() {
        return session;
    }

    private static String normalize(ByteArrayOutputStream buffer) {
        return buffer.toString(StandardCharsets.UTF_8).replace(System.lineSeparator(), "\n");
    }
}
