package mii.bsi.apiportal.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "bsi_apps_api_portal")
public class Application implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, unique = true)
    private Long id;
    private String userId;
    private String applicationName;
    private String companyName;
    private String image;
    private String description;
    private String phone;
    private String email;
    private String corpId;
    private String clientId;
    private String clientSecret;
    private String clientKey;
    private Date createdDate;
    private String createdBy;
    private Date updatedDate;
    private String updatedBy;

    @Transient
    private List<Object> listService = new ArrayList<>();

}
