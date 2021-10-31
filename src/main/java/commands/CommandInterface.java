package commands;

import monitoring.ServerAssistant;

/**
 * Интерфейс команд
 */
public interface CommandInterface {

    /**
     * выполнение команды
     * @param assistant наш ассистент
     * @param visible производится ли считывание из скрипта
     */
    void commit(ServerAssistant assistant, boolean visible);
}
