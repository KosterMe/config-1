package emulator;

import emulator.vfs.Vfs;
import emulator.vfs.VfsException;
import emulator.vfs.VfsLoader;
import java.util.List;

/**
 * Тестовая VFS.
 *
 * <pre>
 * /
 * |-- etc/hostname          "emulator"
 * `-- home/user/
 *     |-- docs/             (пустой каталог)
 *     |-- empty.txt         ""
 *     `-- notes.txt         "abc\n123\n"
 * </pre>
 */
public final class TestVfs {

    private TestVfs() {
    }

    /**
     * Создаёт новую тестовую VFS.
     *
     * @return VFS
     */
    public static Vfs sample() {
        List<String> lines = List.of(
                "path,type,content",
                "/etc,dir,",
                "/etc/hostname,file,ZW11bGF0b3I=",
                "/home,dir,",
                "/home/user,dir,",
                "/home/user/docs,dir,",
                "/home/user/empty.txt,file,",
                "/home/user/notes.txt,file,YWJjCjEyMwo=");
        try {
            return VfsLoader.parse(lines, "test");
        } catch (VfsException e) {
            throw new AssertionError(e.getMessage());
        }
    }
}
