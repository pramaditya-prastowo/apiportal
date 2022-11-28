package mii.bsi.apiportal.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;

import lombok.AllArgsConstructor;
import lombok.Data;

@Table(name = "bsi_service_api_api_portal")
@Entity
@AllArgsConstructor
@Data
public class ServiceApi implements Serializable {
    @Id
    @Column(name = "id", updatable = false)
    private Long id;
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
