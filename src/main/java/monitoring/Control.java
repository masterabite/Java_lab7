package monitoring;

import java.io.*;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

/**
 * класс для работы с форматом CSV
 */
public class Control {

    /**
     * Формат ввода/вывода для поля birthday класса Movie
     */
    private static SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

    /**
     * зарезервированные символы формата CSV
     */
    private static String reservedChars = ",\n";

    public static SimpleDateFormat getDateFormatter() {
        return dateFormat;
    }

    /**
     * Функция проверяет что строка string содержит зарезервированные символы
     * @param string проверяемая строка
     * @return содержит ли строка зарезервированные символы
     */
    public static boolean isReserved(String string) {
        for (int i = 0; i < reservedChars.length(); ++i) {
            if (string.indexOf(reservedChars.charAt(i)) != -1) {
                return true;
            }
        }
        return false;
    }

    /**
     * @param c проверяемый символ
     * @return является ли символ зарезервированным
     */
    public static boolean isReserved(char c) {
        return reservedChars.indexOf(c) != -1;
    }

    /**
     * Функция обрамляет строку string, если в ней содержаться зарезервированные символы
     * @param string обрабатываемая строка
     * @return обработанная строка
     */
    public static String processed(String string) {
        if (isReserved(string)) {
            return "\"" + string + "\"";
        } else {
            return string;
        }
    }

    /**
     * Объект класса localDateTime приводиться к строке соответствующей формату ввода/вывода CSV
     * @param date дата, приводимая к формату CSV
     * @return сторку формата CSV
     */
    public static String DateToCSV(Date date) {
        if (date == null) {
            return "UNKNOW,";
        } else {
            return dateFormat.format(date)+',';
        }
    }

    /**
     * функция приводит объект к строке, если объект null то он приводится к строке "UNKNOW"
     * @param obj объект, приводимый к строке
     * @return соответствующая строка
     */
    public static String objToString(Object obj) {
        if (obj == null) {
            return "UNKNOW";
        } else {
            return obj.toString();
        }
    }

    /**
     * Объект приводиться к строке соответствующей формату CSV
     * @param obj объект, приводимый к формату CSV
     * @return сторку формата CSV
     */
    public static String objToCSV(Object obj) {

        return processed(objToString(obj)) + ',';
    }

    /**
     * Функция обрабатывает строку в конец строки таблицы CSV формата
     * @param string строка приводимая к кону строки таблицы
     * @return строка соответствующая конце строки CSV таблицы
     */
    public static String makeCSVLine(String string) {
        String result = string.substring(0, string.length()-1);
        result += '\n';
        return result;
    }

    /**
     * Объект приводиться к строке соответствующей формату DB
     * @param obj объект, приводимый к формату DB
     * @return сторку формата DB
     */
    public static String objToDb(Object obj) {

        return "'" + objToString(obj) + "', ";
    }

    /**
     * Объект класса DateTime приводиться к строке соответствующей формату ввода/вывода DB
     * @param date дата, приводимая к формату DB
     * @return сторку формата DB
     */
    public static String DateToDb(Date date) {
        if (date == null) {
            return "'UNKNOW', ";
        } else {
            return "'" + dateFormat.format(date)+"', ";
        }
    }

    /**
     * Функция обрабатывает строку в конец строки таблицы DB формата
     * @param string строка приводимая к кону строки таблицы
     * @return строка соответствующая конце строки DB таблицы
     */
    public static String makeDbLine(String string) {
        return string.substring(0, string.length()-2);
    }

    public static String getMovieHead() {
        return "(Id, MovieName, CoordinateX, CoordinateY, " +
                "CreationDate, OscarCount, TotalBoxOffice, " +
                "USABoxOffice, Genre, OperatorName, OperatorBirthDay, OperatorHairColor, " +
                "OperatorLocationX, OperatorLocationY, OperatorLocationName, Master)";
    }

    /**
     * Функция считывает строку из таблицы в CSV формате, и переводит в массив строк содержащий элементы строки
     * @param string начальная строка
     * @param scan сканнер для дополнительного считывания (на случай, если элемент таблицы содержит перевод строки)
     * @return массив строк, содержащий элементы строки таблицы в строков представлении
     */
    public static ArrayList<String> parseLineFromCSV(String string, Scanner scan) {
        ArrayList<String> result = new ArrayList<>();
        boolean priority = false;
        StringBuilder currentString = new StringBuilder();
        StringBuilder stringBuilder = new StringBuilder(string);
        for (int i = 0; i < stringBuilder.length(); ++i) {
            char currentChar = stringBuilder.charAt(i);
            if (currentChar == '\"') {
                priority = !priority;
            } else if (priority || !isReserved(currentChar)) {
                currentString.append(currentChar);
                if (priority && i + 1 == stringBuilder.length()) {
                    stringBuilder.append("\n").append(scan.nextLine());
                }
            } else {
                result.add(currentString.toString());
                currentString = new StringBuilder();
            }
        }
        if (!currentString.toString().equals("")) {
            result.add(currentString.toString());
        }
        return result;
    }

    public static byte[] serialize(Object object) throws IOException {
        final ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
        try (final ObjectOutputStream out = new ObjectOutputStream(byteOut)){
            out.writeObject(object);
        }
        return byteOut.toByteArray();
    }

    public static Object deserialize(byte[] buffer) throws IOException, ClassNotFoundException {

        try (final ObjectInputStream out = new ObjectInputStream(new ByteArrayInputStream(buffer))){
            return out.readObject();
        }
    }
}
