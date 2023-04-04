package mii.bsi.apiportal.apigw;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonObject;
import mii.bsi.apiportal.apigw.model.RequestHeaderApi;
import mii.bsi.apiportal.apigw.model.ResponseApiGw;
import mii.bsi.apiportal.service.LogService;
import mii.bsi.apiportal.utils.DateUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/try-api")
public class TryApiController {

    @Autowired
    private TryApiService tryApiService;

    @Autowired
    private DateUtils dateUtils;
    @Autowired
    private LogService logService;

    @PostMapping("/old/{serviceName}")
    public String tryApi(@PathVariable String serviceName, @RequestHeader("X-PATH") String path,
                             @RequestHeader("access_token") String access_token,
                             @RequestHeader("signature") String signature,
                             @RequestHeader("endpoint_url") String endpointUrl,
                             @RequestBody String requestBody){

        RequestHeaderApi headerService = generateRequestHeaderService(path, access_token, signature);
        RequestHeaderApi headerSignature = generateRequestHeaderSignature(endpointUrl, access_token);

        ResponseApiGw response = tryApiService.tryServiceApi(requestBody,headerSignature,headerService, serviceName);
        return response.getResponseBody();
//        return  requestBody;
    }

    @PostMapping("/{serviceName}")
    public String tryOutServiceApi(@PathVariable String serviceName,
                                   @RequestHeader("access_token") String token,
                                   @RequestHeader("X-SIGNATURE") String signature,
                                   @RequestHeader("X-TIMESTAMP") String timestamp,
                                   @RequestHeader("X-PARTNER-ID") String partnerId,
                                   @RequestHeader("X-EXTERNAL-ID") String externalId,
                                   @RequestHeader("CHANNEL-ID") String channelId,
                                   @RequestBody String requestBody){
        RequestHeaderApi requestHeader = new RequestHeaderApi();
        requestHeader.setSignature(signature);
        requestHeader.setTimestamp(timestamp);
        requestHeader.setPartnerId(partnerId);
        requestHeader.setExternalId(externalId);
        requestHeader.setChannelId(channelId);
        requestHeader.setAccessToken(token);
        requestHeader.setEndPointUrl(serviceName);
        ResponseApiGw responseApiGw =  tryApiService.tryOutServiceApi(requestHeader, requestBody);
        return responseApiGw.getResponseBody();
    }

    RequestHeaderApi generateRequestHeaderService(String path, String access_token, String signature){
        String timeStamp = dateUtils.dateIsoString(new Date());
        RequestHeaderApi requestHeader = new RequestHeaderApi();

        requestHeader.setPath(path);
        requestHeader.setBearerToken(access_token);
        requestHeader.setSignature(signature);
        requestHeader.setTimestamp(timeStamp);
        requestHeader.setPartnerId("DDBSI00002 ");
        requestHeader.setExternalId("41807553358950093184162180797837");
        requestHeader.setChannelId("12345");

        return requestHeader;
    }

    RequestHeaderApi generateRequestHeaderSignature(String path, String access_token){
        String timeStamp = dateUtils.dateIsoString(new Date());
        RequestHeaderApi requestHeader = new RequestHeaderApi();

        requestHeader.setClientSecret("m4nd1r1sy4r14hku");
        requestHeader.setBearerToken(access_token);
        requestHeader.setTimestamp(timeStamp);
        requestHeader.setPath(path);

        return requestHeader;
    }



//    private Map<String, String> stringToJson(String value){
////        String jsonString = "{\n" +
////                "    \"accountNo\": \"2110199457\"\n" +
////                "}";
//
//        // Mengkonversi String dalam format JSON menjadi Map
//        ObjectMapper objectMapper = new ObjectMapper();
//        Map<String, String> data = null;
//        try {
//            data = objectMapper.readValue(value, new TypeReference<Map<String, String>>(){});
//            return data;
//        } catch (JsonProcessingException e) {
//            throw new RuntimeException(e);
//        }
//    }
//    private String jsonToString(Map<String, String> value){
////        Map<String, String> data = new HashMap<>();
////        data.put("accountNo", "2110199457");
//
//        // Mengkonversi Map menjadi String dalam format JSON
//        ObjectMapper objectMapper = new ObjectMapper();
//        String jsonString = null;
//        try {
//            jsonString = objectMapper.writeValueAsString(value);
//            return jsonString;
//        } catch (JsonProcessingException e) {
//            throw new RuntimeException(e);
//        }
//    }
}
