package mii.bsi.apiportal.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import mii.bsi.apiportal.domain.ApprovalMatrixDetail;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddMatrixRequestDTO {
    private Long id;
    private String menuId;
    private List<DetailApprovalMatrixRequest> details;

}
