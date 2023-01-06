package mii.bsi.apiportal.service;

import io.jsonwebtoken.Claims;
import mii.bsi.apiportal.constant.StatusCode;
import mii.bsi.apiportal.domain.model.Roles;
import mii.bsi.apiportal.dto.CountDashboard;
import mii.bsi.apiportal.repository.ServiceApiRepository;
import mii.bsi.apiportal.repository.UserRepository;
import mii.bsi.apiportal.utils.JwtUtility;
import mii.bsi.apiportal.utils.RequestData;
import mii.bsi.apiportal.utils.ResponseHandling;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DashboardService {

    @Autowired
    private JwtUtility jwtUtility;

    @Autowired
    private LogService logService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ServiceApiRepository serviceApiRepository;

    public static final String COUNT_DASHBOARD = "Count Dashboard";

    public ResponseEntity<ResponseHandling<List<CountDashboard>>> getCountDashboard(String token){
        ResponseHandling<List<CountDashboard>> responseData= new ResponseHandling<>();
        List<CountDashboard> listCount = new ArrayList<>();
        try {
            final Claims claim = jwtUtility.getAllClaimsFromToken(token);
            claim.get("role");
            if(!(claim.get("role").equals(Roles.SUPER_ADMIN.toString()) || claim.get("role").equals(Roles.ADMIN.toString()))){
                responseData.failed("Access denied");
                logService.saveLog(new RequestData<>(), responseData, StatusCode.FORBIDDEN, this.getClass().getName(),
                        COUNT_DASHBOARD);
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(responseData);
            }
            Long countUser = userRepository.countUserByAuthPrincipal(Roles.MITRA.toString());
            Long countService = serviceApiRepository.countServiceApi();
            listCount.add(new CountDashboard("Total Service API", countService.intValue(),"setting/service-api"));
            listCount.add(new CountDashboard("Total Request API Hari ini", 0,""));
            listCount.add(new CountDashboard("Total Mitra Terdaftar", 0,""));
            listCount.add(new CountDashboard("Total Pengguna", countUser.intValue(),"dashboard-admin"));
            responseData.setPayload(listCount);
            responseData.success();


        }catch (Exception e){
            e.printStackTrace();
            responseData.failed(e.getMessage());
            logService.saveLog(new RequestData<>(), responseData, StatusCode.INTERNAL_SERVER_ERROR, this.getClass().getName(),
                    COUNT_DASHBOARD);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseData);
        }
        logService.saveLog(new RequestData<>(), responseData, StatusCode.OK, this.getClass().getName(),
                COUNT_DASHBOARD);
        return ResponseEntity.ok(responseData);
    }
}
