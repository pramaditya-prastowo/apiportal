package mii.bsi.apiportal.service;

import mii.bsi.apiportal.constant.StatusCode;
import mii.bsi.apiportal.domain.BsmApiConfig;
import mii.bsi.apiportal.domain.User;
import mii.bsi.apiportal.dto.BsmApiConfigDTO;
import mii.bsi.apiportal.repository.BsmApiConfigRepository;
import mii.bsi.apiportal.utils.CustomError;
import mii.bsi.apiportal.utils.JwtUtility;
import mii.bsi.apiportal.utils.RequestData;
import mii.bsi.apiportal.utils.ResponseHandling;
import mii.bsi.apiportal.validation.UserValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;

import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class BsmApiConfigServiceImpl implements BsmApiConfigService{

    @Autowired
    private BsmApiConfigRepository bsmApiConfigRepository;

    @Autowired
    private CacheManager cacheManager;
    @Autowired
    private UserValidation adminValidation;
    @Autowired
    private JwtUtility jwtUtility;
    @Autowired
    private LogService logService;

    public static final String GET_ALL = "Get All";
    public static final String GET_BY_KEYNAME = "Get By KEYNAME";
    public static final String CREATE = "Create";
    public static final String UPDATE = "Update";
    public static final String DELETE = "Delete";

    @Override
    public ResponseEntity<ResponseHandling<List<BsmApiConfig>>> getAll(String token){
        ResponseHandling<List<BsmApiConfig>> responseData = new ResponseHandling<>();
        RequestData requestData = new RequestData();
        try {

            if(!adminValidation.isAdmin(token)){
                responseData.failed("Access denied");
                logService.saveLog(requestData, responseData, StatusCode.FORBIDDEN, this.getClass().getName(),
                        GET_ALL);
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(responseData);
            }

            List<BsmApiConfig> data = bsmApiConfigRepository.findByEnabled("true");
            data.sort(Comparator.comparing(BsmApiConfig::getUpdatedDate));
            responseData.setPayload(data);
            responseData.success();

        }catch (Exception e){
            e.printStackTrace();
            responseData.failed(e.getMessage());
            logService.saveLog(requestData, responseData, StatusCode.INTERNAL_SERVER_ERROR, this.getClass().getName(),
                    GET_ALL);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseData);
        }
        logService.saveLog(requestData, responseData, StatusCode.OK, this.getClass().getName(),
                GET_ALL);
        return ResponseEntity.ok(responseData);
    }
    @Override
    public ResponseEntity<ResponseHandling<BsmApiConfig>> getByKeyname(String token, String keyname){
        ResponseHandling<BsmApiConfig> responseData = new ResponseHandling<>();
        RequestData requestData = new RequestData();
        requestData.setPayload(logService.setValueRequest("keyname", keyname));
        try {

            if(!adminValidation.isAdmin(token)){
                responseData.failed("Access denied");
                logService.saveLog(requestData, responseData, StatusCode.FORBIDDEN, this.getClass().getName(),
                        GET_BY_KEYNAME);
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(responseData);
            }

            BsmApiConfig data = bsmApiConfigRepository.findByKeyname(keyname);
            responseData.setPayload(data);
            responseData.success();

        }catch (Exception e){
            e.printStackTrace();
            responseData.failed(e.getMessage());
            logService.saveLog(requestData, responseData, StatusCode.INTERNAL_SERVER_ERROR, this.getClass().getName(),
                    GET_BY_KEYNAME);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseData);
        }
        logService.saveLog(requestData, responseData, StatusCode.OK, this.getClass().getName(),
                GET_BY_KEYNAME);
        return ResponseEntity.ok(responseData);
    }
    @Override
    public ResponseEntity<ResponseHandling> createParam(String token, BsmApiConfig request, Errors errors){
        ResponseHandling responseData = new ResponseHandling<>();
        RequestData<BsmApiConfig> requestData = new RequestData<>();
        requestData.setPayload(request);
        try {

            User user = adminValidation.getUserFromToken(token);

            if(user == null){
                responseData.failed("Access denied");
                logService.saveLog(requestData, responseData, StatusCode.FORBIDDEN, this.getClass().getName(),
                        CREATE);
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(responseData);
            }
            if(!adminValidation.isAdmin(token)){
                responseData.failed("Access denied");
                logService.saveLog(requestData, responseData, StatusCode.FORBIDDEN, this.getClass().getName(),
                        CREATE);
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(responseData);
            }

            if(errors.hasErrors()){
                responseData.failed(CustomError.validRequest(errors), "Bad Request");
                logService.saveLog(requestData, responseData, StatusCode.BAD_REQUEST, this.getClass().getName(),
                        CREATE);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseData);
            }

            BsmApiConfig existing = bsmApiConfigRepository.findByKeyname(request.getKeyname());
            if(existing!= null){
                responseData.failed("Keyname already registered");
                logService.saveLog(requestData, responseData, StatusCode.CONFLICT, this.getClass().getName(),
                        CREATE);
                return ResponseEntity.status(HttpStatus.CONFLICT).body(responseData);
            }

            request.initData(user.getId());

//            Date createdDate = new Date();
//            request.setCacheManager("APIPORTAL");
//            request.setEnabled("true");
//            request.setCreatedDate(createdDate);
//            request.setUpdatedDate(createdDate);

            bsmApiConfigRepository.save(request);
            responseData.success();

        }catch (Exception e){
            e.printStackTrace();
            responseData.failed(e.getMessage());
            logService.saveLog(requestData, responseData, StatusCode.INTERNAL_SERVER_ERROR, this.getClass().getName(),
                    CREATE);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseData);
        }
        logService.saveLog(requestData, responseData, StatusCode.CREATED, this.getClass().getName(),
                CREATE);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseData);
    }
    @Override
    public ResponseEntity<ResponseHandling> updateParam(String token, BsmApiConfig request, Errors errors){
        ResponseHandling responseData = new ResponseHandling<>();
        RequestData<BsmApiConfig> requestData = new RequestData<>();
        requestData.setPayload(request);
        try {
            if(!adminValidation.isAdmin(token)){
                responseData.failed("Access denied");
                logService.saveLog(requestData, responseData, StatusCode.FORBIDDEN, this.getClass().getName(),
                        UPDATE);
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(responseData);
            }

            if(errors.hasErrors()){
                responseData.failed(CustomError.validRequest(errors), "Bad Request");
                logService.saveLog(requestData, responseData, StatusCode.BAD_REQUEST, this.getClass().getName(),
                        UPDATE);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseData);
            }

            BsmApiConfig existing = bsmApiConfigRepository.findByKeyname(request.getKeyname());
            if(existing== null){
                responseData.failed("Keyname not found");
                logService.saveLog(requestData, responseData, StatusCode.NOT_FOUND, this.getClass().getName(),
                        UPDATE);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseData);
            }
            existing.setValue(request.getValue());
            existing.setDescription(request.getDescription());
            bsmApiConfigRepository.save(existing);
            responseData.success();

        }catch (Exception e){
            e.printStackTrace();
            responseData.failed(e.getMessage());
            logService.saveLog(requestData, responseData, StatusCode.INTERNAL_SERVER_ERROR, this.getClass().getName(),
                    UPDATE);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseData);
        }
        logService.saveLog(requestData, responseData, StatusCode.OK, this.getClass().getName(),
                UPDATE);
        return ResponseEntity.status(HttpStatus.OK).body(responseData);
    }
    @Override
    public ResponseEntity<ResponseHandling> deleteParam(String token, String keyname){
        ResponseHandling responseData = new ResponseHandling<>();
        RequestData<Map<String, Object>> requestData = new RequestData<>();
        requestData.setPayload(logService.setValueRequest("keyname", keyname));
        try {
            if(!adminValidation.isAdmin(token)){
                responseData.failed("Access denied");
                logService.saveLog(requestData, responseData, StatusCode.FORBIDDEN, this.getClass().getName(),
                        DELETE);
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(responseData);
            }

            BsmApiConfig existing = bsmApiConfigRepository.findByKeyname(keyname);
            if(existing== null){
                responseData.failed("Keyname not found");
                logService.saveLog(requestData, responseData, StatusCode.NOT_FOUND, this.getClass().getName(),
                        DELETE);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseData);
            }
            existing.setEnabled("false");
            bsmApiConfigRepository.save(existing);
            responseData.success();

        }catch (Exception e){
            e.printStackTrace();
            responseData.failed(e.getMessage());
            logService.saveLog(requestData, responseData, StatusCode.INTERNAL_SERVER_ERROR, this.getClass().getName(),
                    DELETE);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseData);
        }
        logService.saveLog(requestData, responseData, StatusCode.OK, this.getClass().getName(),
                DELETE);
        return ResponseEntity.status(HttpStatus.OK).body(responseData);
    }
    @Override
    public BsmApiConfigDTO readData(String keyName, String cacheManager, String cacheName){
        String result = "";

        BsmApiConfig bsmApiConfig = bsmApiConfigRepository.findByKeynameAndCacheManagerAndCacheName(keyName, cacheManager, cacheName);
        BsmApiConfigDTO bsmApiConfigDTO = new BsmApiConfigDTO();

        try{
            bsmApiConfigDTO = new BsmApiConfigDTO(bsmApiConfig);
            bsmApiConfigDTO.setResponseCode("00");
            bsmApiConfigDTO.setResponseMessage("success");
        }catch (Exception ex) {
            bsmApiConfigDTO.setResponseCode("99");
            bsmApiConfigDTO.setResponseMessage("Error getting config!");
        }

        return bsmApiConfigDTO;
    }
    @Override
    public BsmApiConfig getConfig(String keyname, String keygroup){
        try {
            BsmApiConfig bsmApiConfig = bsmApiConfigRepository.findByKeynameAndKeygroup(keyname, keygroup);
            return bsmApiConfig;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
    @Override
    public BsmApiConfig getConfigByKeyName(String keyName){
        try {
            BsmApiConfig bsmApiConfig = bsmApiConfigRepository.findByKeyname(keyName);
            return bsmApiConfig;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    //    public BsmApiConfig getConfig(String keyname) {
//        Cache cache = cacheManager.getCache("api_portal_cache");
//        Cache.ValueWrapper valueWrapper = cache.get(keyname);
//        if (valueWrapper != null) {
//            return (BsmApiConfig) valueWrapper.get();
//        } else {
//            BsmApiConfig config = bsmApiConfigRepository.findById(keyname).orElse(null);
//            if (config != null && config.getEnabled().equals("Y")) {
//                cache.put(config.getKeyname(), config);
//                return config;
//            } else {
//                return null;
//            }
//        }
//    }
//    @EventListener(ApplicationReadyEvent.class)
//    public void loadCache() {
//        List<BsmApiConfig> configList = bsmApiConfigRepository.findAll();
//        System.out.println(configList);
//        Cache cache = cacheManager.getCache("email_cache");
//        for (BsmApiConfig config : configList) {
//            if (config.getEnabled().equals("Y")) {
//                cache.put(config.getKeyname(), config);
//            }
//        }
//    }
}
