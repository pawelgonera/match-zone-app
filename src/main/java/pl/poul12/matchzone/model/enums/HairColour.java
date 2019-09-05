package pl.poul12.matchzone.model.enums;

public enum HairColour {
    BLACK("Black"),
    DARK_BROWN("Dark Brown"),
    LIGHT_BROWN("Light Brown"),
    BROWN("Brown"),
    DARK_BLONDE("Dark Blonde"),
    LIGHT_BLONDE("Light Blonde"),
    BLONDE("Blonde"),
    GINGER_RED("Ginger Red"),
    GREY("Grey"),
    WHITE("White");

    private String name;

    HairColour(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

}
