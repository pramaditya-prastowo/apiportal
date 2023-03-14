package mii.bsi.apiportal.apigw;

import mii.bsi.apiportal.apigw.dto.CreateAppRequestDTO;
import mii.bsi.apiportal.apigw.model.RequestHeaderApi;
import mii.bsi.apiportal.apigw.model.ResponseApiGw;
import mii.bsi.apiportal.service.LogService;
import mii.bsi.apiportal.utils.DateUtils;
import org.apache.http.HttpHeaders;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.Date;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import com.fasterxml.jackson.databind.ObjectMapper;

public class DataApiClient {
//    private RestTemplate restTemplate;
//
//    public DataApiClient(RestTemplate restTemplate) {
//        this.restTemplate = restTemplate;
//    }
//
//    public String getData(String url) {
//        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
//        return response.getBody();
//    }



    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX");
    DateTimeFormatter outputFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssXXX");


    private DateUtils dateUtils = new DateUtils();
    @Autowired
    private LogService logService;

    private CloseableHttpClient httpClient;
    private final ObjectMapper objectMapper;

    public static final String REQUEST_TOKEN = "Request Token";
    public static final String SIGNATURE_SERVICE = "Signature Service";
    public static final String SERVICE_API = "Service API";

    public DataApiClient(CloseableHttpClient httpClient) {
        this.httpClient = httpClient;
        this.objectMapper = new ObjectMapper();
    }

    private String getAuthorization(){
        String username = "Administrator";
        String password = "manage";
        String auth = username + ":" + password;
        byte[] encodedAuth = Base64.getEncoder().encode(auth.getBytes());
        String authHeader = "Basic " + new String(encodedAuth);
        return authHeader;
    }

    public String getData(String url) throws IOException {
        HttpGet httpGet = new HttpGet(url);
        try (CloseableHttpResponse response = httpClient.execute(httpGet)) {
            HttpEntity entity = response.getEntity();
            return EntityUtils.toString(entity);
        }
    }

//    public String postData(String url, String requestBody) throws IOException {
//        HttpPost httpPost = new HttpPost(url);
//        httpPost.setHeader("Content-type", "application/json");
//        httpPost.setEntity(new StringEntity(requestBody));
//
//        try (CloseableHttpResponse response = httpClient.execute(httpPost)) {
//            HttpEntity entity = response.getEntity();
//            return EntityUtils.toString(entity);
//        }
//    }

    public String postData(String url, CreateAppRequestDTO requestData) throws Exception {
        HttpPost request = new HttpPost(url);

        request.setHeader(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_JSON.getMimeType());
        request.setHeader(HttpHeaders.AUTHORIZATION, getAuthorization());

        String requestJson = objectMapper.writeValueAsString(requestData);
        HttpEntity requestEntity = new StringEntity(requestJson, ContentType.APPLICATION_JSON);

        request.setEntity(requestEntity);

        try (CloseableHttpResponse response = httpClient.execute(request)) {
            HttpEntity responseEntity = response.getEntity();
            String responseData = EntityUtils.toString(responseEntity);
            EntityUtils.consume(responseEntity);
            return responseData;
        }
    }

