package mii.bsi.apiportal.apigw;

import mii.bsi.apiportal.apigw.model.RequestHeaderApi;
import mii.bsi.apiportal.apigw.model.ResponseApiGw;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/security")
public class SecurityController {

    @Autowired
    private SecurityService securityService;

    @PostMapping("/signature-auth")
    public String signatureAuth(@RequestHeader("X-CLIENT-KEY") String clientKey,
                                @RequestHeader("X-TIMESTAMP") String timestamp,
                                @RequestHeader("Private_Key") String privateKey){
        RequestHeaderApi requestHeader = new RequestHeaderApi();
        requestHeader.setClientKey(clientKey);
        requestHeader.setTimestamp(timestamp);
        requestHeader.setPrivateKey(privateKey);
        ResponseApiGw responseApiGw = securityService.signatureAuth(requestHeader, "{}");
        return responseApiGw.getResponseBody();
    }

    @PostMapping("/generate-token-b2b")
    public String generateTokenB2B(@RequestHeader("X-CLIENT-KEY") String clientKey,
                                   @RequestHeader("X-TIMESTAMP") String timestamp,
                                   @RequestHeader("X-SIGNATURE") String xSignature,
                                   @RequestBody String requestBody){
        RequestHeaderApi requestHeader = new RequestHeaderApi();
        requestHeader.setClientKey(clientKey);
        requestHeader.setTimestamp(timestamp);
        requestHeader.setSignature(xSignature);
        ResponseApiGw responseApiGw = securityService.generateTokenB2B(requestHeader, requestBody);
        return responseApiGw.getResponseBody();
    }

    @PostMapping("/signature-service-snap")
    public String signatureServiceSnap(@RequestHeader("X-CLIENT-SECRET") String clientSecret,
                                       @RequestHeader("AccesToken") String accessToken,
                                       @RequestHeader("X-TIMESTAMP") String timestamp,
                                       @RequestHeader("EndpointUrl") String endPointUrl,
                                       @RequestBody String requestBody){


        RequestHeaderApi requestHeader = new RequestHeaderApi();
        requestHeader.setClientSecret(clientSecret);
        requestHeader.setTimestamp(timestamp);
        requestHeader.setAccessToken(accessToken);
        requestHeader.setEndPointUrl(endPointUrl);
        ResponseApiGw responseApiGw = securityService.signatureServiceSnap(requestHeader, requestBody);
        return responseApiGw.getResponseBody();
    }
}
