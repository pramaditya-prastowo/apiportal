package mii.bsi.apiportal.dto.task;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import mii.bsi.apiportal.domain.model.ApprovalStatus;
import mii.bsi.apiportal.domain.model.Roles;
import mii.bsi.apiportal.domain.task.TaskMaker;
import mii.bsi.apiportal.domain.task.TaskType;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskMakerResponse {
    private String activityId;
    private String activityName;
    private ApprovalStatus approvalStatus;
    private TaskType taskType;
    private String levelStatus;
    private int currentApprovalLevel;
    private Roles roleMaker;
    private Long menuId;
    private String maker;
    private String makerId;
    private String path;
    private String newData;
    private String oldData;
    private String entityId;

    public TaskMakerResponse(TaskMaker taskMaker){
        this.activityId = taskMaker.getActivityId();
        this.activityName = taskMaker.getActivityName();
        this.approvalStatus = taskMaker.getApprovalStatus();
        this.taskType = taskMaker.getTaskType();
        this.currentApprovalLevel = taskMaker.getCurrentApprovalLevel();
        this.menuId = taskMaker.getMenu().getId();
        this.maker = taskMaker.getMaker().getFullName();
        this.levelStatus = "MAKER";
        this.roleMaker = taskMaker.getMaker().getAuthPrincipal();
        this.path = taskMaker.getMenu().getPath();
        this.newData = taskMaker.getNewData();
        this.oldData = taskMaker.getOldData();
        this.entityId = taskMaker.getEntityId();
        this.makerId = taskMaker.getMaker().getId();
    }
}
