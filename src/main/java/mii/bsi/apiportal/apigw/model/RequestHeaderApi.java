package mii.bsi.apiportal.apigw.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestHeaderApi {
    private String bearerToken;
    private String signature;
    private String timestamp;
    private String partnerId;
    private String externalId;
    private String channelId;
    private String clientKey;
    private String clientSecret;
    private String path;
    private String signaturePath;

}
