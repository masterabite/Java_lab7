package commands;

import monitoring.ServerAssistant;

/**
 * Команды выводит информацию о доступных ногах
 */
public class HelpCommand extends Command {

    public HelpCommand() {
        super("help", "получить список доступных комманд");
    }

    @Override
    public void commit(ServerAssistant assistant, boolean visible) {
        for (Command currentCommand: assistant.getCommands()) {
            assistant.getClientMessage().add(currentCommand.getHelp() + "\n");
        }
    }
}
