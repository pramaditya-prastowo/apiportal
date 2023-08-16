package mii.bsi.apiportal.service;

import java.util.*;

import io.jsonwebtoken.Claims;
import mii.bsi.apiportal.domain.GroupsServiceEntity;
import mii.bsi.apiportal.domain.User;
import mii.bsi.apiportal.domain.model.Roles;
import mii.bsi.apiportal.repository.GroupServiceRepository;
import mii.bsi.apiportal.repository.UserRepository;
import mii.bsi.apiportal.utils.JwtUtility;
import mii.bsi.apiportal.validation.UserValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;

import mii.bsi.apiportal.constant.StatusCode;
import mii.bsi.apiportal.domain.ServiceApiDomain;
import mii.bsi.apiportal.repository.ServiceApiRepository;
import mii.bsi.apiportal.utils.CustomError;
import mii.bsi.apiportal.utils.RequestData;
import mii.bsi.apiportal.utils.ResponseHandling;

@Service
public class ServiceApiService {
    @Autowired
    private LogService logService;

    @Autowired
    private ServiceApiRepository serviceApiRepository;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private GroupServiceRepository groupServiceRepository;
    @Autowired
    UserValidation adminValidation;

    @Autowired
    private JwtUtility jwtUtility;

    public static final String CREATE = "Create";
    public static final String UPDATE = "Update";
    public static final String GETALL = "Get All";
    public static final String GETBYID = "Get By Id";
    public static final String DELETE = "Delete";
    public static final String COUNT_SERVICE = "Count Service";

    public static final String GET_ALL_GROUP = "Get All Group";

