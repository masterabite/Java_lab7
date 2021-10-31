package enums;

public enum MovieGenre {
    DRAMA("DRAMA"),
    COMEDY("COMEDY"),
    TRAGEDY("TRAGEDY");

    private String title;

    MovieGenre(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return this.title;
    }

}