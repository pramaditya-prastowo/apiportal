package mii.bsi.apiportal.constant;

public enum NofiticationType {
    INFO("INFO"), SUCCESS("SUCCESS"), FAILED("FAILED"), ERROR("ERROR"), WAITING("WAITING");

    private final String text;

    NofiticationType(String text) {
        this.text = text;
    }

    public String toString() {
        return this.text;
    }
}
