package mii.bsi.apiportal.service;

import mii.bsi.apiportal.constant.StatusCode;
import mii.bsi.apiportal.domain.Groups;
import mii.bsi.apiportal.domain.Menu;
import mii.bsi.apiportal.domain.User;
import mii.bsi.apiportal.dto.AuthGuardPageRequestDTO;
import mii.bsi.apiportal.repository.GroupsRepository;
import mii.bsi.apiportal.repository.MenuRepository;
import mii.bsi.apiportal.repository.UserRepository;
import mii.bsi.apiportal.utils.CustomError;
import mii.bsi.apiportal.utils.JwtUtility;
import mii.bsi.apiportal.utils.RequestData;
import mii.bsi.apiportal.utils.ResponseHandling;
import mii.bsi.apiportal.validation.UserValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import java.util.*;

@Service
public class GroupsService {

    @Autowired
    private GroupsRepository groupsRepository;

    @Autowired
    private JwtUtility jwtUtility;
    @Autowired
    private LogService logService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private MenuRepository menuRepository;
    @Autowired
    UserValidation adminValidation;

    public static final String GET_ALL = "Get All";
    public static final String GET_BY_ID = "Get By ID";
    public static final String CREATE = "Create";
    public static final String UPDATE = "Update";
    public static final String VALIDATE_PAGE = "Validate Page";
    public static final String GET_MENU = "Get Menu By Group ID";




