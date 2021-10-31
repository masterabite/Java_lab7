package mynet;

//import com.jcraft.jsch.JSch;
//import com.jcraft.jsch.Session;
import enums.Color;
import enums.MovieGenre;
import exceptions.ColorException;
import exceptions.CoordinateXException;
import exceptions.MovieGenreException;
import io.Message;
import monitoring.Control;
import stored.Coordinates;
import stored.Location;
import stored.Movie;
import stored.Person;

import java.io.PrintWriter;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.HashSet;

public class DB {
    private static String dbUrl = "jdbc:postgresql://pg:5432/studs";//"jdbc:postgresql://pg:5432/studs";//"jdbc:postgresql://localhost:1488/studs";
    private static String dbLogin = "s268905";
    private static String dbPassword = "ncq887";
    private static Connection connection;
    private Statement statement;
    private ResultSet resultSet;

    public DB() throws SQLException {

        DriverManager.setLogWriter(new PrintWriter(System.out));
        connection = DriverManager.getConnection(dbUrl, dbLogin, dbPassword);
        statement = connection.createStatement();

/*
        try{
            JSch jsch = new JSch();
            Session session = jsch.getSession(dbLogin, "se.ifmo.ru", 2222);
            session.setPassword(dbPassword);
            session.setConfig("StrictHostKeyChecking", "no");
            session.connect();
            session.setPortForwardingL(1488, "pg", 5432);
        }
        catch (Exception e){
            e.printStackTrace();
        }
*/

        printUsers();
        printMovies();

        /*
        statement.executeQuery("CREATE TABLE users(" +
                "login varchar(255)," +
                "password varchar(255)" +
                ");");

        statement.executeQuery("CREATE TABLE movies(" +
                "Id int," +
                "MovieName varchar(255)," +
                "CoordinateX float," +
                "CoordinateY float," +
                "CreationDate date," +
                "OscarCount int," +
                "TotalBoxOffice int," +
                "USABoxOffice int," +
                "Genre varchar(255)," +
                "OperatorName varchar(255)," +
                "OperatorBirthDay date," +
                "OperatorHairColor varchar(255)," +
                "OperatorLocationX int," +
                "OperatorLocationY int," +
                "OperatorLocationName varchar(255)," +
                "Master varchar(255)" +
                ");");
                */
    }

    void printUsers() {
        System.out.println("db users:");
        try {
            resultSet = statement.executeQuery("SELECT * from users;");
            while (resultSet.next()) {
                System.out.println(resultSet.getString(1) + ",    " + resultSet.getString(2));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    void printMovies() {
        System.out.println("db movies:");
        try {
            resultSet = statement.executeQuery("SELECT * from movies;");
            while (resultSet.next()) {
                for (int i = 1; i <= 15; ++i) {
                    System.out.print(resultSet.getString(i) + ", ");
                }
                System.out.println(resultSet.getString(16));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public HashSet<Movie> getElements() {
        HashSet<Movie> movies = new HashSet<>();
        try {
            ResultSet resultSet = statement.executeQuery("Select * from movies");
            MovieGenre movieGenre = null;
            Color color;


            while(resultSet.next()) {
                /*for (int i = 1; i <=  15; ++i)
                System.out.println(resultSet.getString(i) + " :" + i);*/



                switch (resultSet.getString(9)) {
                    case "DRAMA":
                        movieGenre = MovieGenre.DRAMA;
                        break;
                    case "TRAGEDY":
                        movieGenre = MovieGenre.TRAGEDY;
                        break;
                    case "COMEDY":
                        movieGenre = MovieGenre.COMEDY;
                        break;
                    default:
                        throw new MovieGenreException();
                }

                switch (resultSet.getString(12)) {
                    case "BLACK":
                        color = Color.BLACK;
                        break;
                    case "GREEN":
                        color = Color.GREEN;
                        break;
                    case "RED":
                        color = Color.RED;
                        break;
                    case "YELLOW":
                        color = Color.YELLOW;
                        break;
                    default:
                        throw new ColorException();
                }

                Movie movie = new Movie(
                        resultSet.getString(2),
                        new Coordinates(
                                resultSet.getFloat(3),
                                resultSet.getDouble(4)
                        ),
                        resultSet.getInt(6),
                        resultSet.getInt(7),
                        resultSet.getInt(8),
                        movieGenre,
                        new Person(
                                resultSet.getString(10),
                                resultSet.getDate(11),
                                color,
                                new Location(
                                        resultSet.getLong(13),
                                        resultSet.getInt(14),
                                        resultSet.getString(15)
                                )
                        )
                );
                movie.setMaster(resultSet.getString(16));
                movie.setCreationDate(resultSet.getDate(5));
                movie.setId(resultSet.getInt(1));
                movies.add(movie);
            }
        } catch (SQLException | CoordinateXException | MovieGenreException | ColorException e) {
            e.printStackTrace();
        }
        return movies;
    }

    public boolean inDb(User user) {
        try (Statement statement = connection.createStatement()) {

            final String sql = "select * from users where login = '" + user.getLogin() + "';";

            ResultSet resultSet = statement.executeQuery(sql);

            boolean check = false;
            while (resultSet.next()) {
                check = true;
            }
            return check;

        }
        catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteElement(Movie movie) {
        try {
            String sql = "delete from movies where Id = " + movie.getId() + ";";
            return statement.executeUpdate(sql) == 1;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean addElement(Movie movie) {

        try (Statement statement = connection.createStatement()) {

            String sql = "select nextval ('ID_seq');";

            ResultSet rs = statement.executeQuery(sql);

            rs.next();
            int id = rs.getInt(1);

            movie.setId(id);
            sql = "insert into movies " + Control.getMovieHead() + " VALUES (" + movie.toDb() + ");";
            System.out.println(movie.toCSV());
            if (statement.executeUpdate(sql) == 1)
                return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void registerUser(User user, Message message) {
        if (inDb(user)) {
            message.add("Данный пользователь уже зарегестрирован.");
        } else {
            try {
                String sql = "insert into users (login, password)" +
                        "VALUES ('" + user.getLogin() + "', '" + toHashMD2(user.getPassword()) + "');";
                statement.executeUpdate(sql);
                message.add("Ok");
            } catch (SQLException | NoSuchAlgorithmException e) {
                message.add("Не удалось зарегистрировать пользователя.");
                e.printStackTrace();
            }
        }
    }

    public void loginUser(User user, Message message) {
        if (!inDb(user)) {
            message.add("Данный пользователь не зарегестрирован.");
        } else {
            try {
                String sql = "SELECT  * from users where login = '" + user.getLogin() + "';";
                ResultSet resultSet = statement.executeQuery(sql);
                if (resultSet.next()) {
                    if (!toHashMD2(user.getPassword()).equals(resultSet.getString(2))) {
                        message.add("Указан неверный пароль.");
                    } else {
                        message.add("Ok");
                    }
                }
            } catch (SQLException | NoSuchAlgorithmException e) {
                message.add("Не удалось авторизовать пользователя.");
                e.printStackTrace();
            }
        }
    }

    private String toHashMD2(String password) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD2");

        byte[] messageDigest = md.digest(password.getBytes());

        BigInteger no = new BigInteger(1, messageDigest);

        String hashText = no.toString(16);

        while (hashText.length() < 32) {
            hashText = "0" + hashText;
        }
        return hashText;
    }

}
