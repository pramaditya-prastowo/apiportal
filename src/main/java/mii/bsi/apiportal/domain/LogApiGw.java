package mii.bsi.apiportal.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="bsi_log_api_gw")
public class LogApiGw {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "log_id", unique = true, nullable = false)
    private Long logId;
    private String userId;
    private String serviceName;
    private String statusCode;
    @Lob
    @Type(type = "org.hibernate.type.TextType")
    private String requestHeader;
    @Lob
    @Type(type = "org.hibernate.type.TextType")
    private String requestBody;
    @Lob
    @Type(type = "org.hibernate.type.TextType")
    private String responseBody;
    private Date createDate;
}
