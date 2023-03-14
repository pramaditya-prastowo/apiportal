package mii.bsi.apiportal.domain.model;

public enum TokenVerificationType {
    FORGET_PASSWORD("Forget Password"),
    EMAIL_VERIFICATION("Email Verification"),
    OTP_EMAIL_VERIFICATION("OTP Email Verification");

    private final String text;

    TokenVerificationType(String text){this.text = text;}

    @Override
    public String toString() {
        return this.text;
    }
}