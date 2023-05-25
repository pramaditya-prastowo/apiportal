package mii.bsi.apiportal.dto.task;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import mii.bsi.apiportal.domain.task.TaskApprover;
import mii.bsi.apiportal.domain.task.TaskMaker;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MyTaskResponseDTO {
    private List<TaskMakerResponse> taskMakers;
    private List<TaskApproverResponse> taskApprovers;
}
