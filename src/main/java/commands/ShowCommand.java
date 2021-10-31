package commands;

import monitoring.Control;
import monitoring.ServerAssistant;
import stored.Movie;

/**
 * Команда выводит элементы коллекции в строков представлении
 */
public class ShowCommand extends Command {

    public ShowCommand() {
        super("show", "вывести в стандартный поток вывода " +
                "все элементы коллекции в строковом представлении");
    }
    @Override
    public void commit(ServerAssistant assistant, boolean visible) {
        for (Movie i : assistant.getMovies()) {
            assistant.getClientMessage().add(i.toCSV());
        }
        if (assistant.getMovies().size() == 0) {
            assistant.getClientMessage().add("Похоже в коллекции нет элементов...\n");
        }
    }
}
