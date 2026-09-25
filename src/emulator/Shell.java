package emulator;

import emulator.commands.CdCommand;
import emulator.commands.Command;
import emulator.commands.ExitCommand;
import emulator.commands.LsCommand;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Интерпретатор команд эмулятора (цикл REPL).
 */
public class Shell {

    private static final int STATUS_ERROR = 1;

    private final ShellSession session;
    private final CommandParser parser = new CommandParser();
    private final Map<String, Command> commands = new HashMap<>();

    /**
     * Создаёт оболочку и регистрирует доступные команды.
     *
     * @param session состояние оболочки
     */
    public Shell(ShellSession session) {
        this.session = session;
        register(new LsCommand());
        register(new CdCommand());
        register(new ExitCommand());
    }

    private void register(Command command) {
        commands.put(command.name(), command);
    }

    /**
     * Запускает интерактивный режим: читает команды, пока не будет вызван
     * {@code exit} или не закончится ввод.
     *
     * @param in источник пользовательского ввода
     * @return код завершения эмулятора
     * @throws IOException при ошибке чтения ввода
     */
    public int runInteractive(BufferedReader in) throws IOException {
        while (!session.isExitRequested()) {
            session.out().print(session.prompt());
            session.out().flush();
            String line = in.readLine();
            if (line == null) {
                session.out().println();
                return session.lastStatus();
            }
            execute(line);
        }
        return session.exitCode();
    }

    /**
     * Разбирает и выполняет одну строку ввода.
     *
     * @param line строка ввода
     * @return код завершения команды
     */
    public int execute(String line) {
        Optional<ParsedCommand> parsed = parser.parse(line);
        if (parsed.isEmpty()) {
            return session.lastStatus();
        }
        int status = run(parsed.get());
        session.setLastStatus(status);
        return status;
    }

    private int run(ParsedCommand parsed) {
        Command command = commands.get(parsed.name());
        if (command == null) {
            session.printError(parsed.name() + ": command not found");
            return STATUS_ERROR;
        }
        try {
            return command.execute(parsed.args(), session);
        } catch (ShellException e) {
            session.printError(parsed.name() + ": " + e.getMessage());
            return STATUS_ERROR;
        }
    }
}
