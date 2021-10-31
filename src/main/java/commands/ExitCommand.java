package commands;

import monitoring.ServerAssistant;

/**
 * Команда для прекращения работы приложения
 */
public class ExitCommand extends Command {

    public ExitCommand() {
        super("exit", "завершить программу");
    }
    @Override
    public void commit(ServerAssistant assistant, boolean visible) {
        System.out.println("Прекращение работы сервера...");
        assistant.commitCommand(new SaveCommand());
        assistant.stop();
    }
}
