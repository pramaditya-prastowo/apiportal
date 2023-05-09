package mii.bsi.apiportal.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "bsi_approval_matrix")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApprovalMatrix {

    private static final long serialVersionUID = -8775040500515248078L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Boolean needApproval;
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_id", nullable = true, foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT, name = "none"))
    private Menu menu;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "company_id", nullable = true, foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT, name = "none"))
//    private Company company;
    @Transient
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
//    @OneToMany(mappedBy = "matrix", cascade = {CascadeType.ALL}, orphanRemoval = true, fetch = FetchType.LAZY)
//    @OneToMany(mappedBy = "matrix", fetch = FetchType.LAZY, orphanRemoval = false)
    private List<ApprovalMatrixDetail> details;

    private Date createdDate;
    private String createdBy;
    private Date updatedDate;
    private String updatedBy;

}
