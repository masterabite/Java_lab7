package commands;

import monitoring.ServerAssistant;
import stored.Movie;

import java.util.Comparator;

/**
 * Команда выводит первый объект из коллекции
 * значение поля totalBoxOffice которого является максимальным
 */
public class MaxByTotalBoxOfficeCommand extends Command {

    public MaxByTotalBoxOfficeCommand() {
        super("max_by_total_box_office", "вывести любой объект из коллекции, " +
                "значение поля totalBoxOffice которого является максимальным");
    }

    @Override
    public void commit(ServerAssistant assistant, boolean visible) {
        assistant.getClientMessage().add(
                assistant.getMovies().stream().max(Comparator.comparing(Movie::getTotalBoxOffice)).get().toCSV()
        );
    }
}

