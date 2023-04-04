package mii.bsi.apiportal.service;

import com.google.gson.Gson;
import mii.bsi.apiportal.apigw.ApiGatewayService;
import mii.bsi.apiportal.apigw.dto.CreateAppRequestDTO;
import mii.bsi.apiportal.apigw.model.ResponseApiGw;
import mii.bsi.apiportal.constant.StatusCode;
import mii.bsi.apiportal.domain.Application;
import mii.bsi.apiportal.domain.ApplicationServiceApi;
import mii.bsi.apiportal.domain.BsmApiKey;
import mii.bsi.apiportal.domain.User;
import mii.bsi.apiportal.dto.ApplicationDetailResponseDTO;
import mii.bsi.apiportal.repository.AppServiceApiRepository;
import mii.bsi.apiportal.repository.BsmApiKeyRepository;
import mii.bsi.apiportal.repository.MyAppsRepository;
import mii.bsi.apiportal.repository.dao.impl.AppServiceApiDao;
import mii.bsi.apiportal.utils.CustomError;
import mii.bsi.apiportal.utils.EmailUtility;
import mii.bsi.apiportal.utils.RequestData;
import mii.bsi.apiportal.utils.ResponseHandling;
import mii.bsi.apiportal.validation.UserValidation;
import org.apache.commons.lang3.RandomStringUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;

import java.util.ArrayList;
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
    @Autowired
    private AppServiceApiDao serviceApiDao;
    @Autowired
    private BsmApiKeyRepository bsmApiKeyRepository;
    @Autowired
    private EmailUtility emailUtility;
    @Autowired
    private BsmApiKeyService apiKeyService;

    @Autowired
    private ApiGatewayService apiGatewayService;

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

            String corpId = generateCorpId(apps.getCompanyName());
            String secretKey = RandomStringUtils.randomAlphanumeric(32);;
            String clientKey = RandomStringUtils.randomAlphanumeric(20);;
            CreateAppRequestDTO requestBody = new CreateAppRequestDTO(corpId, apps.getApplicationName(), apps.getCompanyName(),secretKey);

            ResponseApiGw responseApiGw = apiGatewayService.createApplication(requestBody);


            if(responseApiGw.getStatusCode() != 200){
                if(responseApiGw.getStatusCode() == 408){
                    responseData.failed("Request Timeout from API Gateway");
                }else{
                    responseData.failed(responseApiGw.getStatusCode() + " - Failed");
                }

                logService.saveLog(requestData, responseData, StatusCode.OK, this.getClass().getName(),
                        CREATE);
                return  ResponseEntity.status(HttpStatus.OK).body(responseData);
            }

            BsmApiKey bsmApiKey = new BsmApiKey(requestBody, clientKey);

            System.out.println(apps.getListService());

            apps.setCreatedDate(new Date());
            apps.setCreatedBy(user.getId());
            apps.setUserId(user.getId());
            apps.setUserId(user.getId());
            apps.setCorpId(corpId);
            emailUtility.sendCredentialApplication(user, secretKey, clientKey, corpId);

            Application application = myAppsRepository.save(apps);
            bsmApiKeyRepository.save(bsmApiKey);

            for (Object data : apps.getListService()) {
                ApplicationServiceApi appService = new ApplicationServiceApi();
                Integer value = (Integer) data;
                appService.setServiceApiId(value.longValue());
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

    public ResponseEntity<ResponseHandling<ApplicationDetailResponseDTO>> getById(Long appId, String token){
        ResponseHandling<ApplicationDetailResponseDTO> responseData = new ResponseHandling<>();
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

            List<ApplicationServiceApi> listApi = serviceApiDao.getServiceApiByAppId(appId);
            BsmApiKey bsmApiKey = apiKeyService.getByCorpId(application.getCorpId());
            ApplicationDetailResponseDTO responseDTO = new ApplicationDetailResponseDTO();
            responseDTO.setApplication(application);
            responseDTO.setListService(listApi);
            responseDTO.setApiKey(bsmApiKey);


            responseData.setPayload(responseDTO);
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

    private String generateCorpId(String companyName){

        int seq = Integer.parseInt(myAppsRepository.getCorpIdSeq());

        String name = companyName.toUpperCase().replaceAll("\\.", "").replaceAll("PT ","");
        String [] names  = name.split(" ");

        String initialName = "";
        if(names.length > 2){
            for (int i = 0; i < names.length; i++) {
                System.out.println(names[i]);
                initialName = initialName + names[i].charAt(0);
            }
        }else if(names.length == 1){
            initialName = names[0].substring(0,3);
        }else{
            initialName = names[0].substring(0,2) + names[1].charAt(0);
        }
        return initialName.substring(0,3) + String.format("%05d", seq);
    }
}
