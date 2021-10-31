package commands;

import exceptions.*;
import monitoring.ServerAssistant;
import stored.Movie;

import java.text.ParseException;

/**
 * Команда удаляет элемент коллекции по его id
 */
public class UpdateByIdCommand extends Command {
    public UpdateByIdCommand() {
        super("update_by_id", "обновить значение " +
                "элемента коллекции, id которого равен заданному");
    }

    @Override
    public void commit(ServerAssistant assistant, boolean visible) {
        int id = assistant.getReader().getConsoleScan().nextInt();


        try {
            Movie updMovie = assistant.getMovies().stream().filter(movie -> movie.getId() == id).findFirst().get();
            if (assistant.checkMovieMaster(updMovie)) {
                assistant.getReader().nextMovie(updMovie);
            } else {
                assistant.getClientMessage().add("Вы не являетесь владельцем данного объекта.\n");
            }

        } catch (CoordinateXException | MovieGenreException | LessThanZeroException |
                ColorException | NotUniqueIdException | ParseException e) {
            e.printStackTrace();
        }
    }
}
