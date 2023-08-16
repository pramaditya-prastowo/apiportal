package mii.bsi.apiportal.constant;

public enum UserAction {
    UNLOCK("Unlock User"),
    LOCK("Lock User"),
    ACTIVATE("Activate User"),
    INACTIVATE("Inactivate User");

    private final String text;

    UserAction(String text){
        this.text = text;
    }

    public String toString() {
        return this.text;
    }
}
