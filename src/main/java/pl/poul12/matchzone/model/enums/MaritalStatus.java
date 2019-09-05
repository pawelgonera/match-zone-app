package pl.poul12.matchzone.model.enums;

public enum MaritalStatus {

    MARRIED("Married"),
    WIDOWED("Widowed"),
    SEPARATED("Separated"),
    DIVORCED("Divorced"),
    SINGLE("Single");

    private String name;

    MaritalStatus(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
