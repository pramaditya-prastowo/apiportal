package mii.bsi.apiportal.dto.task;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import mii.bsi.apiportal.domain.model.ApprovalStatus;
import mii.bsi.apiportal.domain.model.Roles;
import mii.bsi.apiportal.domain.task.TaskApprover;
import mii.bsi.apiportal.domain.task.TaskType;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskApproverResponse {
    private Long id;
    private String activityName;
    private ApprovalStatus approvalStatus;
    private TaskType taskType;
    private int levelApprover;
    private String levelStatus;
    private int sequence;
    private Long menuId;
    private String approver;
    private String approverId;
    private String maker;
    private String makerId;
    private Roles roleMaker;
    private Roles auth;
    private String activityId;
    private String path;
    private String newData;
    private String oldData;
    private String entityId;
    private Date createdDate;

    public TaskApproverResponse(TaskApprover taskApprover){
        this.id = taskApprover.getId();
        this.activityName = taskApprover.getActivityName();
        this.approvalStatus = taskApprover.getApprovalStatus();
        this.taskType = taskApprover.getTaskType();
        this.levelApprover = taskApprover.getLevelApprover();
        this.levelStatus = taskApprover.getLevelStatus();
        this.sequence = taskApprover.getSequence();
        this.menuId = taskApprover.getMenu().getId();
        this.approver = taskApprover.getApprover().getFullName();
        this.maker = taskApprover.getTaskMaker().getMaker().getFullName();
        this.activityId = taskApprover.getActivityId();
        this.roleMaker = taskApprover.getTaskMaker().getMaker().getAuthPrincipal();
        this.path = taskApprover.getTaskMaker().getMenu().getPath();
        this.newData = taskApprover.getTaskMaker().getNewData();
        this.oldData = taskApprover.getTaskMaker().getOldData();
        this.entityId = taskApprover.getEntityId();
        this.makerId = taskApprover.getTaskMaker().getMaker().getId();
        this.approverId = taskApprover.getApprover().getId();
        this.createdDate = taskApprover.getCreatedDate();
    }
}
