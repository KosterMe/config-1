package emulator;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Сведения о реальной ОС, в которой запущен эмулятор.
 *
 * <p>Используются для формирования приглашения к вводу вида
 * {@code username@hostname:~$}.
 */
public final class SystemInfo {

    private static final String FALLBACK_USER = "user";
    private static final String FALLBACK_HOST = "localhost";

    private SystemInfo() {
    }

    /**
     * Возвращает имя текущего пользователя ОС.
     *
     * @return имя пользователя или {@code user}, если определить его не удалось
     */
    public static String userName() {
        String name = System.getProperty("user.name");
        return isBlank(name) ? FALLBACK_USER : name;
    }

    /**
     * Возвращает сетевое имя компьютера.
     *
     * <p>Сначала проверяются переменные окружения {@code COMPUTERNAME}
     * (Windows) и {@code HOSTNAME} (UNIX), затем имя запрашивается у сетевой
     * подсистемы.
     *
     * @return имя хоста или {@code localhost}, если определить его не удалось
     */
    public static String hostName() {
        for (String variable : new String[] {"COMPUTERNAME", "HOSTNAME"}) {
            String value = System.getenv(variable);
            if (!isBlank(value)) {
                return value;
            }
        }
        try {
            return InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            return FALLBACK_HOST;
        }
    }

    private static boolean isBlank(String value) {
        return value == null || value.isBlank();
    }
}
