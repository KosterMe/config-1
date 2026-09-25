package emulator.vfs;

/**
 * Виртуальная файловая система, целиком хранящаяся в памяти.
 */
public class Vfs {

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
     * Возвращает источник VFS.
     *
     * @return путь к файлу VFS
     */
    public String source() {
        return source;
    }
}
