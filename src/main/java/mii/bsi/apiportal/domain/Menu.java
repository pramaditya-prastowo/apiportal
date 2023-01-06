package mii.bsi.apiportal.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import mii.bsi.apiportal.domain.model.Roles;

import javax.persistence.*;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "bsi_menu_api_portal")
public class Menu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "menu_id", updatable = false, unique = true)
    private Long id;
    private String name;
    private String label;
    private String path;
    private String permissionName;
    private boolean enable;
    private boolean showOnApproval;
    private Date createdDate;
    private String createdBy;
    private Date updatedDate;
    private String updatedBy;
    @Column(columnDefinition = "ENUM('MITRA', 'ADMIN')", name = "role")
    @Enumerated(EnumType.STRING)
    private Roles role = Roles.MITRA;
    private boolean auth;
}
