package mii.bsi.apiportal.service;

import mii.bsi.apiportal.constant.StatusCode;
import mii.bsi.apiportal.domain.BsmApiConfig;
import mii.bsi.apiportal.repository.BsmApiConfigRepository;
import mii.bsi.apiportal.utils.CustomError;
import mii.bsi.apiportal.utils.RequestData;
import mii.bsi.apiportal.utils.ResponseHandling;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ParamsService {

    @Autowired
    private BsmApiConfigRepository configRepository;
    @Autowired
    private LogService logService;

    public static final String PARAMS_PRIVACY_POLICY = "Params Privacy Policy";

    public ResponseEntity<ResponseHandling<Map<String, Object>>> getPrivacyPolicy(){
        ResponseHandling<Map<String, Object>> responseData = new ResponseHandling<>();
        RequestData requestData = new RequestData();

        try {
            List<BsmApiConfig> listResult = getPrivacyPolicyParams();
            Map<String, Object> response = new HashMap<>();
            for (BsmApiConfig conf: listResult) {
                response.put(conf.getKeyname().replaceAll("\\.", "_"), conf.getValue());
            }

            responseData.setPayload(response);
            responseData.success();

        }catch (Exception e){
            e.printStackTrace();
            responseData.failed("INTERNAL SERVER ERROR");
            logService.saveLog(requestData, responseData, StatusCode.INTERNAL_SERVER_ERROR, this.getClass().getName(),
                    PARAMS_PRIVACY_POLICY);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseData);
        }
        logService.saveLog(requestData, responseData, StatusCode.OK, this.getClass().getName(),
                PARAMS_PRIVACY_POLICY);
        return ResponseEntity.status(HttpStatus.OK).body(responseData);
    }

    public List<BsmApiConfig> getPrivacyPolicyParams(){
        return configRepository.findByKeynameIn(new ArrayList<>(Arrays.asList("kebijakan.privasi", "syarat.ketentuan")));
    }

    public List<BsmApiConfig> getParamsByKeyGroup(String keyGroup){
        try {
            List<BsmApiConfig> result = configRepository.findByKeygroup(keyGroup);
            return result;
        }catch (Exception e){
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

}