    public String signatureAuth(String url){
        HttpPost request = new HttpPost(url);

        String formattedDate = new Date().toInstant().atZone(ZoneOffset.UTC).format(outputFormat);
        // Menambahkan header "Content-Type: application/json"
        request.setHeader(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_JSON.getMimeType());
        request.setHeader(HttpHeaders.AUTHORIZATION, getAuthorization());
        request.setHeader("X-CLIENT-KEY", "5ca21839-0cd6-4094-b55a-2d53994bfbb3");
        request.setHeader("Private_Key", "MIIEvwIBADANBgkqhkiG9w0BAQEFAASCBKkwggSlAgEAAoIBAQCggUTfA0dsY0Vg5f8Fzhenfxq3E6gbLp19lGLKSoFZYVflRncLJv/WzG8fJdS7AKq8vjzaAHymJoDJjVRUtmAIH1brBSYc1H9kAMkVdyGH+vnVHIVN/tEbbZg9BKgdpXR6LQDkD2rxwLsJ+OpYf10Z8nTWQ0lpew0aWvJIyBrbevo3AQ1shrytSYq5J8+Llek6ZxcpxtmE2uB0yy7/REhuMyTTBE61JXptkcLHpJkAG90DJvPZnAGaA/rK5M1Zb/AgIHI/VxmctcJgL6xGQ35wNXvG1WI6tvIEojVJ8V4QBU+/DEeeAFCbU0LNFpHA8nsKE7HU850FbnBCxiO+GNMVAgMBAAECggEAWrTTZCkpOxLbCYjRV8mByrHlOiOMtFfivy6VqfbzJL0DfFoXOqE+oniEMBkkIM6eru3f29+8kfVegQky6HDs0opLh6QwRsi8eZqMCWp686sdd5eXql1gdVy5VXqFO8PekJFQWJJyAM/HpDoczgikZ96Csvfxy/+zhvpwxDr6GqB+7SYXGY5ff9EgPwRsap7utDxnuJm5pv/Npiq5GUHN84QyNIKa3rXeZb0cZrvEkjKvAdBZ6TaZAN3Mc3m3aXHfgDwm0ZhlBAr2V8+oauepPamO15Phy4xSV5xMdBmsZJ2lMb44ZUrOmTxNkjo9gZ6l+crFUERg1+FK9hstZpKlYQKBgQDTBU6C4oMnR7WD3kbPEGT1UrjCNwD0ODPsUmaSFcVb3+lPBINQ5BQF2z4pHU4AEJA4MyoMPthGv6XlFvgoSLtfEbq2O+G1+ErsWbSnG9qTnGoox70S4BhjVZt016oY+nIjg7LJXojo0qNvT62inRzvoNzFMFTfoQO8+Df3AOXkzQKBgQDCt3xyMo2YYaeg+JUD43ad2zQ9Cic8WeR1iMOhZetkigQKJz+Z7BNAcaZl8aQwgmVuzHCH2T7fahAGeUnnATMh/IhCcKHZlfGYxc4NcwNUnQSZu6ZF6obLbUgnGAcGuN7sOxrT9AjHjKuo1tFpNP0a0z8Zvl6TLAONtv1vD73naQKBgQCq0ufsomtjl/RD7ONLak0gHzf72MUH7ptx2n64EbGznz5iPhgDmq7u0r2uUM+806u8IwcN5K32D9y+6Go5Si+MVXDdZvpf8cDNNg09HhpCVmPF4XOY3RpBB6MR4igLkmplf45y3vlb6HBvgoPgWOl8vq8ZXffHFLRO/G1poit4nQKBgQCEQiOKYeIhpfs5cH+vQ3qXYIRMDbB24sw2NW5EG7lW8hQqXVxrDZpKBKg0yHxw4rFJIB4zeBGnqSA3dX0IJp13sVNQZbbZ12piDcGXCw8xEvJEBdy70sA6PwFqZHypSTtKFB915mVsPZV/umJFZtOgu+o5b0BIEPZc9PWR0Yx5OQKBgQCZG2h96il0o9LTAVlyQEzmncMoQgJsSPbY3HB0NHopl+zIdYVmZeBLzGvG1PIr57AbCjZXqwexhdJ1E2b6HmaKMZ2xvOR+rnjTW4pEYLZ++xwMJUmLHXqQHT2sTOa3JHEY+qnCAzSfEyxzBHqLC3nqfjKoNZUOzN7RbCT6LZzmww==");
        request.setHeader("X-TIMESTAMP", formattedDate);

        // Membungkus objek DTO menjadi HttpEntity dengan tipe "application/json"
        String requestJson = "{}";
        HttpEntity requestEntity = new StringEntity(requestJson, ContentType.APPLICATION_JSON);

        // Menambahkan request body ke HttpPost request
        request.setEntity(requestEntity);

        // Mengirim request ke server dan menerima responsenya
        try (CloseableHttpResponse response = httpClient.execute(request)) {
            System.out.println(response.getStatusLine().getStatusCode());
            HttpEntity responseEntity = response.getEntity();
            String responseData = EntityUtils.toString(responseEntity);
            EntityUtils.consume(responseEntity);
            return responseData;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public String signatureBpiInquiry(String url){
        HttpPost request = new HttpPost(url);
        String formattedDate = new Date().toInstant().atZone(ZoneOffset.UTC).format(outputFormat);

        // Menambahkan header "Content-Type: application/json"
        request.setHeader(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_JSON.getMimeType());
        request.setHeader(HttpHeaders.AUTHORIZATION, getAuthorization());
        request.setHeader("X-CLIENT-SECRET", "m4nd1r1sy4r14hku");
        request.setHeader("AccesToken", "eyJraWQiOiJzc29zIiwiYWxnIjoiUlM1MTIifQ.eyJhdWQiOiJldmVyeW9uZSIsImlzcyI6IkJJU05BUCIsIkNsaWVudF9JZCI6ImQ2NzQzMmY0LWY3ODAtNDY1ZC1hMmFmLWJjZDI1NjMwMGY1OSIsImV4cCI6MTY3MTQ4MjUzMSwiaWF0IjoxNjcxNDgxNjMxLCJzdWIiOiJlZjdmNGQzMC1mMGYyLTQ0MDQtYTUzNC0xZjY0YjM1YzU0MTQifQ.Lc8G1Ma5ZjPreHJmT3-B3ZvcnRa-yx4IpAYrr4u12jbz1c-kFt-BsrJmMP0Vuo6dYV8VL0aBxHzz0UifQk4G6Q26YQb7kE5A8NLzDDPZhAoGy8MsJZVruMPXcn83TX8tdyhX8rTA62LYNRkDGgV7R7G7n_ZahG94uLHVPZ_VsbpdFRvAPn_eO_CfmLa4p-9DuKcVFjH0dqeEBESjxWK50xDCaKH6BbU9pphXRxonhgX6lDXeTljAs0ZKZQ5RnEG7Rcn4G2Z5P7ga1tpdqZi-bycZCQPzXswIy6EsHmiKVe_fqFpjopC3_reeAmdmuWqcvVQk7jJjDjpXk_9kHAqKuA");
        request.setHeader("X-TIMESTAMP", formattedDate);
        request.setHeader("EndpointUrl", "/BpiApiSnap/resources/snap/inquiry");

        // Membungkus objek DTO menjadi HttpEntity dengan tipe "application/json"
        String requestJson = "{\n" +
                "    \"requestBody\" :\"{\\\"partnerServiceId\\\":\\\"889\\\",\\\"customerNo\\\":\\\"12345678901234\\\",\\\"trxDateInit\\\":\\\"23-08-2019T10:09:23\\\",\\\"amount\\\":{\\\"value\\\":\\\"12345678.00\\\",\\\"currency\\\":\\\"IDR\\\"},\\\"virtualAccountNo\\\":\\\"2110199457\\\",\\\"inquiryRequestId\\\":\\\"123456701\\\",\\\"additionalInfo\\\":{\\\"billerId\\\":\\\"5050999999123401\\\"}}\"\n" +
                "}";
        HttpEntity requestEntity = new StringEntity(requestJson, ContentType.APPLICATION_JSON);

        // Menambahkan request body ke HttpPost request
        request.setEntity(requestEntity);

        // Mengirim request ke server dan menerima responsenya
        try (CloseableHttpResponse response = httpClient.execute(request)) {
            HttpEntity responseEntity = response.getEntity();
            String responseData = EntityUtils.toString(responseEntity);
            EntityUtils.consume(responseEntity);
            return responseData;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

//    public String

    public ResponseApiGw requestToken(String url, RequestHeaderApi requestHeader){
        HttpPost request = new HttpPost(url);

        request.setHeader(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_JSON.getMimeType());
//        request.setHeader(HttpHeaders.AUTHORIZATION, getAuthorization());
        request.setHeader("X-CLIENT-KEY", requestHeader.getClientKey());
        request.setHeader("X-SIGNATURE", requestHeader.getSignature());
        request.setHeader("X-TIMESTAMP", requestHeader.getTimestamp());

        String requestJson = "{\n" +
                "    \"grant_type\": \"client_credentials\"\n" +
                "}";
        HttpEntity requestEntity = new StringEntity(requestJson, ContentType.APPLICATION_JSON);
        request.setEntity(requestEntity);

        try (CloseableHttpResponse response = httpClient.execute(request)) {
            HttpEntity responseEntity = response.getEntity();
            String responseData = EntityUtils.toString(responseEntity);
            EntityUtils.consume(responseEntity);
            System.out.println("Status Code : "+ response.getStatusLine().getStatusCode());
            System.out.println(responseData);
            System.out.println(response.getStatusLine().getStatusCode());
            logService.logApiGw(request.getAllHeaders(), logService.stringToJson(requestJson),
                    responseData,REQUEST_TOKEN,
                    String.valueOf(response.getStatusLine().getStatusCode()), url);
            ResponseApiGw responseApiGw = new ResponseApiGw(response.getStatusLine().getStatusCode(),responseData);

            return responseApiGw;
        }catch (Exception e){
            e.printStackTrace();
            logService.logApiGw(request.getAllHeaders(), logService.stringToJson(requestJson),
                    null,REQUEST_TOKEN,
                    "408", url);
            return new ResponseApiGw(408, "");
        }
    }

    public ResponseApiGw signatureService(String url, RequestHeaderApi requestHeader, String requestBody){
        HttpPost request = new HttpPost(url);

        request.setHeader(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_JSON.getMimeType());
//        request.setHeader(HttpHeaders.AUTHORIZATION, "Bearer "+ requestHeader.getBearerToken());
        request.setHeader("X-CLIENT-SECRET", "m4nd1r1sy4r14hku");
        request.setHeader("X-TIMESTAMP", requestHeader.getTimestamp());
        request.setHeader("AccesToken", requestHeader.getBearerToken());
        request.setHeader("EndpointUrl", requestHeader.getPath());

        HttpEntity requestEntity = new StringEntity(requestBody, ContentType.APPLICATION_JSON);

        request.setEntity(requestEntity);

        try (CloseableHttpResponse response = httpClient.execute(request)) {
            HttpEntity responseEntity = response.getEntity();
            String responseData = EntityUtils.toString(responseEntity);
            EntityUtils.consume(responseEntity);

            logService.logApiGw(request.getAllHeaders(), logService.stringToJson(requestBody),
                    responseData,SIGNATURE_SERVICE,
                    String.valueOf(response.getStatusLine().getStatusCode()), url);

            ResponseApiGw responseApiGw = new ResponseApiGw(response.getStatusLine().getStatusCode(), responseData);

            return responseApiGw;
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseApiGw(500, null);
        }
    }

    public ResponseApiGw balanceInquiry(String url, RequestHeaderApi requestHeader, String requestBody){
        HttpPost request = new HttpPost(url);

        request.setHeader(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_JSON.getMimeType());
        request.setHeader(HttpHeaders.AUTHORIZATION, "Bearer "+ requestHeader.getBearerToken());
        request.setHeader("X-SIGNATURE", requestHeader.getSignature());
        request.setHeader("X-TIMESTAMP", requestHeader.getTimestamp());
        request.setHeader("X-PARTNER-ID", requestHeader.getPartnerId());
        request.setHeader("CHANNEL-ID", requestHeader.getChannelId());
        request.setHeader("X-EXTERNAL-ID", requestHeader.getExternalId());

        HttpEntity requestEntity = new StringEntity(requestBody, ContentType.APPLICATION_JSON);

        request.setEntity(requestEntity);

        try (CloseableHttpResponse response = httpClient.execute(request)) {
            HttpEntity responseEntity = response.getEntity();
            String responseData = EntityUtils.toString(responseEntity);
            EntityUtils.consume(responseEntity);
            logService.logApiGw(request.getAllHeaders(), logService.stringToJson(requestBody),
                    responseData,SERVICE_API,
                    String.valueOf(response.getStatusLine().getStatusCode()), url);
            ResponseApiGw responseApiGw = new ResponseApiGw(response.getStatusLine().getStatusCode(), responseData);

            return responseApiGw;
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseApiGw(500, null);
        }
    }
}
