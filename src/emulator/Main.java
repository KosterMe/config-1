package emulator;

import emulator.config.ArgsParser;
import emulator.config.Config;
import emulator.config.ConfigException;
import emulator.vfs.Vfs;
import emulator.vfs.VfsException;
import emulator.vfs.VfsLoader;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.List;

/**
 * Точка входа эмулятора оболочки.
 */
public final class Main {

    private static final int EXIT_ERROR = 1;
    private static final int EXIT_USAGE = 2;

    private Main() {
    }

    /**
     * Запускает эмулятор: разбирает параметры, загружает VFS, выполняет
     * стартовый скрипт и переходит в интерактивный режим.
     *
     * @param args аргументы командной строки
     * @throws IOException при ошибке чтения ввода
     */
    public static void main(String[] args) throws IOException {
        Config config = parseArgs(args);
        printDebug(config);
        Vfs vfs = loadVfs(config.vfsPath());
        ShellSession session = new ShellSession(vfs, System.out, System.err,
                SystemInfo.userName(), SystemInfo.hostName());
        Shell shell = new Shell(session);
        if (config.scriptPath() != null) {
            shell.runScript(readScript(config.scriptPath()));
        }
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        System.exit(shell.runInteractive(in));
    }

    private static Config parseArgs(String[] args) {
        try {
            return ArgsParser.parse(args);
        } catch (ConfigException e) {
            System.err.println("emulator: " + e.getMessage());
            System.err.println(ArgsParser.USAGE);
            System.exit(EXIT_USAGE);
            return null;
        }
    }

    private static void printDebug(Config config) {
        System.out.println("[debug] vfs path: " + valueOrNotSet(config.vfsPath()));
        System.out.println("[debug] startup script: " + valueOrNotSet(config.scriptPath()));
    }

    private static String valueOrNotSet(String value) {
        return value == null ? "(not set)" : value;
    }

    private static Vfs loadVfs(String path) {
        if (path == null) {
            return Vfs.empty();
        }
        try {
            return VfsLoader.load(path);
        } catch (VfsException e) {
            System.err.println("emulator: cannot load VFS '" + path + "': " + e.getMessage());
            System.exit(EXIT_ERROR);
            return null;
        }
    }

    private static List<String> readScript(String path) {
        try {
            return Files.readAllLines(Path.of(path), StandardCharsets.UTF_8);
        } catch (IOException e) {
            String reason = e instanceof NoSuchFileException ? "file not found" : e.toString();
            System.err.println("emulator: cannot read startup script '" + path + "': " + reason);
            System.exit(EXIT_ERROR);
            return List.of();
        }
    }
}
