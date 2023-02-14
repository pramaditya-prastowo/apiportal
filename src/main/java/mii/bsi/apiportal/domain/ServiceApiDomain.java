package mii.bsi.apiportal.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "bsi_service_api_api_portal")
@Entity
public class ServiceApiDomain implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, unique = true)
    private Long id;
    @NotEmpty(message = "Service name is required")
    private String serviceName;
    @Lob
    @Type(type = "org.hibernate.type.TextType")
    private String serviceDescription;
    private String icon;
    private String swagger;
//    private String linkUrl;
    private String createBy;
    private boolean inActive;
    private String subtitle;
    private Long groupServiceApi;
    @Lob
    @Type(type = "org.hibernate.type.TextType")
    private String sampleDescription;
    private Date createDate;
    private String updateBy;
    private Date updateDate;
}
