package mii.bsi.apiportal.controller;

import mii.bsi.apiportal.domain.PortalNotification;
import mii.bsi.apiportal.dto.NotificationResponseDTO;
import mii.bsi.apiportal.service.PortalNotificationService;
import mii.bsi.apiportal.utils.ResponseHandling;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1.0/notification")
public class NotificationController {

    @Autowired
    private PortalNotificationService notificationService;

    @GetMapping("/home")
    public ResponseEntity<ResponseHandling<List<NotificationResponseDTO>>> getTenNotification(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String token){
        return notificationService.getTenNotification(token.substring(7));
    }

    @GetMapping
    public ResponseEntity<ResponseHandling<List<NotificationResponseDTO>>> getAllNotification(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String token){
        return notificationService.getAllNotification(token.substring(7));
    }

    @GetMapping("/read/{id}")
    public ResponseEntity<ResponseHandling> setReadNotification(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String token, @PathVariable Long id){
        return notificationService.setReadNotification(token.substring(7), id);
    }
}
