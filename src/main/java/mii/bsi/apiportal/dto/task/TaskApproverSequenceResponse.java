package mii.bsi.apiportal.dto.task;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskApproverSequenceResponse {
    private int sequence;
    private List<TaskApproverResponse> taskApprovers;
}
