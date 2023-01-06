package mii.bsi.apiportal.controller;

import mii.bsi.apiportal.dto.CountDashboard;
import mii.bsi.apiportal.service.DashboardService;
import mii.bsi.apiportal.utils.ResponseHandling;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1.0/dashboard")
public class DashboardController {

    @Autowired
    private DashboardService dashboardService;

    @GetMapping
    public ResponseEntity<ResponseHandling<List<CountDashboard>>> getDashboardCount(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String token){
        return dashboardService.getCountDashboard(token.substring(7));
    }
}
