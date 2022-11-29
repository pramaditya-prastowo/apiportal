package mii.bsi.apiportal.service;

import mii.bsi.apiportal.constant.Params;
import mii.bsi.apiportal.constant.StatusCode;
import mii.bsi.apiportal.utils.EncryptUtility;
import mii.bsi.apiportal.utils.RequestData;
import mii.bsi.apiportal.utils.ResponseHandling;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class EncryptService {

    @Autowired
    private LogService logService;

    @Autowired
    private EncryptUtility encryptUtility;

    public static final String ENCRYPT = "Encrypt";
    public static final String DECRYPT = "Decrypt";

    public ResponseEntity<ResponseHandling<String>> encrypt(String value){
        RequestData<Map<String, Object>> requestData = new RequestData<>();
        ResponseHandling<String> responseData = new ResponseHandling<>();
        Map<String, Object> request = new HashMap<>();
        request.put("value", value);
        requestData.setPayload(request);

        try {
            if(value.equals(null) || value.equals("")){
                responseData.success();
                responseData.setPayload("");
                logService.saveLog(requestData, responseData, StatusCode.OK, this.getClass().getName(), ENCRYPT);
            }

            final String result = encryptUtility.encryptAES(value, Params.PASS_KEY);

            responseData.success();
            responseData.setPayload(result);

        }catch (Exception e){
            responseData.failed(e.getMessage());
            e.printStackTrace();
            logService.saveLog(requestData, responseData, StatusCode.INTERNAL_SERVER_ERROR, this.getClass().getName(), ENCRYPT);
            return ResponseEntity.internalServerError().body(responseData);
        }

        logService.saveLog(requestData, responseData, StatusCode.OK, this.getClass().getName(), ENCRYPT);
        return ResponseEntity.ok(responseData);
    }

    public ResponseEntity<ResponseHandling<String>> decrypt(String value){
        RequestData<Map<String, Object>> requestData = new RequestData<>();
        ResponseHandling<String> responseData = new ResponseHandling<>();
        Map<String, Object> request = new HashMap<>();
        request.put("value", value);
        requestData.setPayload(request);

        try {
            if(value.equals(null) || value.equals("")){
                responseData.success();
                responseData.setPayload("");
                logService.saveLog(requestData, responseData, StatusCode.OK, this.getClass().getName(), DECRYPT);
            }

            final String result = encryptUtility.decryptAES(value, Params.PASS_KEY);

            responseData.success();
            responseData.setPayload(result);

        }catch (Exception e){
            responseData.failed(e.getMessage());
            e.printStackTrace();
            logService.saveLog(requestData, responseData, StatusCode.INTERNAL_SERVER_ERROR, this.getClass().getName(), DECRYPT);
            return ResponseEntity.internalServerError().body(responseData);
        }

        logService.saveLog(requestData, responseData, StatusCode.OK, this.getClass().getName(), DECRYPT);
        return ResponseEntity.ok(responseData);
    }
}
