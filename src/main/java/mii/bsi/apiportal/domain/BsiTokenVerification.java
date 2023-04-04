package mii.bsi.apiportal.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import mii.bsi.apiportal.domain.model.TokenVerificationType;
import org.apache.commons.lang3.RandomStringUtils;

import javax.persistence.*;
import java.util.Date;

@Data
@AllArgsConstructor
@Entity
@Table(name = "bsi_token_verification_api_portal")
@NoArgsConstructor
public class BsiTokenVerification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_token", unique = true, nullable = false)
    private Long idToken;
    private String token;
    @Temporal(TemporalType.TIMESTAMP)
    private Date tokenCreateDate = new Date();
    private String validEmail;
    private String userId;
//    @Column(columnDefinition = "ENUM('FORGET_PASSWORD', 'EMAIL_VERIFICATION')")
    @Enumerated(EnumType.STRING)
    private TokenVerificationType tokenType;

    public void generateToken() {
        this.token = RandomStringUtils.randomAlphanumeric(20);
    }

    public void setUser(User user){
        this.validEmail = user.getEmail();
        this.userId = user.getId();
    }

    public boolean isTokenExpired() {
        long creation = this.tokenCreateDate.getTime();
        long now = (new Date()).getTime();
        long diffInMillis = Math.abs(now - creation);
        boolean moreThanDay = (diffInMillis > 129600000L);
        boolean moreThanThreeMinute = (diffInMillis > 180000L);
        if(TokenVerificationType.OTP_EMAIL_VERIFICATION.equals(tokenType) && moreThanThreeMinute){
            return true;
        }else{
            if (moreThanDay)
                return true;
            return false;
        }
    }
}
