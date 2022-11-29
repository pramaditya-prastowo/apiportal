package mii.bsi.apiportal.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "bsi_service_api_api_portal")
@Entity
public class ServiceApiDomain implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, unique = true)
    private String id;
    @NotEmpty(message = "Service name is required")
    private String serviceName;
    private String serviceDescription;
    private String idIcon;
    private String linkUrl;
    private String createBy;
    private Date createDate;
    private String updateBy;
    private Date updateDate;
}
