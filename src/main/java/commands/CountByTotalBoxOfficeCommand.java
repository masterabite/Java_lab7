package commands;

import monitoring.ServerAssistant;

/**
 * Команда вывести количество элементов,
 * значение поля totalBoxOffice которых равно заданному
 */
public class CountByTotalBoxOfficeCommand extends Command {

    public CountByTotalBoxOfficeCommand() {
        super("count_by_total_box_office", "вывести количество элементов, " +
                "значение поля totalBoxOffice которых равно заданному");
    }

    @Override
    public void commit(ServerAssistant assistant, boolean visible) {
        Integer totalBoxOffice = assistant.getReader().getConsoleScan().nextInt();
        long cnt = assistant.getMovies().stream().filter(movie -> movie.getTotalBoxOffice() == totalBoxOffice).count();
        assistant.getClientMessage().add("Кол-во соответствующих элементов: " + cnt + "\n");
    }
}

