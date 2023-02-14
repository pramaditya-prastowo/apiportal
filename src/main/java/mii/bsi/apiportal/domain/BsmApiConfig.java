package mii.bsi.apiportal.domain;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "bsm_api_config")
public class BsmApiConfig implements Serializable {

    @Id
//    @Column(name = "KEYNAME")
    private String keyname;
//    @Column(name = "KEYGROUP")
    private String keygroup;
//    @Column(name = "VALUE")
    private String value;
//    @Column(name = "DESCRIPTION")
    private String description;
//    @Column(name = "CHACHE_MANAGER")
    private String cacheManager;
//    @Column(name = "CACHE_NAME")
    private String cacheName;
//    @Column(name = "ENABLED")
    private String enabled;

}
