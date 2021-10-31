package commands;

import exceptions.*;
import monitoring.ServerAssistant;
import mynet.DB;
import stored.Movie;

import java.sql.SQLException;
import java.text.ParseException;

/**
 * Команда добавляет объект в коллекцию из консоли/скрипта
 */
public class AddCommand extends Command {
    public AddCommand() {
        super("add", "добавить новый элемент в коллекцию");
    }

    @Override
    public void commit(ServerAssistant assistant, boolean visible) {
        try {
            Movie movie = assistant.getReader().nextMovie(new Movie());
            assistant.addMovie(movie);
        } catch (CoordinateXException | MovieGenreException |
                LessThanZeroException | ColorException |
                NotUniqueIdException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}

