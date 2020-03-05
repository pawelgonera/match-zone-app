package pl.poul12.matchzone.model.enums;

public enum Eyes {
    BLUE("Blue"),
    BROWN("Brown"),
    GREEN("Green"),
    HAZEL("Hazel"),
    GREY("Grey"),
    AMBER("Amber");

    private String name;

    Eyes(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
