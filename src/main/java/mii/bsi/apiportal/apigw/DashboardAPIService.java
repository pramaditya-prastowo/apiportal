package mii.bsi.apiportal.apigw;

import mii.bsi.apiportal.apigw.model.ResponseApiGw;
import mii.bsi.apiportal.constant.StatusCode;
import mii.bsi.apiportal.domain.BsmApiConfig;
import mii.bsi.apiportal.service.BsmApiConfigServiceImpl;
import mii.bsi.apiportal.service.LogService;
import mii.bsi.apiportal.utils.DateUtils;
import mii.bsi.apiportal.utils.EncryptUtility;
import mii.bsi.apiportal.utils.RequestData;
import mii.bsi.apiportal.utils.ResponseHandling;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class DashboardAPIService {

    @Autowired
    private DataApiClient dataApiClient;
    @Autowired
    private LogService logService;
    @Autowired
    private DateUtils dateUtils;
    @Autowired
    private EncryptUtility encryptUtility;

    @Value("${apigw.totalservice.daily}")
    private String totalServiceDaily;
    @Value("${apigw.totalservice.bycorpid}")
    private String totalServiceByCorpId;
    @Value("${apigw.totalhitapi.bycorpid}")
    private String getTotalHitApiByCorpId;
    @Value("${apigw.detailhitservice.daily}")
    private String detailHitServiceDaily;
    @Value("${apigw.detailhitservice.monthly}")
    private String detailHitServiceMonthly;
    @Value("${apigw.totalmitra.perservice}")
    private String totalMitraPerService;
    @Value("${apigw.totalmitra.byapi}")
    private String getTotalMitraByApi;
    @Value("${apigw.totalhitapi.byservicename}")
    private String getTotalHitServiceByServiceName;
    @Value("${apigw.totalhitapi.monthly}")
    private String totalHitServiceMonthly;

    @Autowired
    private BsmApiConfigServiceImpl configService;

    public static final String TOTAL_SERVICE_DAILY = "Total Service Daily";
    public static final String TOTAL_SERVICE_BY_CORP_ID = "Total Service By Corp ID";
    public static final String TOTAL_HIT_API_BY_CORP_ID = "Detail Hit Service Daily";
    public static final String DETAIL_HIT_SERVICE_DAILY = "Detail Hit Service Daily";
    public static final String DETAIL_HIT_SERVICE_MONTHLY = "Detail Hit Service Daily";
    public static final String TOTAL_MITRA_PER_SERVICE = "Detail Hit Service Daily";
    public static final String TOTAL_MITRA_BY_API = "Detail Hit Service Daily";
    public static final String TOTAL_HIT_SERVICE_BY_SERVICE_NAME = "Detail Hit Service Daily";

    public static final String TOTAL_HIT_SERVICE_MONTHLY = "Detail Hit Service Daily";

    public ResponseEntity<ResponseHandling<String>> totalServiceDaily() {
        ResponseHandling<String> responseData = new ResponseHandling<>();

        BsmApiConfig hostApiGw = configService.getConfigByKeyName("base.url.apigw");
        String url = hostApiGw.getValue() + totalServiceDaily;
        System.out.println("URL : " + url);

        ResponseApiGw responseApiGw;
        try {
            responseApiGw = dataApiClient.getData(url);
            if(responseApiGw.getStatusCode() == 408){
                responseData.failed("Request Timeout");
                logService.saveLog(new RequestData<>(), responseData, StatusCode.REQUEST_TIMEOUT, this.getClass().getName(),
                        TOTAL_SERVICE_DAILY);
                return ResponseEntity.status(HttpStatus.REQUEST_TIMEOUT).body(responseData);
            }
            if(responseApiGw.getStatusCode() != 200){
                responseData.failed(responseApiGw.getResponseBody() + " - Failed");
                logService.saveLog(new RequestData<>(), responseData, StatusCode.OK, this.getClass().getName(),
                        TOTAL_SERVICE_DAILY);
                return ResponseEntity.status(HttpStatus.OK).body(responseData);
            }
            responseData.success();
            responseData.setPayload(responseApiGw.getResponseBody());
        }catch (Exception e){
            e.printStackTrace();
            if(e.getMessage().contains("timed out")){
                responseData.failed("Time Out");
                logService.saveLog(new RequestData<>(), responseData, StatusCode.REQUEST_TIMEOUT, this.getClass().getName(),
                        TOTAL_SERVICE_DAILY);
                return ResponseEntity.status(HttpStatus.REQUEST_TIMEOUT).body(responseData);
            }else{
                responseData.failed("Internal Server Error");
                logService.saveLog(new RequestData<>(), responseData, StatusCode.INTERNAL_SERVER_ERROR, this.getClass().getName(),
                        TOTAL_SERVICE_DAILY);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseData);
            }
        }
        return ResponseEntity.ok(responseData);
    }

    public ResponseEntity<ResponseHandling<String>> totalServiceByCorpId(String corpId){
        ResponseHandling<String> responseData = new ResponseHandling<>();
        BsmApiConfig hostApiGw = configService.getConfigByKeyName("base.url.apigw");
        String url = hostApiGw.getValue() + totalServiceByCorpId;
        System.out.println("URL : " + url);

        ResponseApiGw responseApiGw;
        try {
            responseApiGw = dataApiClient.postData(url,"{'corpId': "+ corpId+ "}");
            if(responseApiGw.getStatusCode() == 408){
                responseData.failed("Request Timeout");
                logService.saveLog(new RequestData<>(), responseData, StatusCode.OK, this.getClass().getName(),
                        TOTAL_SERVICE_BY_CORP_ID);
                return ResponseEntity.status(HttpStatus.OK).body(responseData);
            }
            if(responseApiGw.getStatusCode() != 200){
                responseData.failed(responseApiGw.getResponseBody() + " - Failed");
                logService.saveLog(new RequestData<>(), responseData, StatusCode.OK, this.getClass().getName(),
                        TOTAL_SERVICE_BY_CORP_ID);
                return ResponseEntity.status(HttpStatus.OK).body(responseData);
            }
            responseData.success();
            responseData.setPayload(responseApiGw.getResponseBody());
        }catch (Exception e){
            e.printStackTrace();
            if(e.getMessage().contains("timed out")){
                responseData.failed("Time Out");
                logService.saveLog(new RequestData<>(), responseData, StatusCode.REQUEST_TIMEOUT, this.getClass().getName(),
                        TOTAL_SERVICE_BY_CORP_ID);
                return ResponseEntity.status(HttpStatus.REQUEST_TIMEOUT).body(responseData);
            }else{
                responseData.failed("Internal Server Error");
                logService.saveLog(new RequestData<>(), responseData, StatusCode.INTERNAL_SERVER_ERROR, this.getClass().getName(),
                        TOTAL_SERVICE_BY_CORP_ID);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseData);
            }
        }
        return ResponseEntity.ok(responseData);
    }

    public ResponseEntity<ResponseHandling<String>> getTotalHitApiByCorpId(String corpId){
        ResponseHandling<String> responseData = new ResponseHandling<>();
        BsmApiConfig hostApiGw = configService.getConfigByKeyName("base.url.apigw");
        String url = hostApiGw.getValue() + getTotalHitApiByCorpId;
        System.out.println("URL : " + url);

        ResponseApiGw responseApiGw;
        try {
            responseApiGw = dataApiClient.postData(url,"{'corpId': "+ corpId+ "}");
            if(responseApiGw.getStatusCode() == 408){
                responseData.failed("Request Timeout");
                logService.saveLog(new RequestData<>(), responseData, StatusCode.REQUEST_TIMEOUT, this.getClass().getName(),
                        TOTAL_HIT_API_BY_CORP_ID);
                return ResponseEntity.status(HttpStatus.REQUEST_TIMEOUT).body(responseData);
            }
            if(responseApiGw.getStatusCode() != 200){
                responseData.failed(responseApiGw.getResponseBody() + " - Failed");
                logService.saveLog(new RequestData<>(), responseData, StatusCode.OK, this.getClass().getName(),
                        TOTAL_HIT_API_BY_CORP_ID);
                return ResponseEntity.status(HttpStatus.OK).body(responseData);
            }
            responseData.success();
            responseData.setPayload(responseApiGw.getResponseBody());
        }catch (Exception e){
            e.printStackTrace();
            if(e.getMessage().contains("timed out")){
                responseData.failed("Time Out");
                logService.saveLog(new RequestData<>(), responseData, StatusCode.REQUEST_TIMEOUT, this.getClass().getName(),
                        TOTAL_HIT_API_BY_CORP_ID);
                return ResponseEntity.status(HttpStatus.REQUEST_TIMEOUT).body(responseData);
            }else{
                responseData.failed("Internal Server Error");
                logService.saveLog(new RequestData<>(), responseData, StatusCode.INTERNAL_SERVER_ERROR, this.getClass().getName(),
                        TOTAL_HIT_API_BY_CORP_ID);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseData);
            }
        }
        return ResponseEntity.ok(responseData);
    }

    public ResponseEntity<ResponseHandling<String>> detailHitServiceDaily(){
        ResponseHandling<String> responseData = new ResponseHandling<>();
        BsmApiConfig hostApiGw = configService.getConfigByKeyName("base.url.apigw");
        String url = hostApiGw.getValue() + getTotalHitApiByCorpId;
        System.out.println("URL : " + url);

        ResponseApiGw responseApiGw;
        try {
            responseApiGw = dataApiClient.getData(url);
            if(responseApiGw.getStatusCode() == 408){
                responseData.failed("Request Timeout");
                logService.saveLog(new RequestData<>(), responseData, StatusCode.REQUEST_TIMEOUT, this.getClass().getName(),
                        DETAIL_HIT_SERVICE_DAILY);
                return ResponseEntity.status(HttpStatus.REQUEST_TIMEOUT).body(responseData);
            }
            if(responseApiGw.getStatusCode() != 200){
                responseData.failed(responseApiGw.getResponseBody() + " - Failed");
                logService.saveLog(new RequestData<>(), responseData, StatusCode.OK, this.getClass().getName(),
                        DETAIL_HIT_SERVICE_DAILY);
                return ResponseEntity.status(HttpStatus.OK).body(responseData);
            }
            responseData.success();
            responseData.setPayload(responseApiGw.getResponseBody());
        }catch (Exception e){
            e.printStackTrace();
            if(e.getMessage().contains("timed out")){
                responseData.failed("Time Out");
                logService.saveLog(new RequestData<>(), responseData, StatusCode.REQUEST_TIMEOUT, this.getClass().getName(),
                        DETAIL_HIT_SERVICE_DAILY);
                return ResponseEntity.status(HttpStatus.REQUEST_TIMEOUT).body(responseData);
            }else{
                responseData.failed("Internal Server Error");
                logService.saveLog(new RequestData<>(), responseData, StatusCode.INTERNAL_SERVER_ERROR, this.getClass().getName(),
                        DETAIL_HIT_SERVICE_DAILY);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseData);
            }
        }
        return ResponseEntity.ok(responseData);
    }

    public ResponseEntity<ResponseHandling<String>> detailHitServiceMonthly(String month){
        ResponseHandling<String> responseData = new ResponseHandling<>();
        BsmApiConfig hostApiGw = configService.getConfigByKeyName("base.url.apigw");
        String url = hostApiGw.getValue() + detailHitServiceMonthly;
        System.out.println("URL : " + url);

        ResponseApiGw responseApiGw;
        try {
            responseApiGw = dataApiClient.postData(url,"{'bulan': "+ month+ "}");
            if(responseApiGw.getStatusCode() == 408){
                responseData.failed("Request Timeout");
                logService.saveLog(new RequestData<>(), responseData, StatusCode.OK, this.getClass().getName(),
                        DETAIL_HIT_SERVICE_MONTHLY);
                return ResponseEntity.status(HttpStatus.OK).body(responseData);
            }
            if(responseApiGw.getStatusCode() != 200){
                responseData.failed(responseApiGw.getResponseBody() + " - Failed");
                logService.saveLog(new RequestData<>(), responseData, StatusCode.OK, this.getClass().getName(),
                        DETAIL_HIT_SERVICE_MONTHLY);
                return ResponseEntity.status(HttpStatus.OK).body(responseData);
            }
            responseData.success();
            responseData.setPayload(responseApiGw.getResponseBody());
        }catch (Exception e){
            e.printStackTrace();
            if(e.getMessage().contains("timed out")){
                responseData.failed("Time Out");
                logService.saveLog(new RequestData<>(), responseData, StatusCode.REQUEST_TIMEOUT, this.getClass().getName(),
                        DETAIL_HIT_SERVICE_MONTHLY);
                return ResponseEntity.status(HttpStatus.REQUEST_TIMEOUT).body(responseData);
            }else{
                responseData.failed("Internal Server Error");
                logService.saveLog(new RequestData<>(), responseData, StatusCode.INTERNAL_SERVER_ERROR, this.getClass().getName(),
                        DETAIL_HIT_SERVICE_MONTHLY);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseData);
            }
        }
        return ResponseEntity.ok(responseData);
    }

    public ResponseEntity<ResponseHandling<String>> totalMitraPerService(){
        ResponseHandling<String> responseData = new ResponseHandling<>();
        BsmApiConfig hostApiGw = configService.getConfigByKeyName("base.url.apigw");
        String url = hostApiGw.getValue() + totalMitraPerService;
        System.out.println("URL : " + url);

        ResponseApiGw responseApiGw;
        try {
            responseApiGw = dataApiClient.getData(url);
            if(responseApiGw.getStatusCode() == 408){
                responseData.failed("Request Timeout");
                logService.saveLog(new RequestData<>(), responseData, StatusCode.REQUEST_TIMEOUT, this.getClass().getName(),
                        TOTAL_MITRA_PER_SERVICE);
                return ResponseEntity.status(HttpStatus.REQUEST_TIMEOUT).body(responseData);
            }
            if(responseApiGw.getStatusCode() != 200){
                responseData.failed(responseApiGw.getResponseBody() + " - Failed");
                logService.saveLog(new RequestData<>(), responseData, StatusCode.OK, this.getClass().getName(),
                        TOTAL_MITRA_PER_SERVICE);
                return ResponseEntity.status(HttpStatus.OK).body(responseData);
            }
            responseData.success();
            responseData.setPayload(responseApiGw.getResponseBody());
        }catch (Exception e){
            e.printStackTrace();
            if(e.getMessage().contains("timed out")){
                responseData.failed("Time Out");
                logService.saveLog(new RequestData<>(), responseData, StatusCode.REQUEST_TIMEOUT, this.getClass().getName(),
                        TOTAL_MITRA_PER_SERVICE);
                return ResponseEntity.status(HttpStatus.REQUEST_TIMEOUT).body(responseData);
            }else{
                responseData.failed("Internal Server Error");
                logService.saveLog(new RequestData<>(), responseData, StatusCode.INTERNAL_SERVER_ERROR, this.getClass().getName(),
                        TOTAL_MITRA_PER_SERVICE);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseData);
            }
        }
        return ResponseEntity.ok(responseData);
    }

    public ResponseEntity<ResponseHandling<String>> getTotalMitraByApi(){
        ResponseHandling<String> responseData = new ResponseHandling<>();
        BsmApiConfig hostApiGw = configService.getConfigByKeyName("base.url.apigw");
        String url = hostApiGw.getValue() + getTotalMitraByApi;
        System.out.println("URL : " + url);

        ResponseApiGw responseApiGw;
        try {
            responseApiGw = dataApiClient.getData(url);
            if(responseApiGw.getStatusCode() == 408){
                responseData.failed("Request Timeout");
                logService.saveLog(new RequestData<>(), responseData, StatusCode.REQUEST_TIMEOUT, this.getClass().getName(),
                        TOTAL_MITRA_BY_API);
                return ResponseEntity.status(HttpStatus.REQUEST_TIMEOUT).body(responseData);
            }
            if(responseApiGw.getStatusCode() != 200){
                responseData.failed(responseApiGw.getResponseBody() + " - Failed");
                logService.saveLog(new RequestData<>(), responseData, StatusCode.OK, this.getClass().getName(),
                        TOTAL_MITRA_BY_API);
                return ResponseEntity.status(HttpStatus.OK).body(responseData);
            }
            responseData.success();
            responseData.setPayload(responseApiGw.getResponseBody());
        }catch (Exception e){
            e.printStackTrace();
            if(e.getMessage().contains("timed out")){
                responseData.failed("Time Out");
                logService.saveLog(new RequestData<>(), responseData, StatusCode.REQUEST_TIMEOUT, this.getClass().getName(),
                        TOTAL_MITRA_BY_API);
                return ResponseEntity.status(HttpStatus.REQUEST_TIMEOUT).body(responseData);
            }else{
                responseData.failed("Internal Server Error");
                logService.saveLog(new RequestData<>(), responseData, StatusCode.INTERNAL_SERVER_ERROR, this.getClass().getName(),
                        TOTAL_MITRA_BY_API);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseData);
            }
        }
        return ResponseEntity.ok(responseData);
    }
    public ResponseEntity<ResponseHandling<String>> getTotalHitServiceByServiceName(){
        ResponseHandling<String> responseData = new ResponseHandling<>();
        BsmApiConfig hostApiGw = configService.getConfigByKeyName("base.url.apigw");
        String url = hostApiGw.getValue() + getTotalHitServiceByServiceName;
        System.out.println("URL : " + url);

        ResponseApiGw responseApiGw;
        try {
            responseApiGw = dataApiClient.getData(url);
            if(responseApiGw.getStatusCode() == 408){
                responseData.failed("Request Timeout");
                logService.saveLog(new RequestData<>(), responseData, StatusCode.REQUEST_TIMEOUT, this.getClass().getName(),
                        TOTAL_HIT_SERVICE_BY_SERVICE_NAME);
                return ResponseEntity.status(HttpStatus.REQUEST_TIMEOUT).body(responseData);
            }
            if(responseApiGw.getStatusCode() != 200){
                responseData.failed(responseApiGw.getResponseBody() + " - Failed");
                logService.saveLog(new RequestData<>(), responseData, StatusCode.OK, this.getClass().getName(),
                        TOTAL_HIT_SERVICE_BY_SERVICE_NAME);
                return ResponseEntity.status(HttpStatus.OK).body(responseData);
            }
            responseData.success();
            responseData.setPayload(responseApiGw.getResponseBody());
        }catch (Exception e){
            e.printStackTrace();
            if(e.getMessage().contains("timed out")){
                responseData.failed("Time Out");
                logService.saveLog(new RequestData<>(), responseData, StatusCode.REQUEST_TIMEOUT, this.getClass().getName(),
                        TOTAL_HIT_SERVICE_BY_SERVICE_NAME);
                return ResponseEntity.status(HttpStatus.REQUEST_TIMEOUT).body(responseData);
            }else{
                responseData.failed("Internal Server Error");
                logService.saveLog(new RequestData<>(), responseData, StatusCode.INTERNAL_SERVER_ERROR, this.getClass().getName(),
                        TOTAL_HIT_SERVICE_BY_SERVICE_NAME);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseData);
            }
        }
        return ResponseEntity.ok(responseData);
    }
    public ResponseEntity<ResponseHandling<String>> totalHitServiceMonthly(String month){
        ResponseHandling<String> responseData = new ResponseHandling<>();
        BsmApiConfig hostApiGw = configService.getConfigByKeyName("base.url.apigw");
        String url = hostApiGw.getValue() + totalHitServiceMonthly;
        System.out.println("URL : " + url);

        ResponseApiGw responseApiGw;
        try {
            responseApiGw = dataApiClient.postData(url,"{'bulan': "+ month+ "}");
            if(responseApiGw.getStatusCode() == 408){
                responseData.failed("Request Timeout");
                logService.saveLog(new RequestData<>(), responseData, StatusCode.OK, this.getClass().getName(),
                        TOTAL_HIT_SERVICE_MONTHLY);
                return ResponseEntity.status(HttpStatus.OK).body(responseData);
            }
            if(responseApiGw.getStatusCode() != 200){
                responseData.failed(responseApiGw.getResponseBody() + " - Failed");
                logService.saveLog(new RequestData<>(), responseData, StatusCode.OK, this.getClass().getName(),
                        TOTAL_HIT_SERVICE_MONTHLY);
                return ResponseEntity.status(HttpStatus.OK).body(responseData);
            }
            responseData.success();
            responseData.setPayload(responseApiGw.getResponseBody());

        }catch (Exception e){
            e.printStackTrace();
            if(e.getMessage().contains("timed out")){
                responseData.failed("Time Out");
                logService.saveLog(new RequestData<>(), responseData, StatusCode.REQUEST_TIMEOUT, this.getClass().getName(),
                        TOTAL_HIT_SERVICE_MONTHLY);
                return ResponseEntity.status(HttpStatus.REQUEST_TIMEOUT).body(responseData);
            }else{
                responseData.failed("Internal Server Error");
                logService.saveLog(new RequestData<>(), responseData, StatusCode.INTERNAL_SERVER_ERROR, this.getClass().getName(),
                        TOTAL_HIT_SERVICE_MONTHLY);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseData);
            }
        }
        return ResponseEntity.ok(responseData);
    }
}
