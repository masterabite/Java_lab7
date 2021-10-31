package stored;

import enums.MovieGenre;
import exceptions.LessThanZeroException;
import monitoring.Control;
import mynet.User;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Date;

public class Movie implements Serializable {
    private static int staticID = 1; //статичное поле определяющее уникальный id
    private int id; //Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически
    private String name; //Поле не может быть null, Строка не может быть пустой
    private Coordinates coordinates; //Поле не может быть null
    private Date creationDate; //Поле не может быть null, Значение этого поля должно генерироваться автоматически
    private Integer oscarsCount; //Значение поля должно быть больше 0, Поле не может быть null
    private int totalBoxOffice; //Значение поля должно быть больше 0
    private int usaBoxOffice; //Значение поля должно быть больше 0
    private MovieGenre genre; //Поле не может быть null
    private Person operator; //Поле не может быть null
    private String master;//владелец

    static private int getID() { // статичный метод для получения уникального ID
        return staticID++;
    }

    public Movie() {
        this.id = Movie.getID();
        this.name = "UNKNOW";
        this.coordinates = new Coordinates();
        this.creationDate = new Date();
        this.oscarsCount = 0;
        this.totalBoxOffice = 0;
        this.usaBoxOffice = 0;
        this.genre = MovieGenre.COMEDY;
        this.operator = new Person();
    }

    public Movie(User user) {
        this.id = Movie.getID();
        this.name = "UNKNOW";
        this.coordinates = new Coordinates();
        this.creationDate = new Date();
        this.oscarsCount = 0;
        this.totalBoxOffice = 0;
        this.usaBoxOffice = 0;
        this.genre = MovieGenre.COMEDY;
        this.operator = new Person();
        this.master = user.getLogin();
    }

    public Movie(
            String name,
            Coordinates coordinates,
            Integer oscarsCount,
            Integer totalBoxOffice,
            Integer usaBoxOffice,
            MovieGenre genre,
            Person operator
    ) {
        this.id = Movie.getID();
        this.name = name;
        this.coordinates = coordinates;
        this.creationDate = new Date();
        this.oscarsCount = oscarsCount;
        this.totalBoxOffice = totalBoxOffice;
        this.usaBoxOffice = usaBoxOffice;
        this.genre = genre;
        this.operator = operator;
    }

    /**
     * Переопределенный метод hashCode возвращает hascode
     */
    @Override
    public int hashCode() {
        return this.name.hashCode();
    }

    /**
     * @return объект в строков представлении в формате CSV
     */
    public String toCSV() {
        return Control.makeCSVLine(
                Control.objToCSV(this.id) +
                Control.objToCSV(this.name) +
                this.coordinates.toCSV() +
                Control.DateToCSV(this.creationDate) +
                Control.objToCSV(this.oscarsCount) +
                Control.objToCSV(this.totalBoxOffice) +
                Control.objToCSV(this.usaBoxOffice) +
                Control.objToCSV(this.genre) +
                this.operator.toCSV() + Control.objToCSV(this.master)
        );
    }

    /**
     * @return объект в строков представлении в формате Db
     */
    public String toDb() {
        return Control.makeDbLine(
                Control.objToDb(this.id) +
                        Control.objToDb(this.name) +
                        this.coordinates.toDb() +
                        Control.DateToDb(this.creationDate) +
                        Control.objToDb(this.oscarsCount) +
                        Control.objToDb(this.totalBoxOffice) +
                        Control.objToDb(this.usaBoxOffice) +
                        Control.objToDb(this.genre) +
                        this.operator.toDb() +
                        Control.objToDb(this.master)
        );
    }


    public int getId() {
        return this.id;
    }

    public static void setStaticID(int staticID) {
        Movie.staticID = staticID;
    }

    /**
     * @return уникальный id
     */
    public static int updateStaticID() {
        return ++Movie.staticID;
    }

    public String getName() {
        return name;
    }

    public static int getStaticID() {
        return Movie.staticID;
    }

    public int getTotalBoxOffice() {
        return totalBoxOffice;
    }

    public void setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public void setGenre(MovieGenre genre) {
        this.genre = genre;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setMaster(String master) {
        this.master = master;
    }

    public String   getMaster() {
        return master;
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

    public void setOperator(Person operator) {
        this.operator = operator;
    }


    /**
     * функция инициализирует поле oscarsCount с учетом того что оно должно быть положительным
     * @param oscarsCount значение, которое мы хотим присвоить oscarCount
     * @throws LessThanZeroException если значение не является положительным числом
     */
    public void setOscarsCount(Integer oscarsCount) throws LessThanZeroException {
        if (oscarsCount <= 0) {
            throw new LessThanZeroException();
        }
        this.oscarsCount = oscarsCount;
    }

    /**
     * функция инициализирует поле totalBoxOffice с учетом того что оно должно быть положительным
     * @param totalBoxOffice значение, которое мы хотим присвоить totalBoxOffice
     * @throws LessThanZeroException если значение не является положительным числом
     */
    public void setTotalBoxOffice(Integer totalBoxOffice) throws LessThanZeroException {
        if (totalBoxOffice <= 0) {
            throw new LessThanZeroException();
        }
        this.totalBoxOffice = totalBoxOffice;
    }

    /**
     * функция инициализирует поле usaBoxOffice с учетом того что оно должно быть положительным
     * @param usaBoxOffice значение, которое мы хотим присвоить usaBoxOffice
     * @throws LessThanZeroException если значение не является положительным числом
     */
    public void setUsaBoxOffice(Integer usaBoxOffice) throws LessThanZeroException{
        if (usaBoxOffice <= 0) {
            throw new LessThanZeroException();
        }
        this.usaBoxOffice = usaBoxOffice;
    }
}
