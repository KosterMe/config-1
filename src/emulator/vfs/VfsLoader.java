package emulator.vfs;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

/**
 * Загрузка VFS из CSV-файла.
 *
 * <p>Формат файла: первая строка — заголовок {@code path,type,content},
 * далее по одной строке на каждый элемент:
 * <ul>
 *   <li>{@code path} — абсолютный путь элемента, например {@code /home/user};</li>
 *   <li>{@code type} — {@code dir} для каталога или {@code file} для файла;</li>
 *   <li>{@code content} — содержимое файла в base64 (у каталога пусто).</li>
 * </ul>
 * Каталог должен быть описан раньше, чем элементы внутри него.
 */
public final class VfsLoader {

    /** Ожидаемый заголовок CSV-файла. */
    public static final String HEADER = "path,type,content";

    private static final int FIELD_COUNT = 3;
    private static final String BOM = "\uFEFF";

    private VfsLoader() {
    }

    /**
     * Загружает VFS из CSV-файла.
     *
     * @param path путь к CSV-файлу
     * @return загруженная VFS
     * @throws VfsException если файл не найден или имеет неверный формат
     */
    public static Vfs load(String path) throws VfsException {
        try {
            List<String> lines = Files.readAllLines(Path.of(path), StandardCharsets.UTF_8);
            return parse(lines, path);
        } catch (NoSuchFileException e) {
            throw new VfsException("file not found");
        } catch (IOException e) {
            throw new VfsException("cannot read file: " + e);
        }
    }

    /**
     * Строит VFS по строкам CSV-файла.
     *
     * @param lines строки файла
     * @param source название источника (путь к файлу)
     * @return построенная VFS
     * @throws VfsException если формат данных неверный
     */
    public static Vfs parse(List<String> lines, String source) throws VfsException {
        if (lines.isEmpty() || !lines.get(0).replace(BOM, "").strip().equals(HEADER)) {
            throw new VfsException("invalid format: first line must be '" + HEADER + "'");
        }
        VfsDirectory root = new VfsDirectory("");
        for (int i = 1; i < lines.size(); i++) {
            if (!lines.get(i).isBlank()) {
                addEntry(root, lines.get(i), i + 1);
            }
        }
        return new Vfs(root, source);
    }

    private static void addEntry(VfsDirectory root, String line, int lineNo)
            throws VfsException {
        String[] fields = line.split(",", -1);
        if (fields.length != FIELD_COUNT) {
            throw error(lineNo, "expected " + FIELD_COUNT + " fields, got " + fields.length);
        }
        List<String> names = splitPath(fields[0].strip(), lineNo);
        VfsDirectory parent = findParent(root, names, lineNo);
        String name = names.get(names.size() - 1);
        if (parent.get(name) != null) {
            throw error(lineNo, "duplicate path '" + fields[0].strip() + "'");
        }
        parent.add(createNode(name, fields[1].strip(), fields[2].strip(), lineNo));
    }

    private static List<String> splitPath(String path, int lineNo) throws VfsException {
        if (!path.startsWith("/")) {
            throw error(lineNo, "path must be absolute: '" + path + "'");
        }
        List<String> names = new ArrayList<>();
        for (String part : path.split("/")) {
            if (part.equals(".") || part.equals("..")) {
                throw error(lineNo, "path must not contain '.' or '..': '" + path + "'");
            }
            if (!part.isEmpty()) {
                names.add(part);
            }
        }
        if (names.isEmpty()) {
            throw error(lineNo, "root directory cannot be described");
        }
        return names;
    }

    private static VfsDirectory findParent(VfsDirectory root, List<String> names, int lineNo)
            throws VfsException {
        VfsDirectory current = root;
        for (String name : names.subList(0, names.size() - 1)) {
            VfsNode next = current.get(name);
            if (next == null || !next.isDirectory()) {
                throw error(lineNo, "parent directory '" + name + "' is not declared");
            }
            current = (VfsDirectory) next;
        }
        return current;
    }

    private static VfsNode createNode(String name, String type, String content, int lineNo)
            throws VfsException {
        if (type.equals("dir")) {
            if (!content.isEmpty()) {
                throw error(lineNo, "directory must not have content");
            }
            return new VfsDirectory(name);
        }
        if (type.equals("file")) {
            return new VfsFile(name, decode(content, lineNo));
        }
        throw error(lineNo, "unknown type '" + type + "' (expected 'dir' or 'file')");
    }

    private static byte[] decode(String content, int lineNo) throws VfsException {
        try {
            return Base64.getDecoder().decode(content);
        } catch (IllegalArgumentException e) {
            throw error(lineNo, "invalid base64 content");
        }
    }

    private static VfsException error(int lineNo, String message) {
        return new VfsException("invalid format at line " + lineNo + ": " + message);
    }
}
