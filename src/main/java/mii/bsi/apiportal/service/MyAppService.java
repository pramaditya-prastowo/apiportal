package mii.bsi.apiportal.service;

import com.google.gson.Gson;
import mii.bsi.apiportal.constant.StatusCode;
import mii.bsi.apiportal.domain.Application;
import mii.bsi.apiportal.domain.ApplicationServiceApi;
import mii.bsi.apiportal.domain.User;
import mii.bsi.apiportal.repository.AppServiceApiRepository;
import mii.bsi.apiportal.repository.MyAppsRepository;
import mii.bsi.apiportal.utils.CustomError;
import mii.bsi.apiportal.utils.RequestData;
import mii.bsi.apiportal.utils.ResponseHandling;
import mii.bsi.apiportal.validation.UserValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class MyAppService {

    @Autowired
    private MyAppsRepository myAppsRepository;

    @Autowired
    private LogService logService;
    @Autowired
    private UserValidation userValidation;
    @Autowired
    private AppServiceApiRepository appServiceApiRepository;

    public static final String GET_BY_USER = "Get by UserId";
    public static final String GET_BY_ID = "Get by ID";
    public static final String CREATE = "Create";

    private Gson gson = new Gson();

    public ResponseEntity<ResponseHandling<List<Application>>> getAllByIdUser(String token){
        ResponseHandling<List<Application>> responseData = new ResponseHandling<>();
        RequestData requestData = new RequestData<>();

        try {
            User user = userValidation.getUserFromToken(token);

            if(user == null){
                responseData.failed("Data not found");
                logService.saveLog(requestData, responseData, StatusCode.NOT_FOUND, this.getClass().getName(),
                        GET_BY_USER);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseData);
            }

            List<Application> list = myAppsRepository.findByUserId(user.getId());
            responseData.success();
            responseData.setPayload(list);

        }catch (Exception e){
            e.printStackTrace();
            responseData.failed(e.getMessage());
            logService.saveLog(requestData, responseData, StatusCode.INTERNAL_SERVER_ERROR, this.getClass().getName(),
                    GET_BY_USER);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseData);
        }
        logService.saveLog(requestData, responseData, StatusCode.OK, this.getClass().getName(),
                GET_BY_USER);
        return ResponseEntity.ok(responseData);
    }

    public ResponseEntity<ResponseHandling<Application>> create(Application apps,String token, Errors errors){
        ResponseHandling<Application> responseData = new ResponseHandling<>();
        RequestData<Application> requestData = new RequestData<>();
        requestData.setPayload(apps);

        try {

            if (errors.hasErrors()) {
                responseData.failed(CustomError.validRequest(errors), "Bad Request");
                logService.saveLog(requestData, responseData, StatusCode.BAD_REQUEST, this.getClass().getName(),
                        CREATE);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseData);
            }

            User user = userValidation.getUserFromToken(token);

            if(user == null){
                responseData.failed("User not found");
                logService.saveLog(requestData, responseData, StatusCode.NOT_FOUND, this.getClass().getName(),
                        CREATE);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseData);
            }

            System.out.println(apps.getListService());

            apps.setCreatedDate(new Date());
            apps.setCreatedBy(user.getId());
            apps.setUserId(user.getId());
            apps.setUserId(user.getId());
            Application application = myAppsRepository.save(apps);

            for (Integer data : apps.getListService()) {
                ApplicationServiceApi appService = new ApplicationServiceApi();
                appService.setServiceApiId(data.longValue());
                appService.setAppId(application.getId());
                appServiceApiRepository.save(appService);
            }

            responseData.setPayload(application);
            responseData.success();

        }catch (Exception e){
            e.printStackTrace();
            responseData.failed(e.getMessage());
            logService.saveLog(requestData, responseData, StatusCode.INTERNAL_SERVER_ERROR, this.getClass().getName(),
                    CREATE);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseData);
        }
        logService.saveLog(requestData, responseData, StatusCode.OK, this.getClass().getName(),
                CREATE);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseData);
    }

    public ResponseEntity<ResponseHandling<Application>> getById(Long appId, String token){
        ResponseHandling<Application> responseData = new ResponseHandling<>();
        RequestData<Map<String, Object>> requestData = new RequestData<>();
        requestData.setPayload(logService.setValueRequest("id", appId));
        try {
            User user = userValidation.getUserFromToken(token);
            if(user == null){
                responseData.failed("User not found");
                logService.saveLog(requestData, responseData, StatusCode.NOT_FOUND, this.getClass().getName(),
                        GET_BY_ID);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseData);
            }

            Application application = myAppsRepository.findById(appId).orElse(null);
            if(application == null){
                responseData.failed("Application not found");
                logService.saveLog(requestData, responseData, StatusCode.NOT_FOUND, this.getClass().getName(),
                        GET_BY_ID);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseData);
            }
            if(!user.getId().equals(application.getUserId())){
                responseData.failed("Application not found");
                logService.saveLog(requestData, responseData, StatusCode.NOT_FOUND, this.getClass().getName(),
                        GET_BY_ID);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseData);
            }

            responseData.setPayload(application);
            responseData.success();
            System.out.println(gson.toJson(responseData));
        }catch (Exception e){
            e.printStackTrace();
            responseData.failed(e.getMessage());
            logService.saveLog(requestData, responseData, StatusCode.INTERNAL_SERVER_ERROR, this.getClass().getName(),
                    GET_BY_ID);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseData);
        }
        logService.saveLog(requestData, responseData, StatusCode.OK, this.getClass().getName(),
                GET_BY_ID);
        return ResponseEntity.ok(responseData);

    }
}
