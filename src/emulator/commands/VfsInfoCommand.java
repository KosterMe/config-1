package emulator.commands;

import emulator.ShellException;
import emulator.ShellSession;
import emulator.vfs.Vfs;
import emulator.vfs.VfsDirectory;
import emulator.vfs.VfsFile;
import emulator.vfs.VfsNode;
import java.io.PrintStream;
import java.util.Iterator;
import java.util.List;

/**
 * Служебная команда {@code vfs-info}: выводит источник VFS, её дерево
 * (как утилита {@code tree}) и число каталогов и файлов.
 */
public class VfsInfoCommand implements Command {

    private static final String BRANCH = "|-- ";
    private static final String LAST_BRANCH = "`-- ";
    private static final String INDENT = "|   ";
    private static final String LAST_INDENT = "    ";

    @Override
    public String name() {
        return "vfs-info";
    }

    @Override
    public int execute(List<String> args, ShellSession session) throws ShellException {
        if (!args.isEmpty()) {
            throw new ShellException("too many arguments");
        }
        Vfs vfs = session.vfs();
        PrintStream out = session.out();
        out.println("source: " + vfs.source());
        out.println("/");
        printTree(vfs.root(), "", out);
        out.println();
        out.println(vfs.root().countDirectories() + " directories, "
                + vfs.root().countFiles() + " files");
        return 0;
    }

    private void printTree(VfsDirectory directory, String indent, PrintStream out) {
        Iterator<VfsNode> children = directory.children().iterator();
        while (children.hasNext()) {
            VfsNode child = children.next();
            boolean last = !children.hasNext();
            out.println(indent + (last ? LAST_BRANCH : BRANCH) + describe(child));
            if (child.isDirectory()) {
                printTree((VfsDirectory) child, indent + (last ? LAST_INDENT : INDENT), out);
            }
        }
    }

    private String describe(VfsNode node) {
        if (node.isDirectory()) {
            return node.name() + "/";
        }
        return node.name() + " (" + ((VfsFile) node).size() + " B)";
    }
}
