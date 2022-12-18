package mii.bsi.apiportal.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Table(name = "bsi_icon_api_portal")
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Icon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, unique = true)
    private Long id;

    private String iconGroup;
    private String iconName;
    private String filename;
    private String createBy;
    private Date createDate;

    @Transient
    public String getIconPath() {
        if (filename == null || id == null) return null;
        return "/assets/images/icon/" + iconGroup + "/" + filename;
    }
}
