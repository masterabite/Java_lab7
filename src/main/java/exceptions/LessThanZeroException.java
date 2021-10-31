package exceptions;
/**
 * Исключение для чисел, которые должны быть положительными
 */
public class LessThanZeroException extends Exception{
    public LessThanZeroException() {
        super("Число должно быть больше 0");
    }
}
