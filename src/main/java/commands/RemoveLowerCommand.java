package commands;

import monitoring.ServerAssistant;
import stored.Movie;

/**
 * Команда удаляет все элементы коллекции значение которых меньше введенного элемента
 */
public class RemoveLowerCommand extends Command {
    public RemoveLowerCommand() {
        super("remove_lower", "удалить из коллекции все элементы, " +
                "меньшие, чем заданный");
    }

    @Override
    public void commit(ServerAssistant assistant, boolean visible) {

        int id = assistant.getReader().getConsoleScan().nextInt();
        Movie curMovie = assistant.findMovie(id);
        assistant.getMovies().stream().filter(movie -> assistant.getMovieComparator().compare(movie, curMovie) < 0).
                filter(assistant::checkMovieMaster).
                forEach(assistant::deleteMovie);
        assistant.updateMovies();
    }
}

