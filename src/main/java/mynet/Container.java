package mynet;

import commands.Command;
import commands.HelpCommand;
import io.Message;

import java.io.Serializable;

public class Container implements Serializable {
    private Command command;
    private Message message;
    private User user;
    private int type;

    public Container(User user, int type) {
        this.user = user;
        this.command = new HelpCommand();
        this.type = type;
    }

    public Container(Command command, Message message, User user) {
        this.command = command;
        this.message = message;
        this.user = user;
        this.type = 0;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Command getCommand() {
        return command;
    }

    public Message getMessage() {
        return message;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    public void setCommand(Command command) {
        this.command = command;
    }
}
