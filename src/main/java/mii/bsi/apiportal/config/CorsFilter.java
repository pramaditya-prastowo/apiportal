package mii.bsi.apiportal.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.web.util.matcher.IpAddressMatcher;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class CorsFilter extends OncePerRequestFilter {

    @Value("${header.cors}")
    private String CORS;
    private String[] allowHeaders ={
            "authorization",
            "content-type",
            "xsrf-token",
            "Cache-Control",
            "X-Path",
            "signature",
            "access_token",
            "endpoint_url",
            "Private_Key",
            "X-TIMESTAMP",
            "X-CLIENT-KEY",
            "X-CLIENT-SECRET",
            "EndpointUrl",
            "AccesToken",
            "X-SIGNATURE",
            "X-PARTNER-ID",
            "X-EXTERNAL-ID",
            "CHANNEL-ID"
    };

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        response.setHeader("Access-Control-Allow-Origin", CORS);
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, PATCH, OPTIONS");
        response.setHeader("Access-Control-Allow-Headers", String.join(",",allowHeaders));
//        response.setHeader("Access-Control-Max-Age", "300");
        response.setHeader("Access-Control-Expose-Headers", "xsrf-token");
        if("OPTIONS".equals(request.getMethod())){
            response.setStatus(HttpServletResponse.SC_OK);
        } else {
            filterChain.doFilter(request, response);
        }
//        Set<String> allowedIpAddresses = new HashSet<>(Arrays.asList("10.0.116.127", "192.168.1.5")); // ganti dengan daftar IP address yang diizinkan

//        String ipAddress = request.getRemoteAddr();
//        System.out.println(ipAddress);
//
//        if (matches(request, "10.0.116.127")) { // ganti dengan IP address yang diizinkan
//            response.setHeader("Access-Control-Allow-Origin", "*");
//            response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, PATCH");
//            response.setHeader("Access-Control-Allow-Headers", "authorization, content-type, " + "xsrf-token, Cache-Control");
//            response.setHeader("Access-Control-Expose-Headers", "xsrf-token");
//            if("OPTIONS".equals(request.getMethod())){
//                response.setStatus(HttpServletResponse.SC_OK);
//            } else {
//                filterChain.doFilter(request, response);
//            }
//        } else {
//            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Access denied");
//        }
    }

    private boolean matches(HttpServletRequest request, String subnet) {
        IpAddressMatcher     ipAddressMatcher = new IpAddressMatcher(subnet);
        return ipAddressMatcher.matches(request);
    }
}
