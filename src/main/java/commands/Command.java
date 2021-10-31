package commands;

import monitoring.ServerAssistant;

import java.io.Serializable;

/**
 * базовый класс для команд
 */
public class Command implements CommandInterface, Serializable {
    private String args;
    private String name;
    private String help;

    Command(String name, String help) {
        this.args = "";
        this.name = name;
        this.help = help;
    }

    public String getName() {
        return name;
    }



    public String getHelp() {
        return this.name + ": " + this.help;
    }

    @Override
    public void commit(ServerAssistant assistant, boolean visible) {
        System.out.println("commit...");
    }
}
