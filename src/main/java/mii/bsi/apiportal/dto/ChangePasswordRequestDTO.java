package mii.bsi.apiportal.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChangePasswordRequestDTO {
    @NotEmpty(message = "Password Lama is required")
    private String oldPassword;
    @NotEmpty(message = "Password Baru is required")
    private String newPassword;
}
