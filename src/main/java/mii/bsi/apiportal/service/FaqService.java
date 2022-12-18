package mii.bsi.apiportal.service;

import io.jsonwebtoken.Claims;
import mii.bsi.apiportal.constant.StatusCode;
import mii.bsi.apiportal.domain.FAQ;
import mii.bsi.apiportal.domain.User;
import mii.bsi.apiportal.domain.model.Roles;
import mii.bsi.apiportal.repository.FaqRepository;
import mii.bsi.apiportal.repository.UserRepository;
import mii.bsi.apiportal.utils.CustomError;
import mii.bsi.apiportal.utils.JwtUtility;
import mii.bsi.apiportal.utils.RequestData;
import mii.bsi.apiportal.utils.ResponseHandling;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;

import java.util.*;

@Service
public class FaqService {

    @Autowired
    private LogService logService;

    @Autowired
    private FaqRepository faqRepository;

    @Autowired
    private JwtUtility jwtUtility;

    @Autowired
    private UserRepository userRepository;

    public static final String CREATE = "Create";
    public static final String GET = "Get Data";
    public static final String UPDATE = "Update Data";
    public static final String DELETE = "Delete Data";

    public ResponseEntity<ResponseHandling> create(FAQ faq,String token, Errors errors){
        ResponseHandling responseData = new ResponseHandling<>();
        RequestData<FAQ> requestData = new RequestData<>();
        requestData.setPayload(faq);

        try {

            final Claims claim = jwtUtility.getAllClaimsFromToken(token);
            claim.get("role");
            if(!(claim.get("role").equals(Roles.SUPER_ADMIN.toString()) || claim.get("role").equals(Roles.ADMIN.toString()))){
                responseData.failed("Access denied");
                logService.saveLog(requestData, responseData, StatusCode.FORBIDDEN, this.getClass().getName(),
                        CREATE);
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(responseData);
            }

            final String username = jwtUtility.getUsernameFromToken(token);
            User user = userRepository.findByEmail(username);

            if(!(user.getAuthPrincipal().equals(Roles.SUPER_ADMIN) || user.getAuthPrincipal().equals(Roles.ADMIN))){
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

            faq.setCreateDate(new Date());
            faq.setCreateBy(user.getEmail().split("@")[0]);
            faqRepository.save(faq);
            responseData.success();

        }catch (Exception e){
            responseData.failed(e.getMessage());
            e.printStackTrace();
            logService.saveLog(requestData, responseData, StatusCode.INTERNAL_SERVER_ERROR, this.getClass().getName(),
                    CREATE);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseData);
        }
        logService.saveLog(requestData, responseData, StatusCode.CREATED, this.getClass().getName(), CREATE);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseData);
    }

    public ResponseEntity<ResponseHandling> getFaq(){
        ResponseHandling responseData = new ResponseHandling();
        RequestData requestData = new RequestData();


        try {

            List<FAQ> faqList = faqRepository.findAll();
            System.out.println(faqList);
            responseData.success();
            responseData.setPayload(faqList);
            
        }catch (Exception e){
            responseData.failed(e.getMessage());
            e.printStackTrace();
            logService.saveLog(requestData, responseData, StatusCode.INTERNAL_SERVER_ERROR, this.getClass().getName(),
                    GET);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseData);
        }
        logService.saveLog(requestData, responseData, StatusCode.OK, this.getClass().getName(), GET);
        return ResponseEntity.ok(responseData);
    }

    public ResponseEntity<ResponseHandling> updateFaq(FAQ faq,String token, Errors errors){
        ResponseHandling responseData = new ResponseHandling();
        RequestData requestData = new RequestData();

        try {

            final Claims claim = jwtUtility.getAllClaimsFromToken(token);
            claim.get("role");
            if(!(claim.get("role").equals(Roles.SUPER_ADMIN.toString()) || claim.get("role").equals(Roles.ADMIN.toString()))){
                responseData.failed("Access denied");
                logService.saveLog(requestData, responseData, StatusCode.FORBIDDEN, this.getClass().getName(),
                        UPDATE);
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(responseData);
            }

            final String username = jwtUtility.getUsernameFromToken(token);
            User user = userRepository.findByEmail(username);

            if(!(user.getAuthPrincipal().equals(Roles.SUPER_ADMIN) || user.getAuthPrincipal().equals(Roles.ADMIN))){
                responseData.failed("Access denied");
                logService.saveLog(requestData, responseData, StatusCode.FORBIDDEN, this.getClass().getName(),
                        UPDATE);
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(responseData);
            }

            if(errors.hasErrors()){
                responseData.failed(CustomError.validRequest(errors), "Bad Request");
                logService.saveLog(requestData, responseData, StatusCode.BAD_REQUEST, this.getClass().getName(),
                        UPDATE);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseData);
            }

            faq.setCreateDate(new Date());
            faq.setCreateBy(user.getEmail().split("@")[0]);
            faqRepository.save(faq);
            responseData.success();


        }catch (Exception e){
            responseData.failed(e.getMessage());
            e.printStackTrace();
            logService.saveLog(requestData, responseData, StatusCode.INTERNAL_SERVER_ERROR, this.getClass().getName(),
                    UPDATE);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseData);
        }

        logService.saveLog(requestData, responseData, StatusCode.OK, this.getClass().getName(), UPDATE);
        return ResponseEntity.status(HttpStatus.OK).body(responseData);

    }

    public ResponseEntity<ResponseHandling> deleteFaq(String id, String token){
        ResponseHandling responseData = new ResponseHandling();
        RequestData requestData = new RequestData();
        Map<String, Object> request = new HashMap<>();
        request.put("id", id);
        requestData.setPayload(request);

        try {
            final Claims claim = jwtUtility.getAllClaimsFromToken(token);
            claim.get("role");
            if(!(claim.get("role").equals(Roles.SUPER_ADMIN.toString()) || claim.get("role").equals(Roles.ADMIN.toString()))){
                responseData.failed("Access denied");
                logService.saveLog(requestData, responseData, StatusCode.FORBIDDEN, this.getClass().getName(),
                        DELETE);
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(responseData);
            }

            final String username = jwtUtility.getUsernameFromToken(token);
            User user = userRepository.findByEmail(username);

            if(!(user.getAuthPrincipal().equals(Roles.SUPER_ADMIN) || user.getAuthPrincipal().equals(Roles.ADMIN))){
                responseData.failed("Access denied");
                logService.saveLog(requestData, responseData, StatusCode.FORBIDDEN, this.getClass().getName(),
                        DELETE);
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(responseData);
            }

            final FAQ faqResult = faqRepository.getOne(Long.parseLong(id));
            if(faqResult == null){
                responseData.failed("Data not found");
                logService.saveLog(requestData, responseData, StatusCode.NOT_FOUND, this.getClass().getName(),
                        DELETE);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseData);
            }

            faqRepository.delete(faqResult);
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
}
