package mii.bsi.apiportal.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="bsi_log_api_portal")
public class BsiLogApiPortal {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "log_id", unique = true, nullable = false)
    private Long logId;
    private String userId;
    private String serviceName;
    private String actionType;
    private String statusCode;
    private String statusMessage;
    private String errorCode;
    private String request;
    private String response;
    private Date createDate;
}
