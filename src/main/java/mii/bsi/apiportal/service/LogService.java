package mii.bsi.apiportal.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import mii.bsi.apiportal.constant.StatusCode;
import mii.bsi.apiportal.domain.BsiLogApiPortal;
import mii.bsi.apiportal.domain.LogApiGw;
import mii.bsi.apiportal.repository.BsiLogApiPortalRepository;
import mii.bsi.apiportal.repository.LogApiGwRepository;
import mii.bsi.apiportal.utils.RequestData;
import mii.bsi.apiportal.utils.ResponseHandling;
import org.apache.http.Header;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class LogService {

    @Autowired
    private BsiLogApiPortalRepository logRepository;

    @Autowired
    private LogApiGwRepository logApiGwRepository;

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

    public void logApiGw(Header[] requestHeader, Map<String, String> requestBody,
                         String responseBody, String serviceName, String statusCode, String endPoint){
        LogApiGw logData = new LogApiGw();
        logData.setLogId(null);
        logData.setUserId(null);
        logData.setServiceName(serviceName);
        logData.setStatusCode(statusCode);
        logData.setCreateDate(new Date());
        logData.setRequestHeader(gson.toJson(headersToJson(requestHeader)));
        logData.setRequestBody(gson.toJson(requestBody));
        logData.setResponseBody(responseBody);

        log.info("==================================================================");
        log.info("URL end point : " + endPoint);
        log.info("Status Code : " + statusCode);
        log.info("Request Header : " + gson.toJson(headersToJson(requestHeader)));
        log.info("Request Body : " + gson.toJson(requestBody));
        log.info("Response Body : " + gson.toJson(responseBody));
        log.info("Log Data : "+gson.toJson(logData));
        log.info("==================================================================");
        logApiGwRepository.save(logData);
    }

    public Map<String, String> headersToJson(Header[] headers){
        Map<String, String> result = new HashMap<>();
        for (Header data: headers) {
            result.put(data.getName(), data.getValue());
        }
        return result;
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

    public Map<String, String> stringToJson(String value){
        if(value.equals(null)){
            return new HashMap<>();
        }
//        String jsonString = "{\n" +
//                "    \"accountNo\": \"2110199457\"\n" +
//                "}";

        // Mengkonversi String dalam format JSON menjadi Map
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, String> data = null;
        try {
            data = objectMapper.readValue(value, new TypeReference<Map<String, String>>(){});
            return data;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
    public String jsonToString(Map<String, String> value){
//        Map<String, String> data = new HashMap<>();
//        data.put("accountNo", "2110199457");

        // Mengkonversi Map menjadi String dalam format JSON
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = null;
        try {
            jsonString = objectMapper.writeValueAsString(value);
            return jsonString;
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
