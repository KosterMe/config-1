package emulator.commands;

import emulator.ShellException;
import emulator.ShellSession;
import java.io.IOException;
import java.util.List;

/**
 * Команда {@code clear} — очищает экран терминала.
 *
 * <p>Используется управляющая последовательность ANSI. Если вывод оболочки
 * идёт прямо в консоль Windows, вызывается {@code cls}, так как консоль cmd
 * не понимает ANSI-последовательности. При перенаправленном выводе (в файл,
 * в тестах) всегда пишется ANSI-последовательность.
 */
public class ClearCommand implements Command {

    private static final String ANSI_CLEAR = "\033[H\033[2J";

    @Override
    public String name() {
        return "clear";
    }

    @Override
    public int execute(List<String> args, ShellSession session) throws ShellException {
        if (!args.isEmpty()) {
            throw new ShellException("too many arguments");
        }
        session.out().flush();
        if (!(writesToWindowsConsole(session) && runCls())) {
            session.out().print(ANSI_CLEAR);
            session.out().flush();
        }
        return 0;
    }

    private boolean writesToWindowsConsole(ShellSession session) {
        String os = System.getProperty("os.name", "");
        return os.startsWith("Windows") && System.console() != null
                && session.out() == System.out;
    }

    private boolean runCls() {
        try {
            new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            return true;
        } catch (IOException e) {
            return false;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return false;
        }
    }
}
