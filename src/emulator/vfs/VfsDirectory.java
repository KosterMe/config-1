package emulator.vfs;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Каталог виртуальной файловой системы.
 *
 * <p>Дочерние элементы хранятся отсортированными по имени.
 */
public class VfsDirectory extends VfsNode {

    private final Map<String, VfsNode> children = new TreeMap<>();

    /**
     * Создаёт пустой каталог.
     *
     * @param name имя каталога
     */
    public VfsDirectory(String name) {
        super(name);
    }

    /**
     * Добавляет элемент в каталог (элемент с таким же именем заменяется).
     *
     * @param node добавляемый элемент
     */
    public void add(VfsNode node) {
        node.setParent(this);
        children.put(node.name(), node);
    }

    /**
     * Ищет дочерний элемент по имени.
     *
     * @param name имя элемента
     * @return элемент или {@code null}, если его нет
     */
    public VfsNode get(String name) {
        return children.get(name);
    }

    /**
     * Возвращает список дочерних элементов, отсортированный по имени.
     *
     * @return дочерние элементы
     */
    public List<VfsNode> children() {
        return new ArrayList<>(children.values());
    }

    /**
     * Считает файлы в каталоге и во всех вложенных каталогах.
     *
     * @return количество файлов
     */
    public int countFiles() {
        int count = 0;
        for (VfsNode child : children.values()) {
            if (child.isDirectory()) {
                count += ((VfsDirectory) child).countFiles();
            } else {
                count++;
            }
        }
        return count;
    }

    /**
     * Считает вложенные каталоги на всех уровнях (сам каталог не учитывается).
     *
     * @return количество каталогов
     */
    public int countDirectories() {
        int count = 0;
        for (VfsNode child : children.values()) {
            if (child.isDirectory()) {
                count += 1 + ((VfsDirectory) child).countDirectories();
            }
        }
        return count;
    }

    @Override
    public boolean isDirectory() {
        return true;
    }

    @Override
    public VfsNode copy(String newName) {
        VfsDirectory copy = new VfsDirectory(newName);
        for (VfsNode child : children.values()) {
            copy.add(child.copy(child.name()));
        }
        return copy;
    }
}
