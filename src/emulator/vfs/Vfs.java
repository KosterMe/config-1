package emulator.vfs;

/**
 * Виртуальная файловая система, целиком хранящаяся в памяти.
 */
public class Vfs {

    /** Обозначение домашнего каталога (совпадает с корнем VFS). */
    public static final String HOME = "~";

    private final VfsDirectory root;
    private final String source;

    /**
     * Создаёт VFS.
     *
     * @param root корневой каталог
     * @param source откуда загружена VFS (путь к файлу)
     */
    public Vfs(VfsDirectory root, String source) {
        this.root = root;
        this.source = source;
    }

    /**
     * Создаёт пустую VFS, в которой есть только корневой каталог.
     *
     * @return пустая VFS
     */
    public static Vfs empty() {
        return new Vfs(new VfsDirectory(""), "(empty)");
    }

    /**
     * Возвращает корневой каталог.
     *
     * @return корень VFS
     */
    public VfsDirectory root() {
        return root;
    }

    /**
     * Ищет элемент по пути.
     *
     * <p>Путь может быть абсолютным ({@code /etc}), относительным текущего
     * каталога ({@code docs/todo.txt}) или начинаться с {@code ~} (домашний
     * каталог, совпадает с корнем VFS). Поддерживаются {@code .} и {@code ..}.
     *
     * @param cwd текущий каталог
     * @param path путь
     * @return найденный элемент или {@code null}, если его нет
     */
    public VfsNode find(VfsDirectory cwd, String path) {
        boolean fromHome = path.equals(HOME) || path.startsWith(HOME + "/");
        VfsNode current = fromHome || path.startsWith("/") ? root : cwd;
        String rest = fromHome ? path.substring(HOME.length()) : path;
        for (String part : rest.split("/")) {
            current = step(current, part);
            if (current == null) {
                return null;
            }
        }
        return current;
    }

    private static VfsNode step(VfsNode current, String part) {
        if (!current.isDirectory()) {
            return null;
        }
        VfsDirectory dir = (VfsDirectory) current;
        if (part.isEmpty() || part.equals(".")) {
            return dir;
        }
        if (part.equals("..")) {
            return dir.parent() == null ? dir : dir.parent();
        }
        return dir.get(part);
    }

    /**
     * Возвращает источник VFS.
     *
     * @return путь к файлу VFS
     */
    public String source() {
        return source;
    }
}
