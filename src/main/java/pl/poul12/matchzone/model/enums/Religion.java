package pl.poul12.matchzone.model.enums;

public enum Religion {

    CHRISTIANITY("Christianity"),
    ISLAM("Islam"),
    HINDUISM("Hinduism"),
    BUDDHISM("Buddhism"),
    JUDAISM("Judaism"),
    ATHEIST("Atheist");

    private String name;

    Religion(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
