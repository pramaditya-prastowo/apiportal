package mii.bsi.apiportal.domain.task;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import mii.bsi.apiportal.domain.User;
import org.apache.commons.lang3.builder.EqualsBuilder;

import javax.persistence.ConstraintMode;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "bsi_task_approver")
public class TaskApprover extends Task implements Serializable {
    private static final long serialVersionUID = 6750550407595742097L;

    public TaskApprover(TaskMaker taskMaker) {
        super.setActivityName(taskMaker.getActivityName());
        super.setDescription(taskMaker.getDescription());
        super.setEntityName(taskMaker.getEntityName());
        super.setEntityId(taskMaker.getEntityId());
        super.setApprovalToken(taskMaker.getApprovalToken());
        super.setTaskLabel(taskMaker.getTaskLabel());
        super.setDescription(taskMaker.getDescription());
        super.setApprovalStatus(taskMaker.getApprovalStatus());
        super.setTaskType(taskMaker.getTaskType());

    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

//    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "approver")
    private User approver;

//    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "activity_id")
    private TaskMaker taskMaker;

    private Integer levelApprover;

    private String levelStatus;

    private Integer sequence;

    public User getApprover() {
        return this.approver;
    }

    public void setApprover(User approver) {
        this.approver = approver;
    }

    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj);
    }

    public String getActivityId() {
        if (this.taskMaker != null)
            return this.taskMaker.getActivityId();
        return null;
    }


}