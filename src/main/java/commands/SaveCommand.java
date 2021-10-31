package commands;

import monitoring.Control;
import monitoring.ServerAssistant;
import stored.Movie;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Команда сохраняет коллекцию в файл
 */
public class SaveCommand extends Command {

    public SaveCommand() {
        super("save", "сохранить коллекцию в файл");
    }
    @Override
    public void commit(ServerAssistant assistant, boolean visible) {
        try {
            System.out.println("Сохранение коллекции...");
            assistant.setBw(new BufferedWriter(new FileWriter(assistant.getMyFile())));
            assistant.getBw().write(Control.getMovieHead());
            for (Movie i : assistant.getMovies()) {
                assistant.getBw().write(i.toCSV());
            }
            assistant.getBw().flush();
            assistant.getBw().close();
            System.out.println("Коллекция успешно сохранена.");
        } catch (IOException | NullPointerException e) {
            assistant.getClientMessage().add("Не удалось сохранить коллекцию.\n");
        }
    }
}
