package stored;

import monitoring.Control;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Location implements Serializable {
    private Long x; //Поле не может быть null
    private int y;
    private String name; //Строка не может быть пустой, Поле может быть null

    public Location() {
        this.x = 0L;
        this.y = 0;
        this.name = "UNKNOW";
    }

    public Location(Long x, int y, String name) {
        this.x = x;
        this.y = y;
        this.name = name;
    }

    @Override
    public String toString() {
        return (this.x + "," + this.y + "," + this.name);
    }

    public String toCSV() {
        return (Control.objToCSV(this.x) +
                Control.objToCSV(this.y) +
                Control.objToCSV(this.name));
    }
    public String toDb() {
        return (Control.objToDb(this.x) +
                Control.objToDb(this.y) +
                Control.objToDb(this.name));
    }

    public void setX(Long x) {
        this.x = x;
    }

    public void setY(Integer y) {
        this.y = y;
    }

    public void setName(String name) {
        if (!name.equals("")) {
            this.name = name;
        } else {
            this.name = "UNKNOW";
        }
    }
}