package pl.poul12.matchzone.model.enums;

public enum Physique {

    SKINNY("Skinny"),
    SLIM("Slim"),
    NORMAL("Normal"),
    SHAPELY("Shapely"),
    MUSCULAR("Muscular"),
    THICK("Thick"),
    FAT("Fat");

    private String name;

    Physique(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
