package mii.bsi.apiportal.domain.task;

import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ConstraintMode;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import mii.bsi.apiportal.domain.ApprovalMatrix;
import mii.bsi.apiportal.domain.User;
import org.hibernate.annotations.Type;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "bsi_task_maker")
public class TaskMaker extends Task {

    @Id
    private String activityId;

    private Integer currentApprovalLevel = Integer.valueOf(1);

//    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "approval_matrix")
    private ApprovalMatrix approvalMatrix;

//    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "maker")
    private User maker;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "old_data")
    private String oldData;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "new_data")
    private String newData;

    @OneToMany(mappedBy = "task", fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
    private Set<TaskApprovalHistory> taskApprovalHistory;

    @OneToMany(mappedBy = "taskMaker", fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
    private Set<TaskApprover> taskApproverList;

    public void addTaskApprovalHistory(TaskApprovalHistory history) {
        if (this.taskApprovalHistory == null)
            this.taskApprovalHistory = new HashSet<>();
        this.taskApprovalHistory.add(history);
        history.setTask(this);
    }

//    public void addTaskApprover(TaskApprover approver) {
//        if (this.taskApproverList == null)
//            this.taskApproverList = new HashSet<>();
//        this.taskApproverList.add(approver);
//        approver.setTaskMaker(this);
//    }
//
//    public Integer getCurrentApprovalLevel() {
//        return this.currentApprovalLevel;
//    }
//
//    public void setCurrentApprovalLevel(Integer currentApprovalLevel) {
//        this.currentApprovalLevel = currentApprovalLevel;
//    }
//
//    public ApprovalMatrix getApprovalMatrix() {
//        return this.approvalMatrix;
//    }
//
//    public void setApprovalMatrix(ApprovalMatrix approvalMatrix) {
//        this.approvalMatrix = approvalMatrix;
//    }
//
//    public User getMaker() {
//        return this.maker;
//    }
//
//    public void setMaker(User maker) {
//        this.maker = maker;
//    }
//
//    public Set<TaskApprovalHistory> getTaskApprovalHistory() {
//        return this.taskApprovalHistory;
//    }
//
//    public void setTaskApprovalHistory(Set<TaskApprovalHistory> taskApprovalHistory) {
//        this.taskApprovalHistory = taskApprovalHistory;
//    }
//
//    public boolean equals(Object obj) {
//        return EqualsBuilder.reflectionEquals(this, obj);
//    }
//
//    public Set<TaskApprover> getTaskApproverList() {
//        return this.taskApproverList;
//    }
//
//    public void setTaskApproverList(Set<TaskApprover> taskApproverList) {
//        this.taskApproverList = taskApproverList;
//    }
//
//    public String getActivityId() {
//        return this.activityId;
//    }
//
//    public void setActivityId(String activityId) {
//        this.activityId = activityId;
//    }
//
//    public byte[] getOldData() {
//        return this.oldData;
//    }
//
//    public void setOldData(byte[] oldData) {
//        this.oldData = oldData;
//    }
//
//    public byte[] getNewData() {
//        return this.newData;
//    }
//
//    public void setNewData(byte[] newData) {
//        this.newData = newData;
//    }
}