package exceptions;
/**
 * Исключение для недопустимого ID класса Movie
 */
public class NotUniqueIdException extends Exception{
    public NotUniqueIdException(String mes) {
        super(mes);
    }
}
