package mii.bsi.apiportal.dto.task;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import mii.bsi.apiportal.domain.task.TaskMaker;

import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DetailTaskResponseDTO {
    private TaskMakerResponse taskMaker;
    private List<TaskApproverSequenceResponse> sequenceApprover;
}
