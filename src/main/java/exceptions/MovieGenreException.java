package exceptions;
/**
 * Исключение ввода несуществующего жанра класса Movie
 */
public class MovieGenreException extends Exception{
    public MovieGenreException() {
        super("Не удалось считать MovieGenre");
    }
}
