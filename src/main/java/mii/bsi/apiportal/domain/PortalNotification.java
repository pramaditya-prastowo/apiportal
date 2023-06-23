package mii.bsi.apiportal.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import mii.bsi.apiportal.constant.NofiticationType;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.Date;

@Table(name = PortalNotification.TABLE_NAME)
@Entity(name = PortalNotification.ENTITY_NAME)
@AllArgsConstructor
@NoArgsConstructor
@Data
public class PortalNotification {
    public static final String TABLE_NAME = "bsi_portal_notification";
    public static final String ENTITY_NAME = "PortalNotification";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_notification", unique = true, nullable = false)
    private Long id;

    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_id")
    private Menu menu;

    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_user")
    private User user;

    private String title;
    @Lob
    @Type(type = "org.hibernate.type.TextType")
    private String body;
    private String moduleName;
    @Lob
    @Type(type = "org.hibernate.type.TextType")
    private String endPoint;
    private boolean isRead = false;
    @Enumerated(EnumType.STRING)
    private NofiticationType notificationType;
    private Date createdDate;
    private String createdBy;
    private Date updatedDate;
    private String updatedBy;

}
