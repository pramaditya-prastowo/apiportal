package mii.bsi.apiportal.domain.task;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import mii.bsi.apiportal.domain.User;
import mii.bsi.apiportal.domain.model.ApprovalGroupType;
import mii.bsi.apiportal.domain.model.ApprovalStatus;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.springframework.security.oauth2.provider.approval.Approval;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.persistence.ConstraintMode;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Data
@Table(name = "bsi_task_approval_history")
public class TaskApprovalHistory {
    private static final long serialVersionUID = -1948088045284815419L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "activity_id", nullable = true, foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT, name = "none"))
    private TaskMaker task;

    private Integer approvalSequence;

    @Enumerated(EnumType.STRING)
    private ApprovalStatus approvalStatus;

    @Temporal(TemporalType.TIMESTAMP)
    private Date creationDate;

    @Temporal(TemporalType.TIMESTAMP)
    private Date processDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "approver", nullable = true, foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT, name = "none"))
    private User approver;

    @Enumerated(EnumType.STRING)
    private ApprovalGroupType type;

    private Date createdDate;
    private String createdBy;
    private Date updatedDate;
    private String updatedBy;

    public String getProcessDateStr() {
        String prcDate = "";
        if (this.processDate != null) {
            DateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
            prcDate = df.format(this.processDate);
        }
        return prcDate;
    }

    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj);
    }

    public TaskApprovalHistory (int currentLevel, User approver, ApprovalStatus status, TaskMaker task, String createdBy){
        this.approvalSequence = currentLevel;
        this.approver = approver;
        this.approvalStatus = status;
        this.task = task;
        this.createdDate = new Date();
        this.processDate = new Date();
        this.createdBy = createdBy;
    }
}
