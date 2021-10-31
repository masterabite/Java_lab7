package commands;

import monitoring.ServerAssistant;
import stored.Movie;

/**
 * Команда удаляет элемент коллекции по его id
 */
public class RemoveByIdCommand extends Command {
    public RemoveByIdCommand() {
        super("remove_by_id", "удалить элемент из коллекции по его id");
    }

    @Override
    public void commit(ServerAssistant assistant, boolean visible) {
        int id = assistant.getReader().getConsoleScan().nextInt();
        Movie desMovie = assistant.getMovies().stream().filter(movie -> movie.getId() == id).findFirst().get();
        if (assistant.checkMovieMaster(desMovie)) {
            assistant.deleteMovie(desMovie);
            assistant.updateMovies();
        } else {
            assistant.getClientMessage().add("Вы не являетесь владельцем данного объекта.");
        }
    }
}
