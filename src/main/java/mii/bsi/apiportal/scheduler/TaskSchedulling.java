package mii.bsi.apiportal.scheduler;

import mii.bsi.apiportal.repository.SystemNotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Calendar;

@Component
public class TaskSchedulling {

    @Autowired
    private SystemNotificationRepository notificationRepository;

//    @Scheduled(initialDelay = 1000, fixedRate = 10000)
//    public void run() {
//        System.out.println("Current time is :: " + Calendar.getInstance().getTime());
//    }

//    @Scheduled(initialDelay = 1000, fixedRate = 10000)
//    public void sendEmailNotification(){
//
//    }


}
