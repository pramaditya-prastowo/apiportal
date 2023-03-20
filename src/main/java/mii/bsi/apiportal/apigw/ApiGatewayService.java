package mii.bsi.apiportal.apigw;

import mii.bsi.apiportal.apigw.dto.CreateAppRequestDTO;
import mii.bsi.apiportal.apigw.dto.RequestTokenResponseDTO;
import mii.bsi.apiportal.apigw.model.RequestHeaderApi;
import mii.bsi.apiportal.apigw.model.ResponseApiGw;
import mii.bsi.apiportal.constant.StatusCode;
import mii.bsi.apiportal.domain.BsmApiConfig;
import mii.bsi.apiportal.domain.model.Token;
import mii.bsi.apiportal.repository.BsmApiConfigRepository;
import mii.bsi.apiportal.service.BsmApiConfigService;
import mii.bsi.apiportal.service.LogService;
import mii.bsi.apiportal.utils.DateUtils;
import mii.bsi.apiportal.utils.EncryptUtility;
import mii.bsi.apiportal.utils.RequestData;
import mii.bsi.apiportal.utils.ResponseHandling;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

@Service
public class ApiGatewayService {

    @Autowired
    private LogService logService;
    @Autowired
    private DateUtils dateUtils;
    @Autowired
    private EncryptUtility encryptUtility;
    @Autowired
    private DataApiClient dataApiClient;
    @Autowired
    private BsmApiConfigService configService;
    @Autowired
    private BsmApiConfigRepository configRepository;
    @Value("${endpoint.insert.application}")
    private String insertApplicationPath;
    @Value("${endpoint.request.token}")
    private String requestTokenPath;
    public static final String GENERATE_TOKEN = "Generate Token";

    public String createApplication(CreateAppRequestDTO request) {
        BsmApiConfig hostApiGw = configService.getConfigByKeyName("base.url.apigw");
        String url = hostApiGw.getValue() + insertApplicationPath;
        System.out.println("URL : " + url);
        String responseData2 = null;
        try {
            responseData2 = dataApiClient.postData(url, request);
//            System.out.println(responseData2);
            return responseData2;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public String signatureAuth(){
        String url = "http://10.0.116.127:5555/api/v1.0/utilities/signature-auth";
        CloseableHttpClient httpClient = HttpClients.createDefault();
        DataApiClient dataApiClient2 = new DataApiClient(httpClient);
        String responseData2 = null;
        try {
            responseData2 = dataApiClient2.signatureAuth(url);
            System.out.println(responseData2);
            return responseData2;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public ResponseApiGw requestToken(RequestHeaderApi requestHeader){
        BsmApiConfig hostApiGw = configService.getConfigByKeyName("base.url.apigw");
        String url = hostApiGw.getValue() + requestTokenPath;
        ResponseApiGw responseApiGw;
        try {

            responseApiGw = dataApiClient.requestToken(url, requestHeader);
            return responseApiGw;
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseApiGw(500,"");
        }
    }

    public ResponseEntity<ResponseHandling<RequestTokenResponseDTO>> requestTokenB2B(String token){
        ResponseHandling<RequestTokenResponseDTO> responseData = new ResponseHandling<>();
        RequestData requestData = new RequestData<>();

        try {
            BsmApiConfig privateKeyConf = configRepository.findByKeyname("private.key");
            BsmApiConfig clientKeyConf = configRepository.findByKeyname("client.key");
            System.out.println(privateKeyConf.getValue());
            System.out.println(clientKeyConf.getValue());

            String privateKey = privateKeyConf.getValue();
            String clientKey = clientKeyConf.getValue();
            String timeStamp = dateUtils.dateIsoString(new Date());
            String signature =  encryptUtility.generateSignature(privateKey, clientKey+"|"+timeStamp);
            RequestHeaderApi requestHeader = new RequestHeaderApi();
            requestHeader.setSignature(signature);
            requestHeader.setTimestamp(timeStamp);
            requestHeader.setClientKey(clientKey);
//            requestHeader.setClientKey("5ca21839-0cd6-4094-b55a-2d53994bfbb3");
            ResponseApiGw responseToken = requestToken(requestHeader);

            if(responseToken.getStatusCode() == 408){
                responseData.failed("Request Timeout");
                logService.saveLog(requestData, responseData, StatusCode.REQUEST_TIMEOUT, this.getClass().getName(),
                        GENERATE_TOKEN);
                return ResponseEntity.status(HttpStatus.REQUEST_TIMEOUT).body(responseData);
            }
            if(responseToken.getStatusCode() == 500){
                responseData.failed("Internal Server Error");
                logService.saveLog(requestData, responseData, StatusCode.INTERNAL_SERVER_ERROR, this.getClass().getName(),
                        GENERATE_TOKEN);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseData);
            }

            JSONObject jsonObject = new JSONObject(responseToken.getResponseBody());
            Calendar calendar = Calendar.getInstance();

            calendar.add(Calendar.SECOND, Integer.parseInt(jsonObject.getString("expiresIn")));

            Date newDate = calendar.getTime();
            Token tokenB2B = new Token(jsonObject.getString("accessToken"), jsonObject.getString("tokenType"),jsonObject.getString("expiresIn"), newDate, signature);
            responseData.setPayload(new RequestTokenResponseDTO(tokenB2B));
            responseData.success();

        }catch (Exception e){
            e.printStackTrace();
            responseData.failed(e.getMessage());
            logService.saveLog(requestData, responseData, StatusCode.INTERNAL_SERVER_ERROR, this.getClass().getName(),
                    GENERATE_TOKEN);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseData);
        }

        logService.saveLog(requestData, responseData, StatusCode.OK, this.getClass().getName(),
                GENERATE_TOKEN);
        return ResponseEntity.ok(responseData);
    }

    //    public String balanceInquiry(String signature, String timestamp, String token){
//        String url = "http://10.0.116.127:5555/v1.0/balance-inquiry";
//        CloseableHttpClient httpClient = HttpClients.createDefault();
//        DataApiClient dataApiClient2 = new DataApiClient(httpClient);
//        String responseData2 = null;
//        try {
//            responseData2 = dataApiClient2.balanceInquiry(url, signature, timestamp, token);
////            System.out.println(responseData2);
//            return responseData2;
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//    }
}
