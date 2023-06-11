package mii.bsi.apiportal.utils;

import freemarker.template.TemplateException;
import mii.bsi.apiportal.constant.NofiticationType;
import mii.bsi.apiportal.domain.Menu;
import mii.bsi.apiportal.domain.PortalNotification;
import mii.bsi.apiportal.domain.SystemNotification;
import mii.bsi.apiportal.domain.User;
import mii.bsi.apiportal.domain.model.Roles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class TemplateNotification {

    @Autowired
    private EmailUtility emailUtility;

    public PortalNotification notificationKerjasama(User user,String body, NofiticationType nofiticationType, Menu menu, String entityId){
        PortalNotification portalNotification = new PortalNotification();
        portalNotification.setUser(user);
        portalNotification.setCreatedDate(new Date());
        portalNotification.setMenu(menu);
        portalNotification.setCreatedBy(user.getId());
        portalNotification.setModuleName("PengajuanKerjasamaMitra");
        if(user.getAuthPrincipal().equals(Roles.MITRA)){
            portalNotification.setTitle("Pengajuan Kerjasama");
            portalNotification.setEndPoint("/kerjasama");
        }else{
            portalNotification.setTitle("Permintaan Approve Kerjasama");
            portalNotification.setEndPoint("/my-task");
        }
        portalNotification.setNotificationType(nofiticationType);
        portalNotification.setBody(body);

        return portalNotification;
    }

    public SystemNotification systemNotificationKerjasama(User user,String subject, String body, String nofiticationType) throws TemplateException, MessagingException, IOException {
        SystemNotification systemNotification = new SystemNotification();
        systemNotification.setErrorCode("00");
        systemNotification.setNotifType(nofiticationType);
        systemNotification.setUserId(user.getId());
        systemNotification.setNotifDate(new Date());
        systemNotification.setMediaType("EMAIL");
        systemNotification.setName(user.getFullName());
        systemNotification.setMediaAddress(user.getEmail());
        systemNotification.setNotifSubject(subject);
        Map<String, Object> model = new HashMap<>();
        model.put("fullName", user.getFullName());
        model.put("userId", user.getId());
        model.put("content", body);
        String content = emailUtility.getContentFromTemplate(model, "id", "pengajuanKerjasamaMitra.vm");
        systemNotification.setNotifMessage(content);
        return systemNotification;
    }


}
