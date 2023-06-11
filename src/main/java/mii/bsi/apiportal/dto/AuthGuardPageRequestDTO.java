package mii.bsi.apiportal.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthGuardPageRequestDTO {
    @NotNull(message = "groupId is required")
    private Long groupId;
    @NotEmpty(message = "permissionName is required")
    private String permissionName;
}
