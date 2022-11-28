package mii.bsi.apiportal.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import mii.bsi.apiportal.domain.User;

@Data
@AllArgsConstructor
public class AuthenticationResponseDTO {
    private String email;
    private String firstName;
    private String lastName;
    private String corporateName;
    private String token;
    private String expiredAt;
    private boolean emailVerified;

    public AuthenticationResponseDTO(User user, String token, String expiredAt){
        this.email = user.getEmail();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.corporateName = user.getCorporateName();
        this.token = token;
        this.expiredAt = expiredAt;
        this.emailVerified = user.isEmailVerified();
    }
}