    public ResponseEntity<ResponseHandling<List<Groups>>> getAll(String token){
        ResponseHandling<List<Groups>> responseData = new ResponseHandling<>();

        try {

            if(!adminValidation.isAdmin(token)){
                responseData.failed("Access denied");
                responseData.setPayload(new ArrayList<>());
                logService.saveLog(new RequestData<>(), responseData, StatusCode.FORBIDDEN, this.getClass().getName(),
                        GET_ALL);
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(responseData);
            }

            List<Groups> list = groupsRepository.findByScope("ADMIN");
            System.out.println(list);
            responseData.setPayload(list);
            responseData.success();


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

    public ResponseEntity<ResponseHandling<Groups>> getById(long id, String token){
        ResponseHandling<Groups> responseData = new ResponseHandling<>();
        RequestData<Map<String, Object>> requestData = new RequestData<>();
        requestData.setPayload(logService.setValueRequest("id", id));

        try {
            if(!adminValidation.isAdmin(token)){
                responseData.failed("Access denied");
                logService.saveLog(requestData, responseData, StatusCode.FORBIDDEN, this.getClass().getName(),
                        GET_BY_ID);
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(responseData);
            }

            Groups group = groupsRepository.getReferenceById(id);
            if(group == null){
                responseData.failed("Not Found");
                logService.saveLog(requestData, responseData, StatusCode.NOT_FOUND, this.getClass().getName(),
                        GET_BY_ID);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseData);
            }
            responseData.success();
            responseData.setPayload(group);


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

    public ResponseEntity<ResponseHandling> createGroups(@Valid @RequestBody Groups groups,
                                                         Errors errors, String token){
        ResponseHandling responseData = new ResponseHandling();
        RequestData<Groups> requestData = new RequestData<>();
        requestData.setPayload(groups);

        try {

            if(!adminValidation.isAdmin(token)){
                responseData.failed("Access denied");
                logService.saveLog(requestData, responseData, StatusCode.FORBIDDEN, this.getClass().getName(),
                        CREATE);
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(responseData);
            }

            if(errors.hasErrors()){
                responseData.failed(CustomError.validRequest(errors), "Bad Request");
                logService.saveLog(requestData, responseData, StatusCode.BAD_REQUEST, this.getClass().getName(),
                        CREATE);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseData);
            }

            groups.setCreatedDate(new Date());
            groups.setCreatedBy(adminValidation.getUserFromToken(token).getId());
            groups.setScope("ADMIN");
            groupsRepository.save(groups);
            responseData.success();

        }catch (Exception e){
            e.printStackTrace();
            responseData.failed(e.getMessage());
            logService.saveLog(requestData, responseData, StatusCode.INTERNAL_SERVER_ERROR, this.getClass().getName(),
                    CREATE);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseData);
        }
        logService.saveLog(requestData, responseData, StatusCode.CREATED, this.getClass().getName(),
                CREATE);
        return ResponseEntity.ok(responseData);
    }

    public ResponseEntity<ResponseHandling<Boolean>> validatePage(String token, AuthGuardPageRequestDTO authGuard, Errors errors){
        ResponseHandling<Boolean> responseData = new ResponseHandling<>();
        RequestData<AuthGuardPageRequestDTO> requestData = new RequestData<>();
        requestData.setPayload(authGuard);
        try {
            if(!adminValidation.isAdmin(token)){
                responseData.failed("Access denied");
                responseData.setPayload(false);
//                logService.saveLog(requestData, responseData, StatusCode.OK, this.getClass().getName(),
//                        VALIDATE_PAGE);
                return ResponseEntity.status(HttpStatus.OK).body(responseData);
            }

            Menu menu = menuRepository.findOneByPermissionNameAndMenuId(authGuard.getPermissionName(), authGuard.getMenuId());
            Groups group = groupsRepository.getReferenceById(authGuard.getGroupId());

            if(!group.getPermission().contains(menu.getPermissionName())){
                responseData.failed("Access denied");
                responseData.setPayload(false);
//                logService.saveLog(requestData, responseData, StatusCode.OK, this.getClass().getName(),
//                        VALIDATE_PAGE);
                return ResponseEntity.status(HttpStatus.OK).body(responseData);
            }
            responseData.setPayload(true);
            responseData.success();

        }catch (Exception e){
            e.printStackTrace();
            responseData.failed(e.getMessage());
            responseData.setPayload(false);
//            logService.saveLog(requestData, responseData, StatusCode.INTERNAL_SERVER_ERROR, this.getClass().getName(),
//                    VALIDATE_PAGE);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseData);
        }
//        logService.saveLog(requestData, responseData, StatusCode.OK, this.getClass().getName(),
//                VALIDATE_PAGE);
        return ResponseEntity.ok(responseData);
    }

    public ResponseEntity<ResponseHandling<List<Menu>>> getMenuByGroupId(String token, Long groupId){
        ResponseHandling<List<Menu>> responseData = new ResponseHandling<>();
        RequestData<Map<String, Object>> requestData = new RequestData<>();
        requestData.setPayload(logService.setValueRequest("groupId", groupId));
        try {

            if(!adminValidation.isAdmin(token)){
                responseData.failed("Access denied");
                logService.saveLog(requestData, responseData, StatusCode.FORBIDDEN, this.getClass().getName(),
                        GET_MENU);
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(responseData);
            }

            if(groupId== null){
                responseData.failed("Not Found");
                logService.saveLog(requestData, responseData, StatusCode.NOT_FOUND, this.getClass().getName(),
                        GET_MENU);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseData);
            }

            Groups groups = groupsRepository.getReferenceById(groupId);
            if(groups == null){
                responseData.failed("Not Found");
                logService.saveLog(requestData, responseData, StatusCode.NOT_FOUND, this.getClass().getName(),
                        GET_MENU);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseData);
            }
            String permissionList = groups.getPermission();
            List<String> list = Arrays.asList(permissionList.split(","));
            Collection<String> collection = new ArrayList<String>(list);

            List<Menu> listMenu = menuRepository.findByPermissionNameIn(collection);
            responseData.success();
            responseData.setPayload(listMenu);


        }catch (Exception e){
            e.printStackTrace();
            responseData.failed(e.getMessage());
            logService.saveLog(requestData, responseData, StatusCode.INTERNAL_SERVER_ERROR, this.getClass().getName(),
                    GET_MENU);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseData);
        }

        logService.saveLog(requestData, responseData, StatusCode.OK, this.getClass().getName(),
                GET_MENU);
        return ResponseEntity.ok(responseData);
    }

    public ResponseEntity<ResponseHandling> updateGroup(String token, Groups groups){
        ResponseHandling responseData = new ResponseHandling();
        RequestData<Groups> requestData = new RequestData<>();
        requestData.setPayload(groups);

        try {
            if(!adminValidation.isAdmin(token)){
                responseData.failed("Access denied");
                responseData.setPayload(false);
                logService.saveLog(requestData, responseData, StatusCode.FORBIDDEN, this.getClass().getName(),
                        UPDATE);
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(responseData);
            }

            Groups groupDb = groupsRepository.getReferenceById(groups.getId());
            groupDb.setName(groups.getName());
            groupDb.setDescription(groups.getDescription());
            groupDb.setPermission(groups.getPermission());
            groupsRepository.save(groupDb);
            responseData.success();

        }catch (Exception e){
            e.printStackTrace();
            responseData.failed(e.getMessage());
            responseData.setPayload(false);
            logService.saveLog(requestData, responseData, StatusCode.INTERNAL_SERVER_ERROR, this.getClass().getName(),
                    UPDATE);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseData);
        }
        logService.saveLog(requestData, responseData, StatusCode.OK, this.getClass().getName(),
                UPDATE);
        return ResponseEntity.ok(responseData);
    }

    public ResponseEntity<ResponseHandling> deleteById(long id, String token){
        ResponseHandling<Groups> responseData = new ResponseHandling<>();
        RequestData<Map<String, Object>> requestData = new RequestData<>();
        requestData.setPayload(logService.setValueRequest("id", id));

        try {
            if(!adminValidation.isAdmin(token)){
                responseData.failed("Access denied");
                logService.saveLog(requestData, responseData, StatusCode.FORBIDDEN, this.getClass().getName(),
                        GET_BY_ID);
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(responseData);
            }

            Groups group = groupsRepository.getReferenceById(id);
            if(group == null){
                responseData.failed("Not Found");
                logService.saveLog(requestData, responseData, StatusCode.NOT_FOUND, this.getClass().getName(),
                        GET_BY_ID);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseData);
            }

            List<User> listUser = userRepository.findByGroupId(group.getId());
            if(listUser.size()!=0){
                responseData.failed("User Group masih digunakan");
                logService.saveLog(requestData, responseData, StatusCode.BAD_REQUEST, this.getClass().getName(),
                        GET_BY_ID);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseData);
            }

            groupsRepository.deleteById(id);
            responseData.success();


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
