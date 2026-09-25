package emulator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Точка входа эмулятора оболочки.
 */
public final class Main {

    private Main() {
    }

    /**
     * Запускает эмулятор в интерактивном режиме.
     *
     * @param args аргументы командной строки (не используются)
     * @throws IOException при ошибке чтения ввода
     */
    public static void main(String[] args) throws IOException {
        ShellSession session = new ShellSession(System.out, System.err,
                SystemInfo.userName(), SystemInfo.hostName());
        Shell shell = new Shell(session);
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        System.exit(shell.runInteractive(in));
    }
}
