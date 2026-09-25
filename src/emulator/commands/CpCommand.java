package emulator.commands;

import emulator.ShellException;
import emulator.ShellSession;
import emulator.vfs.VfsDirectory;
import emulator.vfs.VfsNode;
import java.util.ArrayList;
import java.util.List;

/**
 * Команда {@code cp [-r] источник назначение} — копирует файл или каталог.
 *
 * <p>Если назначение — существующий каталог, источник копируется внутрь
 * него под своим именем. Иначе создаётся (или перезаписывается) элемент
 * с именем из пути назначения. Каталоги копируются только с опцией
 * {@code -r} ({@code -R}, {@code --recursive}); если каталог назначения уже
 * есть, содержимое объединяется. Все изменения выполняются только в памяти.
 */
public class CpCommand implements Command {

    private static final int OPERAND_COUNT = 2;
    private static final String RECURSIVE_SHORT = "-[rR]+";
    private static final String RECURSIVE_LONG = "--recursive";

    @Override
    public String name() {
        return "cp";
    }

    @Override
    public int execute(List<String> args, ShellSession session) throws ShellException {
        boolean recursive = false;
        List<String> operands = new ArrayList<>();
        for (String arg : args) {
            if (arg.matches(RECURSIVE_SHORT) || arg.equals(RECURSIVE_LONG)) {
                recursive = true;
            } else if (arg.startsWith("-")) {
                throw new ShellException("invalid option '" + arg + "'");
            } else {
                operands.add(arg);
            }
        }
        checkOperands(operands);
        copy(operands.get(0), operands.get(1), recursive, session);
        return 0;
    }

    private void checkOperands(List<String> operands) throws ShellException {
        if (operands.isEmpty()) {
            throw new ShellException("missing file operand");
        }
        if (operands.size() < OPERAND_COUNT) {
            throw new ShellException("missing destination file operand after '"
                    + operands.get(0) + "'");
        }
        if (operands.size() > OPERAND_COUNT) {
            throw new ShellException("too many arguments");
        }
    }

    private void copy(String from, String to, boolean recursive, ShellSession session)
            throws ShellException {
        VfsNode source = session.vfs().find(session.cwd(), from);
        if (source == null) {
            throw new ShellException("cannot stat '" + from + "': No such file or directory");
        }
        if (source.isDirectory() && !recursive) {
            throw new ShellException("-r not specified; omitting directory '" + from + "'");
        }
        VfsNode dest = session.vfs().find(session.cwd(), to);
        VfsDirectory parent;
        String name;
        if (dest != null && dest.isDirectory()) {
            parent = (VfsDirectory) dest;
            name = source.name();
        } else {
            parent = findParent(to, session);
            name = to.substring(to.lastIndexOf('/') + 1);
        }
        if (isInside(parent, source)) {
            throw new ShellException("cannot copy a directory, '" + from
                    + "', into itself, '" + to + "'");
        }
        place(source, parent, name);
    }

    private VfsDirectory findParent(String path, ShellSession session) throws ShellException {
        int slash = path.lastIndexOf('/');
        String parentPath = "/";
        if (slash < 0) {
            parentPath = ".";
        } else if (slash > 0) {
            parentPath = path.substring(0, slash);
        }
        VfsNode parent = session.vfs().find(session.cwd(), parentPath);
        if (parent == null || !parent.isDirectory()) {
            throw new ShellException("cannot create '" + path + "': No such file or directory");
        }
        return (VfsDirectory) parent;
    }

    private boolean isInside(VfsDirectory directory, VfsNode node) {
        for (VfsNode current = directory; current != null; current = current.parent()) {
            if (current == node) {
                return true;
            }
        }
        return false;
    }

    private void place(VfsNode source, VfsDirectory parent, String name) throws ShellException {
        VfsNode existing = parent.get(name);
        if (existing == null) {
            parent.add(source.copy(name));
            return;
        }
        if (existing == source) {
            throw new ShellException("'" + source.path() + "' and '" + existing.path()
                    + "' are the same file");
        }
        if (existing.isDirectory() != source.isDirectory()) {
            throw new ShellException("cannot overwrite '" + existing.path() + "' with '"
                    + source.path() + "': types differ (file and directory)");
        }
        if (!source.isDirectory()) {
            parent.add(source.copy(name));
            return;
        }
        for (VfsNode child : ((VfsDirectory) source).children()) {
            place(child, (VfsDirectory) existing, child.name());
        }
    }
}
