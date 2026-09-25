package emulator.commands;

import emulator.ShellSession;
import java.util.List;

/**
 * Команда-заглушка {@code ls}: выводит своё имя и аргументы.
 */
public class LsCommand implements Command {

    @Override
    public String name() {
        return "ls";
    }

    @Override
    public int execute(List<String> args, ShellSession session) {
        session.out().println(name() + ": stub, arguments: " + args);
        return 0;
    }
}
