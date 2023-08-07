package mii.bsi.apiportal.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import io.jsonwebtoken.Claims;
import mii.bsi.apiportal.apigw.DashboardAPIService;
import mii.bsi.apiportal.apigw.model.OutputDashboardResponse;
import mii.bsi.apiportal.apigw.model.ResponseApiGw;
import mii.bsi.apiportal.apigw.model.ServiceRank;
import mii.bsi.apiportal.constant.StatusCode;
import mii.bsi.apiportal.domain.PengajuanKerjasama;
import mii.bsi.apiportal.domain.User;
import mii.bsi.apiportal.domain.model.ApprovalStatus;
import mii.bsi.apiportal.domain.model.Roles;
import mii.bsi.apiportal.dto.CountDashboard;
import mii.bsi.apiportal.repository.ServiceApiRepository;
import mii.bsi.apiportal.repository.UserRepository;
import mii.bsi.apiportal.utils.JwtUtility;
import mii.bsi.apiportal.utils.RequestData;
import mii.bsi.apiportal.utils.ResponseHandling;
import mii.bsi.apiportal.validation.UserValidation;
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

    @Autowired
    private DashboardAPIService dashboardAPIService;
    @Autowired
    private PengajuanKerjasamaService pengajuanKerjasamaService;
    @Autowired
    private UserValidation userValidation;
    private Gson gson = new Gson();

    public static final String COUNT_DASHBOARD = "Count Dashboard";
    public static final String SERVICE_RANK = "Service Rank";
    public static final String MITRA_TERDAFTAR = "Mitra Terdaftar";

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
            Long countRequestDaily = 0L;
            Long countMitraRegister = 0L;
            ResponseEntity<ResponseHandling<Integer>> responseMitraTerdaftar = pengajuanKerjasamaService.getCountMitraTerdaftar(token);
            ResponseEntity<ResponseHandling<String>> responseRequestDaily = dashboardAPIService.totalServiceDaily();
            if(responseMitraTerdaftar.getStatusCode().equals(HttpStatus.OK)){
                countMitraRegister = Long.parseLong(responseMitraTerdaftar.getBody().getPayload().toString());
            }
            if(responseRequestDaily.getStatusCode().equals(HttpStatus.OK)){
                OutputDashboardResponse outputDashboardResponse = gson.fromJson(responseRequestDaily.getBody().getPayload(), OutputDashboardResponse.class);
                countRequestDaily = Long.parseLong(outputDashboardResponse.getOutput());
            }

            listCount.add(new CountDashboard("Total Service API", countService.intValue(),"setting/service-api"));
            listCount.add(new CountDashboard("Total Request API Hari ini", countRequestDaily.intValue(),"dashboard-main/log-api/detail"));
            listCount.add(new CountDashboard("Total Mitra Terdaftar", countMitraRegister.intValue(),"dashboard-main/mitra"));
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

    public ResponseEntity<ResponseHandling<List<ServiceRank>>> getServiceRank(String token){
        ResponseHandling<List<ServiceRank>> responseData = new ResponseHandling<>();

        try {
            User user = userValidation.getUserFromToken(token);
            if(user == null){
                responseData.failed("User Not Found");
                logService.saveLog(new RequestData<>(), responseData, StatusCode.NOT_FOUND, this.getClass().getName(),
                        SERVICE_RANK);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseData);
            }
            if(!userValidation.isAdmin(token)){
                responseData.failed("Access denied");
                logService.saveLog(new RequestData<>(), responseData, StatusCode.FORBIDDEN, this.getClass().getName(),
                        SERVICE_RANK);
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(responseData);
            }

            List<ServiceRank> participantJsonList = new ArrayList<>();
            ResponseEntity<ResponseHandling<String>> responseServiceRank = dashboardAPIService.getTotalHitServiceByServiceName();
            if(responseServiceRank.getStatusCode().equals(HttpStatus.OK)){
                ServiceRank outputDashboardResponse = gson.fromJson(responseServiceRank.getBody().getPayload(), ServiceRank.class);
                ObjectMapper mapper = new ObjectMapper();
                participantJsonList = mapper.readValue(responseServiceRank.getBody().getPayload(), new TypeReference<List<ServiceRank>>(){});
            }

            responseData.setPayload(participantJsonList);
            responseData.success();

        }catch (Exception e){
            e.printStackTrace();
            responseData.failed(e.getMessage());
            logService.saveLog(new RequestData<>(), responseData, StatusCode.INTERNAL_SERVER_ERROR, this.getClass().getName(),
                    SERVICE_RANK);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseData);
        }

        logService.saveLog(new RequestData<>(), responseData, StatusCode.OK, this.getClass().getName(),
                SERVICE_RANK);
        return ResponseEntity.ok(responseData);
    }

    public ResponseEntity<ResponseHandling<List<PengajuanKerjasama>>> getMitraTerdaftar(String token, ApprovalStatus status){
        ResponseHandling<List<PengajuanKerjasama>> responseData = new ResponseHandling<>();
        try {
            User user = userValidation.getUserFromToken(token);
            if(user == null){
                responseData.failed("User Not Found");
                logService.saveLog(new RequestData<>(), responseData, StatusCode.NOT_FOUND, this.getClass().getName(),
                        MITRA_TERDAFTAR);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseData);
            }
            if(!userValidation.isAdmin(token)){
                responseData.failed("Access denied");
                logService.saveLog(new RequestData<>(), responseData, StatusCode.FORBIDDEN, this.getClass().getName(),
                        MITRA_TERDAFTAR);
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(responseData);
            }
            ResponseEntity<ResponseHandling<List<PengajuanKerjasama>>> responseEntity = pengajuanKerjasamaService.getPengejuanKerjasamaByStatus(token,status);
            if(responseEntity.getStatusCode().equals(HttpStatus.OK)){
                responseData.setPayload(responseEntity.getBody().getPayload());
                responseData.success();
//                logService.saveLog(new RequestData<>(), responseData, StatusCode.OK, this.getClass().getName(),
//                        MITRA_TERDAFTAR);
                return ResponseEntity.ok(responseData);
            }else{
                responseData.setPayload(responseEntity.getBody().getPayload());
                responseData.failed(responseEntity.getBody().getResponseMessage());
                logService.saveLog(new RequestData<>(), responseData, StatusCode.OK, this.getClass().getName(),
                        MITRA_TERDAFTAR);
                return ResponseEntity.ok(responseData);
            }


        }catch (Exception e){
            e.printStackTrace();
            responseData.failed(e.getMessage());
            logService.saveLog(new RequestData<>(), responseData, StatusCode.INTERNAL_SERVER_ERROR, this.getClass().getName(),
                    MITRA_TERDAFTAR);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseData);
        }

    }
}
