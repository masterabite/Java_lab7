package io;

import java.io.Serializable;
import java.util.ArrayList;

public class Message implements Serializable {
    private ArrayList<String> message = new ArrayList<>();

    public Message() {
        message.add("");
    }

    public Message(int lines) {
        for (int i = 0; i < lines; ++i) {
            message.add("");
        }
    }

    public ArrayList<String> getMessage() {
        return message;
    }

    public String getAt(int index) {
        if (index < 0 || index >= message.size()) {
            System.out.println("Ошибка доступа сообщения.");
            return("");
        }
        return message.get(index);
    }

    public void add(String m) {
        String newM = message.get(message.size() - 1) + m;
        message.set(message.size()-1, newM);
    }

    public void add(String m, int index) {
        String newM = message.get(index) + m;
        message.set(index, newM);
    }

    public void addLine(String m) {
        message.add("");
    }

    public void clear() {
        this.message.clear();
    }

    public void print() {
        for (String m : message)
        System.out.println(m);
    }
}
