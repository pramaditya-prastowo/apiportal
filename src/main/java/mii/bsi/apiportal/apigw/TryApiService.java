package mii.bsi.apiportal.apigw;

import mii.bsi.apiportal.apigw.model.RequestHeaderApi;
import mii.bsi.apiportal.apigw.model.ResponseApiGw;
import mii.bsi.apiportal.domain.BsmApiConfig;
import mii.bsi.apiportal.service.BsmApiConfigServiceImpl;
import mii.bsi.apiportal.service.LogService;
import mii.bsi.apiportal.utils.DateUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class TryApiService {

    @Autowired
    private BsmApiConfigServiceImpl configService;

    @Autowired
    private DataApiClient dataApiClient;
    @Autowired
    private LogService logService;
    @Autowired
    private DateUtils dateUtils;

    public ResponseApiGw tryServiceApi(String request, RequestHeaderApi signatureHeader,
                                       RequestHeaderApi serviceHeader, String serviceName){
        try {

            ResponseApiGw signatureResponse = signatureService(signatureHeader, request, serviceName);
            if(signatureResponse.getStatusCode() != 200){

            }
            JSONObject responseBody = new JSONObject(signatureResponse.getResponseBody());
            serviceHeader.setSignature(responseBody.getString("Signature"));
            serviceHeader.setTimestamp(dateUtils.dateIsoString(new Date()));
//            System.out.println(serviceHeader);
            ResponseApiGw serviceResponse = serviceApi(serviceHeader, request);
            System.out.println("Service API Status Code: "+ serviceResponse.getStatusCode());
            if(serviceResponse.getStatusCode() != 200){

            }
            return serviceResponse;

        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    public ResponseApiGw tryOutServiceApi(RequestHeaderApi requestHeader, String requestBody){
        BsmApiConfig hostApiGw = configService.getConfigByKeyName("base.url.apigw");
        BsmApiConfig endPoint = configService.getConfigByKeyName("apigw.serviceapi");

        String url = hostApiGw.getValue() + endPoint.getValue()+"/"+requestHeader.getEndPointUrl();
        ResponseApiGw responseApiGw = null;
        try {
            responseApiGw = dataApiClient.balanceInquiry(url, requestHeader, requestBody);
            return responseApiGw;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public ResponseApiGw serviceApi(RequestHeaderApi requestHeader, String requestBody){
        BsmApiConfig hostApiGw = configService.getConfigByKeyName("base.url.apigw");
        String url = hostApiGw.getValue() + requestHeader.getPath();
        ResponseApiGw balanceInquiry = null;
        try {
            balanceInquiry = dataApiClient.balanceInquiry(url, requestHeader, requestBody);
            return balanceInquiry;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public ResponseApiGw signatureService(RequestHeaderApi requestHeader, String requestBody, String serviceName){
        BsmApiConfig hostApiGw = configService.getConfigByKeyName("base.url.apigw");
        BsmApiConfig signaturePath = configService.getConfigByKeyName("apigw.signature.service.path");
        String url = hostApiGw.getValue() + signaturePath.getValue();
        ResponseApiGw signatureService = null;
        try {
            signatureService = dataApiClient.signatureService(url, requestHeader, requestBody);
            return signatureService;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
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
