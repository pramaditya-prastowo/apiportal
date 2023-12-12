package mii.bsi.apiportal.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import mii.bsi.apiportal.domain.model.ApprovalStatus;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "bsi_log_pengajuan_kerjasama")
public class LogPengajuanKerjasama {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "log_id", updatable = false, unique = true)
    private Long id;

    private Date createdDate;
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_user")
    private User user;
    private Long pekerId;
    @Lob
    @Type(type = "org.hibernate.type.TextType")
    private String message;
    @Lob
    @Type(type = "org.hibernate.type.TextType")
    private String description;
    @Enumerated(EnumType.STRING)
    private ApprovalStatus status;

    public LogPengajuanKerjasama (Long pekerId, User user, String message, String description, ApprovalStatus status){
//        LogPengajuanKerjasama logPengajuanKerjasama = new LogPengajuanKerjasama();
        this.pekerId = pekerId;
        this.user = user;
        this.createdDate = new Date();
        this.message = message;
        this.description = description;
        this.status = status;
    }
}
