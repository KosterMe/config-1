package emulator.vfs;

/**
 * Файл виртуальной файловой системы. Содержимое хранится в памяти.
 */
public class VfsFile extends VfsNode {

    private final byte[] data;

    /**
     * Создаёт файл.
     *
     * @param name имя файла
     * @param data содержимое файла
     */
    public VfsFile(String name, byte[] data) {
        super(name);
        this.data = data.clone();
    }

    /**
     * Возвращает копию содержимого файла.
     *
     * @return байты файла
     */
    public byte[] data() {
        return data.clone();
    }

    /**
     * Возвращает размер файла.
     *
     * @return размер содержимого в байтах
     */
    public int size() {
        return data.length;
    }

    @Override
    public boolean isDirectory() {
        return false;
    }

    @Override
    public VfsNode copy(String newName) {
        return new VfsFile(newName, data);
    }
}
