package emulator.commands;

import emulator.ShellException;
import emulator.ShellSession;
import java.util.List;

/**
 * Команда эмулятора оболочки.
 */
public interface Command {

    /**
     * Возвращает имя, по которому команда вызывается.
     *
     * @return имя команды
     */
    String name();

    /**
     * Выполняет команду.
     *
     * @param args аргументы команды
     * @param session текущее состояние оболочки
     * @return код завершения (0 — успех)
     * @throws ShellException если команда завершилась с ошибкой
     */
    int execute(List<String> args, ShellSession session) throws ShellException;
}
