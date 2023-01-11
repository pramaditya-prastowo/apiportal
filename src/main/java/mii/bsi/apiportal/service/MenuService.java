package mii.bsi.apiportal.service;

import mii.bsi.apiportal.constant.StatusCode;
import mii.bsi.apiportal.domain.Menu;
import mii.bsi.apiportal.domain.model.Roles;
import mii.bsi.apiportal.repository.MenuRepository;
import mii.bsi.apiportal.utils.RequestData;
import mii.bsi.apiportal.utils.ResponseHandling;
import mii.bsi.apiportal.validation.UserValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MenuService {

    @Autowired
    private MenuRepository menuRepository;
    @Autowired
    private UserValidation adminValidation;
    @Autowired
    private LogService logService;
    public static final String GET_ALL = "Get All";
    public static final String GET_BY_ID = "Get By ID";

    public static final String CREATE = "Create";


    public ResponseEntity<ResponseHandling<List<Menu>>> getAll(String token){
        ResponseHandling<List<Menu>> responseData = new ResponseHandling<>();
        RequestData requestData = new RequestData();

        try {

            if(!adminValidation.isAdmin(token)){
                responseData.failed("Access denied");
                logService.saveLog(requestData, responseData, StatusCode.FORBIDDEN, this.getClass().getName(),
                        GET_ALL);
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(responseData);
            }

            List<Menu> list = menuRepository.findByRole(Roles.ADMIN);
            responseData.success();
            responseData.setPayload(list);

        }catch (Exception e){
            e.printStackTrace();
            responseData.failed(e.getMessage());
            logService.saveLog(new RequestData<>(), responseData, StatusCode.INTERNAL_SERVER_ERROR, this.getClass().getName(),
                    GET_ALL);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseData);
        }
        logService.saveLog(new RequestData<>(), responseData, StatusCode.OK, this.getClass().getName(),
                GET_ALL);
        return ResponseEntity.ok(responseData);

    }

    public ResponseEntity<ResponseHandling<Menu>> getById(Long id, String token){
        ResponseHandling<Menu> responseData = new ResponseHandling<>();
        RequestData requestData = new RequestData();

        try {

            if(!adminValidation.isAdmin(token)){
                responseData.failed("Access denied");
                logService.saveLog(requestData, responseData, StatusCode.FORBIDDEN, this.getClass().getName(),
                        GET_ALL);
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(responseData);
            }

            Menu menu = menuRepository.getReferenceById(id);
            if(menu == null){
                responseData.failed("Not Found");
                logService.saveLog(requestData, responseData, StatusCode.NOT_FOUND, this.getClass().getName(),
                        GET_BY_ID);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseData);
            }

            responseData.success();
            responseData.setPayload(menu);

        }catch (Exception e){
            e.printStackTrace();
            responseData.failed(e.getMessage());
            logService.saveLog(requestData, responseData, StatusCode.INTERNAL_SERVER_ERROR, this.getClass().getName(),
                    GET_BY_ID);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseData);
        }
        logService.saveLog(requestData, responseData, StatusCode.OK, this.getClass().getName(),
                GET_BY_ID);
        return ResponseEntity.ok(responseData);
    }
}
