package enums;

public enum Color {
    GREEN ("GREEN"),
    RED ("RED"),
    BLACK("BLACK"),
    YELLOW("YELLOW");

    private String title;

    Color(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return this.title;
    }

}