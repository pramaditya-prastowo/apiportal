package mii.bsi.apiportal.domain.model;

public enum Roles {

    SUPER_ADMIN("SUPER_ADMIN"),
    MITRA("MITRA");

    private final String text;

    Roles(String text){this.text = text;}

    @Override
    public String toString() {
        return this.text;
    }
}
