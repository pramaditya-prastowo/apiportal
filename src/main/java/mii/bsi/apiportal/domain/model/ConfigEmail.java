package mii.bsi.apiportal.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import mii.bsi.apiportal.domain.BsmApiConfig;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConfigEmail {
    private BsmApiConfig host;
    private BsmApiConfig port;
    private BsmApiConfig username;
    private BsmApiConfig password;
}
