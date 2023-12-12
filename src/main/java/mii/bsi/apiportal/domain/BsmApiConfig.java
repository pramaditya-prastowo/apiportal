package mii.bsi.apiportal.domain;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "bsm_api_config")
public class BsmApiConfig implements Serializable {

    @Id
    @NotEmpty(message = "Keyname is required")
    private String keyname;
    @NotEmpty(message = "Keygroup is required")
    private String keygroup;
    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @NotEmpty(message = "Value is required")
    private String value;
    @NotEmpty(message = "Description is required")
    private String description;
    private String cacheManager;
    private String cacheName;
    private String enabled;

    private Date createdDate;
    private String createdBy;
    private Date updatedDate;
    private String updatedBy;

    public void initData(String createdBy){
        Date createdDate = new Date();
        this.cacheManager = "APIPORTAL";
        this.enabled = "true";
        this.createdDate = createdDate;
        this.updatedDate = createdDate;
        this.createdBy = createdBy;
//        this.updatedBy = createdBy;
        this.cacheName = this.keyname.replaceAll(" ",".").replaceAll("\\.", "-");
    }

}
