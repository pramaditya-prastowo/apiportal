package mii.bsi.apiportal.apigw;

import mii.bsi.apiportal.apigw.model.DashboardType;
import mii.bsi.apiportal.apigw.model.RequestHeaderApi;
import mii.bsi.apiportal.apigw.model.ResponseApiGw;
import mii.bsi.apiportal.service.LogService;
import mii.bsi.apiportal.utils.DateUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Base64;

@Service
public class DataApiClientEsb {
    private DateUtils dateUtils = new DateUtils();
    @Autowired
    private LogService logService;

    private CloseableHttpClient httpClient;
    @Value("${esbsandbox.username}")
    private String esbsandboxUsername;
    @Value("${esbsandbox.password}")
    private String aesbsandboxPassword;
    @Value("${esbapi.username}")
    private String esbapiUsername;
    @Value("${esbapi.password}")
    private String esbapiPassword;

    public static final String INSERT_APP = "Insert App";
    public ResponseApiGw insertApplication(String url, RequestHeaderApi requestHeader, String requestBody) throws Exception {
        HttpPost request = new HttpPost(url);

        request.setHeader(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_JSON.getMimeType());
        request.setHeader(HttpHeaders.AUTHORIZATION, getAuthorizationEsbSandbox());

//        String requestJson = objectMapper.writeValueAsString(requestData);
        HttpEntity requestEntity = new StringEntity(requestBody, ContentType.APPLICATION_JSON);

        request.setEntity(requestEntity);

        try (CloseableHttpResponse response = httpClient.execute(request)) {
            HttpEntity responseEntity = response.getEntity();
            String responseData = EntityUtils.toString(responseEntity);
            EntityUtils.consume(responseEntity);
            System.out.println("Status Code : "+ response.getStatusLine().getStatusCode());
            System.out.println(responseData);
            System.out.println(response.getStatusLine().getStatusCode());
            logService.logApiGw(request.getAllHeaders(), logService.stringToJson(requestBody),
                    responseData,INSERT_APP,
                    String.valueOf(response.getStatusLine().getStatusCode()), url);
            ResponseApiGw responseApiGw = new ResponseApiGw(response.getStatusLine().getStatusCode(),responseData);
            return responseApiGw;
        }catch (Exception e){
            e.printStackTrace();
            logService.logApiGw(request.getAllHeaders(), logService.stringToJson(requestBody),
                    null,INSERT_APP,
                    "408", url);
            return new ResponseApiGw(408, "");
        }
    }

    public ResponseApiGw postDashboardData(String url, RequestHeaderApi requestHeaderApi, String requestBody, boolean isEsbApi, DashboardType dashboardType){

        HttpPost request = new HttpPost(url);
        request.setHeader(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_JSON.getMimeType());

        if(isEsbApi){
            request.setHeader(HttpHeaders.AUTHORIZATION, getAuthorizationEsbApi());
        }else{
            request.setHeader(HttpHeaders.AUTHORIZATION, getAuthorizationEsbSandbox());
        }
        HttpEntity requestEntity = new StringEntity(requestBody, ContentType.APPLICATION_JSON);
        request.setEntity(requestEntity);

        try (CloseableHttpResponse response = httpClient.execute(request)) {
            HttpEntity responseEntity = response.getEntity();
            String responseData = EntityUtils.toString(responseEntity);
            EntityUtils.consume(responseEntity);
            System.out.println("Status Code : "+ response.getStatusLine().getStatusCode());
            System.out.println(responseData);
            System.out.println(response.getStatusLine().getStatusCode());
            logService.logApiGw(request.getAllHeaders(), logService.stringToJson(requestBody),
                    responseData,dashboardType.toString(),
                    String.valueOf(response.getStatusLine().getStatusCode()), url);
            ResponseApiGw responseApiGw = new ResponseApiGw(response.getStatusLine().getStatusCode(),responseData);
            return responseApiGw;
        }catch (Exception e){
            e.printStackTrace();
            logService.logApiGw(request.getAllHeaders(), logService.stringToJson(requestBody),
                    null,dashboardType.toString(),
                    "408", url);
            return new ResponseApiGw(408, "");
        }

    }

    public ResponseApiGw getDashboardData(String url, RequestHeaderApi requestHeaderApi, String requestBody, boolean isEsbApi, DashboardType dashboardType){

        HttpGet request = new HttpGet(url);
        request.setHeader(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_JSON.getMimeType());

        if(isEsbApi){
            request.setHeader(HttpHeaders.AUTHORIZATION, getAuthorizationEsbApi());
        }else{
            request.setHeader(HttpHeaders.AUTHORIZATION, getAuthorizationEsbSandbox());
        }
//        HttpEntity requestEntity = new StringEntity(requestBody, ContentType.APPLICATION_JSON);
//        request.setEntity(requestEntity);

        try (CloseableHttpResponse response = httpClient.execute(request)) {
            HttpEntity responseEntity = response.getEntity();
            String responseData = EntityUtils.toString(responseEntity);
            EntityUtils.consume(responseEntity);
            System.out.println("Status Code : "+ response.getStatusLine().getStatusCode());
            System.out.println(responseData);
            System.out.println(response.getStatusLine().getStatusCode());
            logService.logApiGw(request.getAllHeaders(), logService.stringToJson(requestBody),
                    responseData,dashboardType.toString(),
                    String.valueOf(response.getStatusLine().getStatusCode()), url);
            ResponseApiGw responseApiGw = new ResponseApiGw(response.getStatusLine().getStatusCode(),responseData);
            return responseApiGw;
        }catch (Exception e){
            e.printStackTrace();
            logService.logApiGw(request.getAllHeaders(), logService.stringToJson(requestBody),
                    null,dashboardType.toString(),
                    "408", url);
            return new ResponseApiGw(408, "");
        }

    }



    private String getAuthorizationEsbSandbox(){
        String auth = esbsandboxUsername + ":" + aesbsandboxPassword;
        byte[] encodedAuth = Base64.getEncoder().encode(auth.getBytes());
        String authHeader = "Basic " + new String(encodedAuth);
        return authHeader;
    }
    private String getAuthorizationEsbApi(){
        String auth = esbapiUsername + ":" + esbapiPassword;
        byte[] encodedAuth = Base64.getEncoder().encode(auth.getBytes());
        String authHeader = "Basic " + new String(encodedAuth);
        return authHeader;
    }
}
