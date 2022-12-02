package mii.bsi.apiportal.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;

@Table(name = "bsi_promo_api_portal")
@Entity
@Data
@AllArgsConstructor
public class Promo implements Serializable {
    @Id
    @Column(name = "id", updatable = false, unique = true)
    private Long id;
    private String title;
    private String startPromo;
    private String endPromo;
    private String kodePromo;
    private String description;
    private String icon1;
    private String icon2;
    private String createBy;
    private Date createDate;
    private String updateBy;
    private Date updateDate;
}
