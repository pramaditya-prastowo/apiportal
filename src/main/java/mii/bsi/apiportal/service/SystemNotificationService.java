package mii.bsi.apiportal.service;

import mii.bsi.apiportal.repository.SystemNotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SystemNotificationService {

    @Autowired
    private SystemNotificationRepository notificationRepository;


}
