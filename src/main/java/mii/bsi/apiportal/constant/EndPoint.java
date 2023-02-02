package mii.bsi.apiportal.constant;

public class EndPoint {

    public static String[] permitAll = {
            "/api/v1.0/auth/authentication",
            "/api/key/readData",
//            "/api/v1.0/user/**",
            "/api/v1.0/user/verification",
            "/api/v1.0/faq",
            "/oauth2/v1/authentication",
            "/api/v1.0/util/**",
            "/api/v1.0/serviceApi",
            "/api/v1.0/need-help",
            "/api/v1.0/promo/**",
            "/api/v1.0/forget_password",
            "/swagger/**",
            "/api/files/service-api/**",
            "/api/files/promo/**",
            "/api/files/apps/**",
            "/api/v1.0/serviceApi/groups"
//            "/forget_password/**",
//            "/session",
//            "/session/user",
//            "/login",
//            "/testing"
    };
}
