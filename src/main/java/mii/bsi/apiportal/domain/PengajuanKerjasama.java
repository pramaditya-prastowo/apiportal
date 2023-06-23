package mii.bsi.apiportal.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import mii.bsi.apiportal.domain.model.ApprovalStatus;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

@Table(name = PengajuanKerjasama.TABLE_NAME)
@Entity(name = PengajuanKerjasama.ENTITY_NAME)
@AllArgsConstructor
@NoArgsConstructor
@Data
public class PengajuanKerjasama {

    public static final String TABLE_NAME= "bsi_pengajuan_kerjasama_api_portal";
    public static final String ENTITY_NAME= "PengajuanKerjasama";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "peker_id", updatable = false, unique = true)
    private Long id;
    @NotEmpty(message = "Company Name tidak boleh kosong!")
    private String companyName;
    @NotEmpty(message = "Company Address tidak boleh kosong!")
    private String companyAddress;
    @NotEmpty(message = "Nama PIC 1 tidak boleh kosong!")
    private String pic1Name;
    @NotEmpty(message = "No. Telp PIC 1 tidak boleh kosong!")
    private String pic1Telp;
    @NotEmpty(message = "Email PIC 1 tidak boleh kosong!")
    private String pic1Email;
    @NotEmpty(message = "Nama PIC 2 tidak boleh kosong!")
    private String pic2Name;
    @NotEmpty(message = "No. Telp PIC 2 tidak boleh kosong!")
    private String pic2Telp;
    @NotEmpty(message = "EMail PIC 2 tidak boleh kosong!")
    private String pic2Email;
    @NotNull(message = "Employee tidak boleh kosong")
    private int employeeCount;
    private Date createdDate;
    private String createdBy;
    private Date updatedDate;
    private String updatedBy;
    @Enumerated(EnumType.STRING)
    private ApprovalStatus status = ApprovalStatus.MENUNGGU_PERSETUJUAN;

    //    private Long docId;
//    @Transient
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doc_id")
    private DocKerjasama docPengajuan;
    @Transient
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
//    @OneToMany(mappedBy = "id", fetch = FetchType.LAZY, orphanRemoval = false)
    private List<KerjasamaServiceApi> services;

    @Transient
    public void updateStatus(ApprovalStatus status, String updatedBy){
        this.status = status;
        this.updatedDate = new Date();
        this.updatedBy = updatedBy;
    }
}
