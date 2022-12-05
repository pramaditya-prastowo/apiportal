package mii.bsi.apiportal.constant;

public class EndPoint {

    public static String[] permitAll = {
            "/api/v1.0/auth/authentication",
            "/api/key/readData",
            "/api/v1.0/user/**",
            "/oauth2/v1/authentication",
            "/api/v1.0/util/**",
            "/api/v1.0/serviceApi/**",
            "/api/v1.0/promo/**",
//            "/forget_password/**",
//            "/session",
//            "/session/user",
//            "/login",
//            "/testing"
    };
}
