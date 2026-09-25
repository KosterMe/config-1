package emulator.vfs;

/**
 * Элемент виртуальной файловой системы: файл или каталог.
 */
public abstract class VfsNode {

    private final String name;
    private VfsDirectory parent;

    /**
     * Создаёт элемент VFS.
     *
     * @param name имя элемента
     */
    protected VfsNode(String name) {
        this.name = name;
    }

    /**
     * Возвращает имя элемента.
     *
     * @return имя
     */
    public String name() {
        return name;
    }

    /**
     * Возвращает родительский каталог.
     *
     * @return родитель или {@code null} для корня
     */
    public VfsDirectory parent() {
        return parent;
    }

    /**
     * Устанавливает родительский каталог.
     *
     * @param parent новый родитель
     */
    void setParent(VfsDirectory parent) {
        this.parent = parent;
    }

    /**
     * Возвращает абсолютный путь элемента, например {@code /home/user}.
     *
     * @return путь от корня VFS
     */
    public String path() {
        if (parent == null) {
            return "/";
        }
        String parentPath = parent.path();
        return parentPath.equals("/") ? "/" + name : parentPath + "/" + name;
    }

    /**
     * Проверяет, является ли элемент каталогом.
     *
     * @return {@code true} для каталога
     */
    public abstract boolean isDirectory();
}
