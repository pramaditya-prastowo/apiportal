package mii.bsi.apiportal.service;

import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import mii.bsi.apiportal.constant.StatusCode;
import mii.bsi.apiportal.domain.BsiLogApiPortal;
import mii.bsi.apiportal.repository.BsiLogApiPortalRepository;
import mii.bsi.apiportal.utils.RequestData;
import mii.bsi.apiportal.utils.ResponseHandling;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class LogService {

    @Autowired
    private BsiLogApiPortalRepository logRepository;

    private Gson gson = new Gson();

    public void saveLog(RequestData request, ResponseHandling response,
                        StatusCode statusCode, String serviceName, String actionType){

        BsiLogApiPortal logData = new BsiLogApiPortal();
        logData.setLogId(null);
        logData.setUserId(null);
        logData.setServiceName(serviceName.substring(18));
        logData.setActionType(actionType);
        logData.setStatusMessage(statusCode.toString());
        logData.setStatusCode(statusCode.getCode());
        logData.setErrorCode(response.getResponseCode());
        logData.setRequest(gson.toJson(request.getPayload()));
        logData.setResponse(gson.toJson(response));
        logData.setCreateDate(new Date());
        log.info("==================================================================");
        log.info("Request Data : "+gson.toJson(request.getPayload()));
        log.info("Response Data : "+gson.toJson(response));
        log.info("Log Data : "+gson.toJson(logData));
        log.info("==================================================================");

        logRepository.save(logData);
    }

    public Map<String, Object> setValueRequest(Map<String, Object> request, String key, Object value){
        request.put(key, value);
        return request;
    }

    public Map<String, Object> setValueRequest(String key, Object value){
        Map<String, Object> request = new HashMap<>();
        request.put(key, value);
        return request;
    }
}
