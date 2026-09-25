package emulator.commands;

import emulator.ShellException;
import emulator.ShellSession;
import java.util.List;

/**
 * Команда-заглушка {@code cd [путь]}: выводит своё имя и аргументы.
 *
 * <p>Как и настоящая команда {@code cd}, принимает не больше одного аргумента.
 */
public class CdCommand implements Command {

    private static final int MAX_ARGS = 1;

    @Override
    public String name() {
        return "cd";
    }

    @Override
    public int execute(List<String> args, ShellSession session) throws ShellException {
        if (args.size() > MAX_ARGS) {
            throw new ShellException("too many arguments");
        }
        session.out().println(name() + ": stub, arguments: " + args);
        return 0;
    }
}
