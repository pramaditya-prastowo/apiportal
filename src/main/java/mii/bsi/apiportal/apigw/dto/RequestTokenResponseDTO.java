package mii.bsi.apiportal.apigw.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import mii.bsi.apiportal.domain.model.Token;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestTokenResponseDTO {
    private Token token;
}
