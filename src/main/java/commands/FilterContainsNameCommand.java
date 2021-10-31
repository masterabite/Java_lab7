package commands;

import monitoring.ServerAssistant;

/**
 * Команда выводит элементы, значение поля name которых содержит заданную подстроку
 */
public class FilterContainsNameCommand extends Command {

    public FilterContainsNameCommand() {
        super("filter_contains_name", "вывести элементы, значение поля name которых содержит заданную подстроку");
    }

    @Override
    public void commit(ServerAssistant assistant, boolean visible) {
        String name = assistant.getReader().readLine("Введите подстроку: ", visible);

        assistant.getMovies().stream().filter(movie -> movie.getName().contains(name)).
                forEach(
                    movie -> assistant.getClientMessage().add(movie.toCSV())
                )
        ;

    }
}

