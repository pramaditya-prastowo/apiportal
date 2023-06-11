package mii.bsi.apiportal.service;

import mii.bsi.apiportal.constant.StatusCode;
import mii.bsi.apiportal.domain.PortalNotification;
import mii.bsi.apiportal.domain.User;
import mii.bsi.apiportal.dto.NotificationResponseDTO;
import mii.bsi.apiportal.repository.PortalNotificationRepository;
import mii.bsi.apiportal.utils.RequestData;
import mii.bsi.apiportal.utils.ResponseHandling;
import mii.bsi.apiportal.validation.UserValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class PortalNotificationService {

    @Autowired
    private PortalNotificationRepository notificationRepository;
    @Autowired
    private UserValidation userValidation;
    @Autowired
    private LogService logService;

    public static final String GET_TEN = "Get Ten";
    public static final String GET_ALL = "Get All";
    public static final String SET_READ = "Set Read Notification";


    public ResponseEntity<ResponseHandling<List<NotificationResponseDTO>>> getTenNotification(String token){
        ResponseHandling<List<NotificationResponseDTO>> responseData = new ResponseHandling<>();
        RequestData requestData = new RequestData<>();

        try {
            User user = userValidation.getUserFromToken(token);
            if(user == null){
                responseData.failed("User not found");
                logService.saveLog(requestData, responseData, StatusCode.NOT_FOUND, this.getClass().getName(),
                        GET_TEN);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseData);
            }

            List<PortalNotification> notificationList = getPortalNotificationByLimit(10, user.getId());
            List<NotificationResponseDTO> responseDTOList = new ArrayList<>();
            for (PortalNotification notification: notificationList) {
                responseDTOList.add(new NotificationResponseDTO(notification));
            }

            responseData.success();
            responseData.setPayload(responseDTOList);

        }catch (Exception e){
            e.printStackTrace();
            responseData.failed(e.getMessage());
            logService.saveLog(requestData, responseData, StatusCode.INTERNAL_SERVER_ERROR, this.getClass().getName(),
                    GET_TEN);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseData);
        }
        return ResponseEntity.ok(responseData);
    }

    public ResponseEntity<ResponseHandling<List<NotificationResponseDTO>>> getAllNotification(String token){
        ResponseHandling<List<NotificationResponseDTO>> responseData = new ResponseHandling<>();
        RequestData requestData = new RequestData<>();

        try {
            User user = userValidation.getUserFromToken(token);
            if(user == null){
                responseData.failed("User not found");
                logService.saveLog(requestData, responseData, StatusCode.NOT_FOUND, this.getClass().getName(),
                        GET_ALL);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseData);
            }

            List<PortalNotification> notificationList = notificationRepository.findByIdUserAll(user.getId());
            List<NotificationResponseDTO> responseDTOList = new ArrayList<>();
            for (PortalNotification notification: notificationList) {
                responseDTOList.add(new NotificationResponseDTO(notification));
            }

            responseData.success();
            responseData.setPayload(responseDTOList);

        }catch (Exception e){
            e.printStackTrace();
            responseData.failed(e.getMessage());
            logService.saveLog(requestData, responseData, StatusCode.INTERNAL_SERVER_ERROR, this.getClass().getName(),
                    GET_ALL);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseData);
        }
        return ResponseEntity.ok(responseData);
    }

    public ResponseEntity<ResponseHandling> setReadNotification(String token, Long id){
        ResponseHandling responseData = new ResponseHandling<>();
        RequestData<Map<String,Object>> requestData = new RequestData<>();
        requestData.setPayload(logService.setValueRequest("id", id));

        try {
            User user = userValidation.getUserFromToken(token);
            if(user == null){
                responseData.failed("User not found");
                logService.saveLog(requestData, responseData, StatusCode.NOT_FOUND, this.getClass().getName(),
                        SET_READ);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseData);
            }

            PortalNotification notification = notificationRepository.findById(id).get();
            notification.setRead(true);
            notification.setUpdatedBy(user.getId());
            notification.setUpdatedDate(new Date());
            notificationRepository.save(notification);
            responseData.success();

        }catch (Exception e){
            e.printStackTrace();
            responseData.failed(e.getMessage());
            logService.saveLog(requestData, responseData, StatusCode.INTERNAL_SERVER_ERROR, this.getClass().getName(),
                    SET_READ);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseData);
        }
        logService.saveLog(requestData, responseData, StatusCode.OK, this.getClass().getName(),
                SET_READ);
        return ResponseEntity.ok(responseData);
    }


    public List<PortalNotification> getPortalNotificationByLimit(int limit, String userId){
        List<PortalNotification> notificationList = new ArrayList<>();
        notificationList = notificationRepository.findByIdUserByLimit(userId, limit);
        if(notificationList.size() > 0){
            return notificationList;
        }
        return new ArrayList<>();
    }
}
