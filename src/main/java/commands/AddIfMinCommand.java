package commands;

import exceptions.*;
import monitoring.ServerAssistant;
import stored.Movie;

import java.text.ParseException;


/**
 * Команда добавляет объект в коллекцию из консоли/скрипта если он меньше минимального
 */
public class AddIfMinCommand extends Command {
    public AddIfMinCommand() {
        super("add_if_min", "добавить новый элемент в коллекцию, если его значение меньше, " +
                "чем у наименьшего элемента этой коллекции");
    }

    @Override
    public void commit(ServerAssistant assistant, boolean visible) {
        try {
            Movie movie = assistant.getReader().nextMovie(new Movie());
            if (0 <= assistant.getMovieComparator().compare
                    (
                            movie,
                            assistant.getMovies().stream().min(assistant.getMovieComparator()).get()
                    )
            ) {
                if (assistant.addMovie(movie));
            } else {
                assistant.getClientMessage().add("Элемент не минимальный.");
            }
        } catch (CoordinateXException | NotUniqueIdException | ParseException |
                MovieGenreException | LessThanZeroException | ColorException e) {
            e.printStackTrace();
        }
    }
}

