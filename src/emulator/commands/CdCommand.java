package emulator.commands;

import emulator.ShellException;
import emulator.ShellSession;
import emulator.vfs.Vfs;
import emulator.vfs.VfsDirectory;
import emulator.vfs.VfsNode;
import java.util.List;

/**
 * Команда {@code cd [путь]} — меняет текущий каталог.
 *
 * <p>Без аргумента выполняется переход в домашний каталог {@code ~}.
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
        String path = args.isEmpty() ? Vfs.HOME : args.get(0);
        VfsNode node = session.vfs().find(session.cwd(), path);
        if (node == null) {
            throw new ShellException(path + ": No such file or directory");
        }
        if (!node.isDirectory()) {
            throw new ShellException(path + ": Not a directory");
        }
        session.setCwd((VfsDirectory) node);
        return 0;
    }
}
