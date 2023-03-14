package mii.bsi.apiportal.apigw.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseApiGw {
    private int statusCode;
    private String responseBody;
}
