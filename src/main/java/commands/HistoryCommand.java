package commands;

import monitoring.ServerAssistant;

/**
 * Команда выводит последние 7 команд (без их аргументов)
 */
public class HistoryCommand extends Command {

    public HistoryCommand() {
        super("history", " вывести последние 7 команд (без их аргументов)");
    }

    @Override
    public void commit(ServerAssistant assistant, boolean visible) {
        assistant.getClientMessage().add("Последние 7 комманд: ");
        for (int i = assistant.getHistory().size() - 1; i >= Math.max(assistant.getHistory().size() - 7, 0); --i) {
            assistant.getClientMessage().add(assistant.getHistory().get(i) + ' ');
        }
        assistant.getClientMessage().add("\n");
    }
}
