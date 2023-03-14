package mii.bsi.apiportal.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Token {

    private String accessToken;
    private String tokenType;
    private String expiresIn;
    private Date expiredAt;
    private String signature;
}
