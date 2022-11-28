package mii.bsi.apiportal.constant;

public enum StatusCode {
    OK("OK", "200"),

    CREATED("CREATED", "201"),
    BAD_REQUEST("BAD REQUEST", "400"),
    UNAUTHORIZED("UNAUTHORIZED", "401"),
    FORBIDDEN("FORBIDDEN","403"),
    NOT_FOUND("NOT FOUND","404"),
    CONFLICT("CONFLICT", "409"),
    GONE("GONE", "410"),
    INTERNAL_SERVER_ERROR("INTERNAL SERVER ERROR", "500");


    private final String text;
    private final String code;

    StatusCode(String text, String code) {
        this.text = text;
        this.code = code;
    }

    public String toString() {
        return this.text;
    }
    public String getCode(){
        return this.code;
    }
}