    public ResponseEntity<ResponseHandling<ServiceApiDomain>> create(ServiceApiDomain serviceApi,String token, Errors errors) {
        ResponseHandling<ServiceApiDomain> responseData = new ResponseHandling<>();
        RequestData<ServiceApiDomain> requestData = new RequestData<>();
        requestData.setPayload(serviceApi);

        try {
            if (errors.hasErrors()) {
                responseData.failed(CustomError.validRequest(errors), "Bad Request");
                logService.saveLog(requestData, responseData, StatusCode.BAD_REQUEST, this.getClass().getName(),
                        CREATE);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseData);
            }
            final String username = jwtUtility.getUsernameFromToken(token);
            serviceApi.setCreateDate(new Date());
            serviceApi.setCreateBy(username.split("@")[0]);


            serviceApiRepository.save(serviceApi);
            responseData.success();
        } catch (Exception e) {
            responseData.failed(e.getMessage());
            logService.saveLog(requestData, responseData, StatusCode.INTERNAL_SERVER_ERROR, this.getClass().getName(),
                    CREATE);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseData);
        }
        logService.saveLog(requestData, responseData, StatusCode.CREATED, this.getClass().getName(), CREATE);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseData);
    }

    public ResponseEntity<ResponseHandling<ServiceApiDomain>> update(ServiceApiDomain serviceApi,String token, Errors errors) {
        ResponseHandling<ServiceApiDomain> responseData = new ResponseHandling<>();
        RequestData<ServiceApiDomain> requestData = new RequestData<>();
        requestData.setPayload(serviceApi);

        try {

            if(!adminValidation.isAdmin(token)){
                responseData.failed("Access denied");
                logService.saveLog(requestData, responseData, StatusCode.FORBIDDEN, this.getClass().getName(),
                        UPDATE);
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(responseData);
            }
            if (errors.hasErrors()) {
                responseData.failed(CustomError.validRequest(errors), "Bad Request");
                logService.saveLog(requestData, responseData, StatusCode.BAD_REQUEST, this.getClass().getName(),
                        UPDATE);
            }

            ServiceApiDomain serviceDb = serviceApiRepository.findByIdActive(serviceApi.getId());
            if(!serviceApi.getIcon().equals("")){
                serviceDb.setIcon(serviceApi.getIcon());
            }
            if(!serviceApi.getSwagger().equals("")){
                serviceDb.setSwagger(serviceApi.getSwagger());
            }
            serviceApi.getId();

            serviceDb.setUpdateDate(new Date());
            serviceDb.setUpdateBy("");
            serviceDb.setServiceName(serviceApi.getServiceName());
            serviceDb.setSubtitle(serviceApi.getSubtitle());
            serviceDb.setServiceDescription(serviceApi.getServiceDescription());
            serviceDb.setSampleDescription(serviceApi.getSampleDescription());
            serviceApiRepository.save(serviceDb);
            responseData.success();
        } catch (Exception e) {
            responseData.failed(e.getMessage());
            logService.saveLog(requestData, responseData, StatusCode.INTERNAL_SERVER_ERROR, this.getClass().getName(),
                    UPDATE);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseData);
        }
        logService.saveLog(requestData, responseData, StatusCode.OK, this.getClass().getName(), UPDATE);
        return ResponseEntity.status(HttpStatus.OK).body(responseData);
    }

    public ResponseEntity<ResponseHandling<List<ServiceApiDomain>>> getAll(String serviceType) {
        ResponseHandling<List<ServiceApiDomain>> responseData = new ResponseHandling<>();
        RequestData<ServiceApiDomain> requestData = new RequestData<>();
        try {
            List<ServiceApiDomain> listApi;
            if("".equals(serviceType)){
                listApi = serviceApiRepository.findByServiceApiActive();
            }else{
                listApi = serviceApiRepository.findByServiceApiActiveAndServiceType(serviceType);
            }
            responseData.setPayload(listApi);
            responseData.success();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseData);
        }
        logService.saveLog(requestData, responseData, StatusCode.OK, this.getClass().getName(), GETALL);
        return ResponseEntity.status(HttpStatus.OK).body(responseData);
    }

    public ResponseEntity<ResponseHandling<List<ServiceApiDomain>>> getServiceHome() {
        ResponseHandling<List<ServiceApiDomain>> responseData = new ResponseHandling<>();
        RequestData<ServiceApiDomain> requestData = new RequestData<>();
        try {
            List<ServiceApiDomain> listApi = serviceApiRepository.findByServiceApiActiveAndServiceType("SNAP");
            List<ServiceApiDomain> listApiHome = new ArrayList<>();
            if(listApi.size() > 6){
                for (int i = 0; i < 6; i++) {
                    listApiHome.add(listApi.get(i));
                }
            }else{
                listApiHome = listApi;
            }
            responseData.setPayload(listApiHome);
            responseData.success();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseData);
        }
        logService.saveLog(requestData, responseData, StatusCode.OK, this.getClass().getName(), GETALL);
        return ResponseEntity.status(HttpStatus.OK).body(responseData);
    }

    public ResponseEntity<ResponseHandling<List<GroupsServiceEntity>>> getAllGroupApi(String groupType){
        ResponseHandling<List<GroupsServiceEntity>> responseData = new ResponseHandling<>();
        RequestData requestData = new RequestData();

        try {
            List<GroupsServiceEntity> listGroup;
            if("".equals(groupType)){
                listGroup= groupServiceRepository.findAll();
            }else{
                listGroup= groupServiceRepository.findByGroupType(groupType);
            }
            for (GroupsServiceEntity data: listGroup) {

            }

            responseData.success();
            responseData.setPayload(listGroup);

        }catch (Exception e){
            responseData.failed(e.getMessage());
            logService.saveLog(requestData, responseData, StatusCode.INTERNAL_SERVER_ERROR, this.getClass().getName(),
                    GET_ALL_GROUP);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseData);
        }
        logService.saveLog(requestData, responseData, StatusCode.OK, this.getClass().getName(),
                GET_ALL_GROUP);
        return ResponseEntity.status(HttpStatus.OK).body(responseData);
    }

    public ResponseEntity<ResponseHandling<ServiceApiDomain>> getById(Long id) {
        ResponseHandling<ServiceApiDomain> responseData = new ResponseHandling<>();
        RequestData<ServiceApiDomain> requestData = new RequestData<>();
        try {
            responseData.setPayload(serviceApiRepository.findByIdActive(id));
            responseData.success();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseData);
        }
        logService.saveLog(requestData, responseData, StatusCode.OK, this.getClass().getName(), GETBYID);
        return ResponseEntity.status(HttpStatus.OK).body(responseData);
    }

    public ResponseEntity<ResponseHandling> deleteById(Long id, String token){
        ResponseHandling responseData = new ResponseHandling();
        RequestData<Map<String, Object>> requestData = new RequestData<>();
        Map<String, Object> request = new HashMap<>();
        request.put("id", id);
        requestData.setPayload(request);

        try {
            final String username = jwtUtility.getUsernameFromToken(token);
            final Claims claim = jwtUtility.getAllClaimsFromToken(token);
            if(!(claim.get("role").equals(Roles.SUPER_ADMIN.toString()) || claim.get("role").equals(Roles.ADMIN.toString()))){
                responseData.failed("Access denied");
                logService.saveLog(requestData, responseData, StatusCode.FORBIDDEN, this.getClass().getName(), DELETE);
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(responseData);
            }

            User admin = userRepository.findByEmail(username);
            ServiceApiDomain apiDomain = serviceApiRepository.getReferenceById(id);
            System.out.println(apiDomain);
            apiDomain.setInActive(true);
            apiDomain.setUpdateBy(admin.getId());
            apiDomain.setUpdateDate(new Date());

            serviceApiRepository.save(apiDomain);
            responseData.success();

        }catch (Exception e){
            responseData.failed(e.getMessage());
            e.printStackTrace();
            logService.saveLog(requestData, responseData, StatusCode.INTERNAL_SERVER_ERROR, this.getClass().getName(),
                    DELETE);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseData);
        }
        logService.saveLog(requestData, responseData, StatusCode.OK, this.getClass().getName(), DELETE);
        return ResponseEntity.ok(responseData);
    }

    public ResponseEntity<ResponseHandling<Integer>> getCountServiceAPI(String token){
        ResponseHandling responseData = new ResponseHandling<>();
        try {
            final Claims claim = jwtUtility.getAllClaimsFromToken(token);
            claim.get("role");
            if(!(claim.get("role").equals(Roles.SUPER_ADMIN.toString()) || claim.get("role").equals(Roles.ADMIN.toString()))){
                responseData.failed("Access denied");
                logService.saveLog(new RequestData<>(), responseData, StatusCode.FORBIDDEN, this.getClass().getName(),
                        COUNT_SERVICE);
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(responseData);
            }

            long count = serviceApiRepository.countServiceApi("SNAP");
            responseData.setPayload(count);
            responseData.success();

        }catch (Exception e){
            e.printStackTrace();
            responseData.failed(e.getMessage());
            logService.saveLog(new RequestData<>(), responseData, StatusCode.INTERNAL_SERVER_ERROR, this.getClass().getName(),
                    COUNT_SERVICE);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseData);
        }
        logService.saveLog(new RequestData<>(), responseData, StatusCode.OK, this.getClass().getName(),
                COUNT_SERVICE);
        return ResponseEntity.ok(responseData);
    }
}
