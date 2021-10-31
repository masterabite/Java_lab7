package stored;

import enums.Color;
import monitoring.Control;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

public class Person implements Serializable {
    private String name; //Поле не может быть null, Строка не может быть пустой
    private Date birthday; //Поле может быть null
    private Color hairColor; //Поле может быть null
    private Location location; //Поле может быть null

    public Person() {
        this.name = "UNKNOW";
        this.hairColor = Color.BLACK;
        this.location = new Location();
    }

    public Person(String name, Date birthday, Color hairColor, Location location) {
        this.name = name;
        this.birthday = birthday;
        this.hairColor = hairColor;
        this.location = location;
        this.hairColor = hairColor;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public void setHairColor(Color hairColor) {
        this.hairColor = hairColor;
    }

    /**
     * функция инициализирует поле name с учетом того что оно не может быть пустой строкой
     * @param name обрабатываемое имя
     */
    public void setName(String name) {
        if (!name.equals("")) {
            this.name = name;
        } else {
            this.name = "UNKNOW";
        }
    }

    /**
     * @return объект в строков представлении в формате CSV
     */
    public String toCSV() {
        return (
                Control.objToCSV(this.name) +
                Control.DateToCSV(this.birthday) +
                Control.objToCSV(this.hairColor) +
                this.location.toCSV());
    }

    /**
     * @return объект в строков представлении в формате Db
     */
    public String toDb() {
        return (
                Control.objToDb(this.name) +
                        Control.DateToDb(this.birthday) +
                        Control.objToDb(this.hairColor) +
                        this.location.toDb());
    }

    public void setLocation(Location location) {
        this.location = location;
    }
}