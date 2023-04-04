package mii.bsi.apiportal.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.Date;

@Table(name = "bsi_doc_pengajuan_kerjasama_api_portal")
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class DocKerjasama implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "doc_id", updatable = false, unique = true)
    private Long id;
    @NotEmpty(message = "Surat Penyataan Pengajuan tidak boleh kosong!")
    private String srtPernyataanPengajuan;
    @NotEmpty(message = "Surate Keterangan Kemenkumham tidak boleh kosong!")
    private String srtKetKemenkumham;
    @NotEmpty(message = "Akta Pendirian dan Perubahan tidak boleh kosong!")
    private String aktaPendirianPerubahan;
    @NotEmpty(message = "Company Profile tidak boleh kosong!")
    private String companyProfile;
    @NotEmpty(message = "NPWP Perusahaan tidak boleh kosong!")
    private String companyNpwp;
    @NotEmpty(message = "Foto KTP Pengurus tidak boleh kosong!")
    private String fotoKtpPengurus;
    @NotEmpty(message = "Foto KTP Project Manager tidak boleh kosong!")
    private String fotoKtpPM;
    @NotEmpty(message = "SIUP tidak boleh kosong!")
    private String srtSiup;
    @NotEmpty(message = "NIB tidak boleh kosong!")
    private String nib;
    @NotEmpty(message = "No. Rekening tidak boleh kosong!")
    private String noRekPerusahaan;
    @NotEmpty(message = "hasil Sandbox tidak boleh kosong!")
    private String hasilSandbox;
    @NotEmpty(message = "Buku Tabungan tidak boleh kosong!")
    private String bukuTabungan;
    @NotEmpty(message = "Public Key tidak boleh kosong!")
    private String publicKey;

//    @ManyToOne
//    @JoinColumn(name="peker_id", nullable=false)
//    private PengajuanKerjasama pengajuanKerjasama;

    private Date createdDate;
    private String createdBy;
    private Date updatedDate;
    private String updatedBy;
}
