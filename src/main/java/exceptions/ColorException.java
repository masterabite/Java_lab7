package exceptions;
/**
 * Исключение ввода несуществующего цвета класса Movie
 */
public class ColorException extends Exception{
    public ColorException() {
        super("Не удалось считать Color");
    }
}
