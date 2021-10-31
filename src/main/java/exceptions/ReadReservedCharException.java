package exceptions;
/**
 * Исключение неразрешенного ввода зарезервированных символов CSV
 */
public class ReadReservedCharException extends Exception{
    public ReadReservedCharException() {
        super("Считаны зарезервированные символы");
    }
}
