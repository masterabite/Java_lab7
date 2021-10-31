package commands;

import monitoring.ServerAssistant;

/**
 * Команда очищает коллекцию
 */
public class ClearCommand extends Command {
    public ClearCommand() {
        super("clear", "очистить коллекцию");
    }

    @Override
    public void commit(ServerAssistant assistant, boolean visible) {
        assistant.getMovies().stream().filter(assistant::checkMovieMaster).
                forEach(assistant::deleteMovie);
        assistant.updateMovies();
    }
}

