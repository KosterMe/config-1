package emulator.commands;

import emulator.ShellException;
import emulator.ShellSession;
import java.util.List;

/**
 * Команда {@code exit [код]} — завершает работу эмулятора.
 *
 * <p>Без аргумента используется код завершения последней команды.
 */
public class ExitCommand implements Command {

    private static final int MAX_ARGS = 1;

    @Override
    public String name() {
        return "exit";
    }

    @Override
    public int execute(List<String> args, ShellSession session) throws ShellException {
        if (args.size() > MAX_ARGS) {
            throw new ShellException("too many arguments");
        }
        int code = session.lastStatus();
        if (!args.isEmpty()) {
            code = parseCode(args.get(0));
        }
        session.requestExit(code);
        return code;
    }

    private int parseCode(String text) throws ShellException {
        try {
            return Integer.parseInt(text);
        } catch (NumberFormatException e) {
            throw new ShellException(text + ": numeric argument required");
        }
    }
}
