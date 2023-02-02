package mii.bsi.apiportal.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Table(name = "bsi_promo_api_portal")
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Promo implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, unique = true)
    private Long id;
    @NotEmpty(message = "Judul is required")
    private String title;
    private Date startPromo;
    private Date endPromo;
    @NotEmpty(message = "Kode Promo is required")
    private String kodePromo;
    @NotEmpty(message = "Deskripsi is required")
    private String description;
    @NotEmpty(message = "Banner 1 is required")
    private String icon1;
    @NotEmpty(message = "Banner 2 is required")
    private String icon2;
    private String createBy;
    private Date createDate;
    private String updateBy;
    private Date updateDate;
}
