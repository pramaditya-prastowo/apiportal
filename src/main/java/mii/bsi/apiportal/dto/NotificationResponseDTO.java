package mii.bsi.apiportal.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import mii.bsi.apiportal.constant.NofiticationType;
import mii.bsi.apiportal.domain.PortalNotification;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificationResponseDTO {
    private Long id;
    private String title;
    private String body;
    private String moduleName;
    private String endPoint;
    private NofiticationType nofiticationType;
    private Date createdDate;
    private boolean read;

    public NotificationResponseDTO(PortalNotification portalNotification){
        this.id = portalNotification.getId();
        this.title = portalNotification.getTitle();
        this.body = portalNotification.getBody();
        this.moduleName = portalNotification.getModuleName();
        this.endPoint = portalNotification.getEndPoint();
        this.nofiticationType = portalNotification.getNotificationType();
        this.createdDate = portalNotification.getCreatedDate();
        this.read = portalNotification.isRead();
    }

}
