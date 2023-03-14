package mii.bsi.apiportal.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@AllArgsConstructor
@Data
public class OTPEmailVerificationGenerateRequestDTO {
    @NotEmpty(message = "Email is required")
//    @Email(message = "Email is invalid")
    private String email;
    @NotEmpty(message = "Name is required")
    private String name;
}
