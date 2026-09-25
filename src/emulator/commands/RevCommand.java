package emulator.commands;

import emulator.ShellException;
import emulator.ShellSession;
import emulator.vfs.VfsFile;
import emulator.vfs.VfsNode;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * Команда {@code rev файл...} — выводит строки файлов, записывая символы
 * каждой строки в обратном порядке.
 */
public class RevCommand implements Command {

    @Override
    public String name() {
        return "rev";
    }

    @Override
    public int execute(List<String> args, ShellSession session) throws ShellException {
        if (args.isEmpty()) {
            throw new ShellException("missing file operand");
        }
        int status = 0;
        for (String path : args) {
            try {
                printReversed(path, session);
            } catch (ShellException e) {
                session.printError("rev: " + e.getMessage());
                status = 1;
            }
        }
        return status;
    }

    private void printReversed(String path, ShellSession session) throws ShellException {
        VfsNode node = session.vfs().find(session.cwd(), path);
        if (node == null) {
            throw new ShellException("cannot open " + path + ": No such file or directory");
        }
        if (node.isDirectory()) {
            throw new ShellException(path + ": Is a directory");
        }
        String text = new String(((VfsFile) node).data(), StandardCharsets.UTF_8);
        for (String line : text.lines().toList()) {
            session.out().println(new StringBuilder(line).reverse());
        }
    }
}
