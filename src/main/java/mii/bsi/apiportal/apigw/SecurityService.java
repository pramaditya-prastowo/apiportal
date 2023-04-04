package mii.bsi.apiportal.apigw;

import mii.bsi.apiportal.apigw.model.RequestHeaderApi;
import mii.bsi.apiportal.apigw.model.ResponseApiGw;
import mii.bsi.apiportal.domain.BsmApiConfig;
import mii.bsi.apiportal.service.BsmApiConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SecurityService {
    @Autowired
    private BsmApiConfigService configService;
    @Autowired
    private DataApiClient dataApiClient;

    public ResponseApiGw signatureAuth(RequestHeaderApi requestHeader, String requestBody ){
        BsmApiConfig hostApiGw = configService.getConfigByKeyName("base.url.apigw");
        BsmApiConfig signatureAuthEndPoint = configService.getConfigByKeyName("apigw.signature-auth");

        String url = hostApiGw.getValue() + signatureAuthEndPoint.getValue();
        ResponseApiGw responseApiGw = null;
        try {
            responseApiGw = dataApiClient.signatureAuth(url, requestHeader, requestBody);
            return responseApiGw;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public ResponseApiGw generateTokenB2B(RequestHeaderApi requestHeader, String requestBody ){
        BsmApiConfig hostApiGw = configService.getConfigByKeyName("base.url.apigw");
        BsmApiConfig endPoint = configService.getConfigByKeyName("apigw.access-token");

        String url = hostApiGw.getValue() + endPoint.getValue();
        ResponseApiGw responseApiGw = null;
        try {
            responseApiGw = dataApiClient.requestToken(url, requestHeader, requestBody);
            return responseApiGw;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public ResponseApiGw signatureServiceSnap(RequestHeaderApi requestHeader, String requestBody ){
        BsmApiConfig hostApiGw = configService.getConfigByKeyName("base.url.apigw");
        BsmApiConfig endPoint = configService.getConfigByKeyName("apigw.signature.service.path");

        String url = hostApiGw.getValue() + endPoint.getValue();
        ResponseApiGw responseApiGw = null;
        try {
            responseApiGw = dataApiClient.signatureService(url, requestHeader, requestBody);
            return responseApiGw;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
