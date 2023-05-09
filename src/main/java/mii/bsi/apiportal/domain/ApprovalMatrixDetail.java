package mii.bsi.apiportal.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import mii.bsi.apiportal.domain.model.ApprovalGroupType;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "bsi_approval_matrix_details")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApprovalMatrixDetail {

    private static final long serialVersionUID = 7179112574165505996L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer sequence = Integer.valueOf(1);

    private Integer numberOfApprovalUser = Integer.valueOf(1);

    @Enumerated(EnumType.STRING)
    private ApprovalGroupType approvalGroupType = ApprovalGroupType.ANY_GROUP;
//    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "selected_group", nullable = true, foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT, name = "none"))
//    private Groups selectedGroup;
//    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "matrix_id", nullable = true, foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT, name = "none"))
//    private ApprovalMatrix matrix;
    private Long matrixId;

    @Transient
    private Integer numberOfUser;

    @Transient
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
//    @OneToMany(mappedBy = "id", fetch = FetchType.LAZY, orphanRemoval = false)
    private List<ApprovalGroup> selectedGroup;

//    @OneToMany(mappedBy = "matrix_detail", cascade = {CascadeType.ALL}, orphanRemoval = true, fetch = FetchType.LAZY)
//    private Set<ApprovalGroup> approvalGroups;
}
