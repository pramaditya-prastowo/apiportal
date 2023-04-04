package mii.bsi.apiportal.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import mii.bsi.apiportal.domain.model.StatusKerjasama;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Table(name = "bsi_pengajuan_kerjasama_api_portal")
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class PengajuanKerjasama {

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
    private StatusKerjasama status = StatusKerjasama.MENUNGGU_PERSETUJUAN;

    //    private Long docId;
//    @Transient
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doc_id")
    private DocKerjasama docPengajuan;
    @Transient
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
//    @OneToMany(mappedBy = "id", fetch = FetchType.LAZY, orphanRemoval = false)
    private List<KerjasamaServiceApi> services;
}
