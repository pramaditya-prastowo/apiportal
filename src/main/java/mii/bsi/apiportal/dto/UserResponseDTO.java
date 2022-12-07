package mii.bsi.apiportal.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import mii.bsi.apiportal.domain.model.Roles;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserResponseDTO {
    private String id;
    private String firstName;
    private String lastName;
    private String email;
    private String corporateName;
    private boolean inActive;
    private boolean locked;
    private Roles role;
    private boolean emailVerified;
    private Date createDate;
}
