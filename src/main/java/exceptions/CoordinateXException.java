package exceptions;
/**
 * Исключение для поля x у класса Movie
 */
public class CoordinateXException extends Exception{
    public CoordinateXException() {
        super("Координата x не может быть меньше 961!");
    }
}
