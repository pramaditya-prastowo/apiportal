package mii.bsi.apiportal.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
@AllArgsConstructor
@Data
public class OTPEmailVerificationRequestDTO {
    @NotEmpty(message = "Email is required")
//    @Email(message = "Email is invalid")
    private String email;
    @NotEmpty(message = "Kode OTP is required")
    private String kodeOtp;
}
