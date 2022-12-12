package mii.bsi.apiportal.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VerificationEmailRequest {
    @NotEmpty(message = "Token is required")
    private String token;

    @NotEmpty(message = "ID is Required")
    private String id;
}
