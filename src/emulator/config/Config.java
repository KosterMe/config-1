package emulator.config;

/**
 * Параметры запуска эмулятора.
 *
 * @param vfsPath путь к файлу VFS или {@code null}, если не задан
 * @param scriptPath путь к стартовому скрипту или {@code null}, если не задан
 */
public record Config(String vfsPath, String scriptPath) {
}
