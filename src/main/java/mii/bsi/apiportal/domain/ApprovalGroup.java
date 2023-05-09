package mii.bsi.apiportal.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "bsi_approval_group")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApprovalGroup {

    private static final long serialVersionUID = 7179112574165505996L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long approvalGroupId;
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id")
    private Groups group;
    private Long matrixDetailId;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "matrix_detail_id", nullable = true, foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT, name = "none"))
//    private ApprovalMatrixDetail matrixDetail;
}
