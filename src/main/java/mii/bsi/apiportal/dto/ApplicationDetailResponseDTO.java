package mii.bsi.apiportal.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import mii.bsi.apiportal.domain.Application;
import mii.bsi.apiportal.domain.ApplicationServiceApi;
import mii.bsi.apiportal.domain.BsmApiKey;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApplicationDetailResponseDTO {

    private Application application;
    private List<ApplicationServiceApi> listService;
    private BsmApiKey apiKey;
}
