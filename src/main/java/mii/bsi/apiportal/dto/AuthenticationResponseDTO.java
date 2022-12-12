package mii.bsi.apiportal.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import mii.bsi.apiportal.domain.User;
import mii.bsi.apiportal.domain.model.Roles;

import java.util.Date;

@Data
@AllArgsConstructor
public class AuthenticationResponseDTO {
    private String uid;
    private String email;
    private String firstName;
    private String lastName;
    private String corporateName;
    private String token;
    private Date expiredAt;

    private Date createDate;
    private boolean emailVerified;
    private Roles roles;

    public AuthenticationResponseDTO(User user, String token, Date expiredAt){
        this.uid = user.getId();
        this.email = user.getEmail();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.corporateName = user.getCorporateName();
        this.token = token;
        this.expiredAt = expiredAt;
        this.createDate = user.getCreateDate();
        this.emailVerified = user.isEmailVerified();
        this.roles = user.getAuthPrincipal();
    }
    
}
