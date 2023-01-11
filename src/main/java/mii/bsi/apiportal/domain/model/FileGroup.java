package mii.bsi.apiportal.domain.model;

public enum FileGroup {
    SERVICE_API("Service API"),

    MITRA("MITRA"),
    MY_APPLICATION("My Application");

    private final String text;

    FileGroup(String text){this.text = text;}

    @Override
    public String toString() {
        return this.text;
    }
}
