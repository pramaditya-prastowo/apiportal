package mii.bsi.apiportal.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@Data
@AllArgsConstructor
public class UpdatePasswordRequestDTO {
    @NotEmpty(message = "Token is required")
    private String token;

    @NotEmpty(message = "Password is Required")
    private String password;
}
