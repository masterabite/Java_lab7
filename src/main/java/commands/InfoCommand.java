package commands;

import monitoring.ServerAssistant;

/**
 * Команда выводит информацию о коллекции
 */
public class InfoCommand extends Command {

    public InfoCommand() {
        super("info", "вывести в стандартный поток вывода " +
                "информацию о коллекции (тип, дата инициализации, количество элементов и т.д.)");
    }

    @Override
    public void commit(ServerAssistant assistant, boolean visible) {
        assistant.getClientMessage().add("Дата инициализации коллекции: ");
        assistant.getClientMessage().add(assistant.getMoviesInitializationDate().toString() + '\n');
        assistant.getClientMessage().add("Тип коллекции: ");
        assistant.getClientMessage().add(assistant.getMovies().getClass().toString() + '\n');
        assistant.getClientMessage().add("Количество элементов в коллекци: ");
        assistant.getClientMessage().add(String.valueOf(assistant.getMovies().size()) + '\n');
    }

}
