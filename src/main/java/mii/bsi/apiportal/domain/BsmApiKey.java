package mii.bsi.apiportal.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import mii.bsi.apiportal.apigw.dto.CreateAppRequestDTO;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "bsm_api_key")
public class BsmApiKey implements Serializable {
    @Id
    private String apiKey;
    private String corpId;
    private String appName;
    private String corpName;

    public BsmApiKey(CreateAppRequestDTO appRequestDTO) {
        this.apiKey = appRequestDTO.getApiKey();
        this.corpId = appRequestDTO.getCorpId();
        this.appName = appRequestDTO.getAppName();
        this.corpName = appRequestDTO.getCorpName();
    }
}
