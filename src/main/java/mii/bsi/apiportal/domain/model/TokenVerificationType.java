package mii.bsi.apiportal.domain.model;

public enum TokenVerificationType {
    FORGET_PASSWORD("Forget Password"),
    EMAIL_VERIFICATION("Email Verification");

    private final String text;

    TokenVerificationType(String text){this.text = text;}

    @Override
    public String toString() {
        return this.text;
    }
}