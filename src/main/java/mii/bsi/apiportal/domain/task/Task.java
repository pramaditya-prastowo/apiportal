package mii.bsi.apiportal.domain.task;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import mii.bsi.apiportal.domain.Menu;
import mii.bsi.apiportal.domain.model.ApprovalStatus;

import javax.persistence.*;
import java.util.Date;

@MappedSuperclass
@Data
public abstract class Task {
    private String activityName;

    private String description;

    private String entityName;

    private String entityId;

    private String serviceName;

    private String approvalToken;

    private Boolean isBulkTask = Boolean.valueOf(false);

    private String taskLabel;

    private String createdDateEmail;

    @Transient
    private String approvalStatusString;

    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_id")
    private Menu menu;

    @Enumerated(EnumType.STRING)
    private TaskType taskType;

    @Enumerated(EnumType.STRING)
    private ApprovalStatus approvalStatus;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;

    @Temporal(TemporalType.TIMESTAMP)
    private Date approveDate;
}
