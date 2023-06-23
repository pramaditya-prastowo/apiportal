package mii.bsi.apiportal.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import mii.bsi.apiportal.domain.model.ApprovalStatus;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApprovalKerjasamaRequest {
    private ApprovalStatus approvalStatus;
    private Long pekerId;
    private String activityId;
    private String message;

}
