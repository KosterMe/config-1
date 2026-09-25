package emulator.commands;

import emulator.ShellException;
import emulator.ShellSession;
import emulator.vfs.VfsDirectory;
import emulator.vfs.VfsNode;
import java.util.ArrayList;
import java.util.List;

/**
 * Команда {@code ls [путь...]} — выводит содержимое каталогов.
 *
 * <p>Без аргументов выводится текущий каталог. Для файла выводится его имя.
 * Если путей несколько, перед содержимым каждого каталога печатается его имя,
 * а блоки каталогов отделяются пустой строкой.
 */
public class LsCommand implements Command {

    private static final int MANY_PATHS = 2;

    @Override
    public String name() {
        return "ls";
    }

    @Override
    public int execute(List<String> args, ShellSession session) throws ShellException {
        checkOptions(args);
        List<String> paths = args.isEmpty() ? List.of(".") : args;
        boolean withHeaders = paths.size() >= MANY_PATHS;
        int status = 0;
        boolean printed = false;
        for (String path : paths) {
            VfsNode node = session.vfs().find(session.cwd(), path);
            if (node == null) {
                session.printError("ls: cannot access '" + path + "': No such file or directory");
                status = 1;
                continue;
            }
            if (printed && node.isDirectory()) {
                session.out().println();
            }
            print(path, node, withHeaders, session);
            printed = true;
        }
        return status;
    }

    private void checkOptions(List<String> args) throws ShellException {
        for (String arg : args) {
            if (arg.startsWith("-")) {
                throw new ShellException("invalid option '" + arg + "'");
            }
        }
    }

    private void print(String path, VfsNode node, boolean withHeader, ShellSession session) {
        if (!node.isDirectory()) {
            session.out().println(path);
            return;
        }
        if (withHeader) {
            session.out().println(path + ":");
        }
        List<String> names = new ArrayList<>();
        for (VfsNode child : ((VfsDirectory) node).children()) {
            names.add(child.name());
        }
        if (!names.isEmpty()) {
            session.out().println(String.join("  ", names));
        }
    }
}
