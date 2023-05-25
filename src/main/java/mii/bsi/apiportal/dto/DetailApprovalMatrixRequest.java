package mii.bsi.apiportal.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import mii.bsi.apiportal.domain.ApprovalGroup;
import mii.bsi.apiportal.domain.Groups;
import mii.bsi.apiportal.domain.model.ApprovalGroupType;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Transient;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DetailApprovalMatrixRequest {
    private Long id;
    private Integer no = Integer.valueOf(1);

    private Integer count = Integer.valueOf(1);

    @Enumerated(EnumType.STRING)
    private ApprovalGroupType type = ApprovalGroupType.ANY_GROUP;
    private Long matrixId;
    private Integer numberOfUser;
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
//    @OneToMany(mappedBy = "id", fetch = FetchType.LAZY, orphanRemoval = false)
    private List<Groups> group;
}
