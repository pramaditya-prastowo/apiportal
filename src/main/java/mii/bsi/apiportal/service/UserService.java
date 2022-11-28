package mii.bsi.apiportal.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.validation.Valid;

import mii.bsi.apiportal.constant.StatusCode;
import mii.bsi.apiportal.domain.BsiTokenVerification;
import mii.bsi.apiportal.domain.model.TokenVerificationType;
import mii.bsi.apiportal.repository.BsiTokenVerificationRepository;
import mii.bsi.apiportal.utils.CustomError;
import mii.bsi.apiportal.utils.EmailUtility;
import mii.bsi.apiportal.utils.RequestData;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;

import mii.bsi.apiportal.domain.User;
import mii.bsi.apiportal.repository.UserRepository;
import mii.bsi.apiportal.utils.ResponseHandling;

@Service
public class UserService {

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;



    @Autowired
    private LogService logService;

    public static final String REGISTER = "Register";
    public static final String EMAIL_VERIFICATION = "Email Verification";
    public static final String RESEND_EMAIL_VERIFICATION = "Resend Email Verification";
    public static final String DELETE_USER = "Delete User";

    @Autowired
    private BsiTokenVerificationRepository tokenRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailUtility emailUtility;

    public ResponseHandling<User> create(@Valid User user, Errors errors) {
        ResponseHandling<User> responseHandling = new ResponseHandling<>();
        System.out.println(user);
        try {
            String sequence = userRepository.getUserSequence();
            String pattern = "yyyyMMddHHmmss";
            SimpleDateFormat timestamp = new SimpleDateFormat(pattern);
            String date = timestamp.format(new Date());
            String idUser = date.concat(StringUtils.leftPad(sequence, 4, "0"));
            user.setId(idUser);
            user.setAccountInactive(false);
            user.setAccountLocked(false);
            user.setRetryPasswordCount(0);
            responseHandling.setPayload(userRepository.save(user));
            responseHandling.setResponseCode("00");
            responseHandling.setResponseMessage("success");
        } catch (Exception e) {
            errors.hasErrors();
            for (ObjectError err : errors.getAllErrors()) {
                responseHandling.getMessageError().add(err.getDefaultMessage());
            }
            responseHandling.setResponseCode("99");
            responseHandling.setResponseMessage("failed");
            responseHandling.setPayload(user);
        }
        return responseHandling;
    }

    public ResponseHandling<User> update(User user) {
        ResponseHandling<User> responseHandling = new ResponseHandling<>();
        try {
            String pattern = "yyyyMMddHHmmss";
            SimpleDateFormat timestamp = new SimpleDateFormat(pattern);
            String date = timestamp.format(new Date());
            user.getId();
            user.setUpdateDate(new Date());
            responseHandling.setPayload(userRepository.save(user));
            responseHandling.setResponseCode("00");
            responseHandling.setResponseMessage("success");
        } catch (Exception e) {
            responseHandling.setResponseCode("99");
            responseHandling.setResponseMessage("failed");
            responseHandling.setPayload(user);
        }
        return responseHandling;
    }

    public ResponseHandling<Iterable<User>> getAll() {
        ResponseHandling<Iterable<User>> responseHandling = new ResponseHandling<>();
        try {
            responseHandling.setPayload(userRepository.findAll());
            responseHandling.setResponseCode("00");
            responseHandling.setResponseMessage("success");
        } catch (Exception e) {
            responseHandling.setResponseMessage("failed");
            responseHandling.setResponseCode("99");
        }
        return responseHandling;
    }

    public ResponseHandling<User> getById(String id) {
        ResponseHandling<User> responseHandling = new ResponseHandling<>();
        try {
            responseHandling.setPayload(userRepository.findById(id).get());
            responseHandling.setResponseCode("00");
            responseHandling.setResponseMessage("success");
        } catch (Exception e) {
            responseHandling.setResponseCode("99");
            responseHandling.setResponseMessage("failed");
        }
        return responseHandling;
    }

    public ResponseEntity<ResponseHandling<User>> register(User user, Errors errors){
        ResponseHandling<User> responseData = new ResponseHandling<>();
        RequestData<User> requestData = new RequestData<>();
        requestData.setPayload(user);

        try {

            if(errors.hasErrors()){
                responseData.failed(CustomError.validRequest(errors), "Bad Request");
                logService.saveLog(requestData, responseData, StatusCode.BAD_REQUEST, this.getClass().getName(), REGISTER);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseData);
            }

            User userExist = userRepository.findByEmail(requestData.getPayload().getEmail());
            if(userExist != null){
                responseData.failed("Email is already register");
                logService.saveLog(requestData, responseData, StatusCode.CONFLICT, this.getClass().getName(), REGISTER);
                return ResponseEntity.status(HttpStatus.CONFLICT).body(responseData);
            }

            String sequence = userRepository.getUserSequence();
            user.generateCreated(sequence);
            user.setPassword(passwordEncoder.encode(user.getPassword()));

            BsiTokenVerification tokenVerification = new BsiTokenVerification();
            tokenVerification.generateToken();
            tokenVerification.setUser(user);
            tokenVerification.setIdToken(null);
            tokenVerification.setTokenType(TokenVerificationType.EMAIL_VERIFICATION);
            emailUtility.sendEmailVerification(user, tokenVerification.getToken());

            userRepository.save(user);
            tokenRepository.save(tokenVerification);
            responseData.success();

        }catch (Exception e){
            responseData.failed(e.getMessage());
            e.printStackTrace();
            logService.saveLog(requestData, responseData, StatusCode.INTERNAL_SERVER_ERROR, this.getClass().getName(), REGISTER);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseData);
        }
        logService.saveLog(requestData, responseData, StatusCode.CREATED, this.getClass().getName(), REGISTER);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseData);
    }

    public ResponseEntity<ResponseHandling> confirmEmailVerification(String token){
        ResponseHandling responseData = new ResponseHandling();
        RequestData<Map<String, Object>> requestData = new RequestData<>();
        Map<String, Object> request = new HashMap<>();
        request.put("token", token);
        requestData.setPayload(request);

        try {
            if(token.equals(null) || token.equals("")){
                responseData.failed("Token is required");
                logService.saveLog(requestData, responseData, StatusCode.BAD_REQUEST, this.getClass().getName(), EMAIL_VERIFICATION);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseData);
            }

            BsiTokenVerification resultToken = tokenRepository.findByToken(token);
            if(resultToken == null){
                responseData.failed("Token is not valid");
                logService.saveLog(requestData, responseData, StatusCode.BAD_REQUEST, this.getClass().getName(), EMAIL_VERIFICATION);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseData);
            }

            if(resultToken.isTokenExpired()){
                responseData.failed("Token is expired");
                logService.saveLog(requestData, responseData, StatusCode.GONE, this.getClass().getName(), EMAIL_VERIFICATION);
                return ResponseEntity.status(HttpStatus.GONE).body(responseData);
            }

            User user = userRepository.findByEmail(resultToken.getValidEmail());
            user.setEmailVerified(true);
            user.setEmailVerifiedDate(new Date());
            userRepository.save(user);
            responseData.success();
        }catch (Exception e){
            responseData.failed(e.getMessage());
            e.printStackTrace();
            logService.saveLog(requestData, responseData, StatusCode.INTERNAL_SERVER_ERROR, this.getClass().getName(), REGISTER);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseData);
        }
        logService.saveLog(requestData, responseData, StatusCode.OK, this.getClass().getName(), EMAIL_VERIFICATION);
        return ResponseEntity.ok(responseData);

    }

    public ResponseEntity<ResponseHandling> resendEmailVerification(String email){
        ResponseHandling responseData = new ResponseHandling();
        RequestData<Map<String, Object>> requestData = new RequestData<>();
        Map<String, Object> request = new HashMap<>();
        request.put("email", email);
        requestData.setPayload(request);

        try {
            if(email.equals(null) || email.equals("")){
                responseData.failed("Email is required");
                logService.saveLog(requestData, responseData, StatusCode.BAD_REQUEST, this.getClass().getName(), RESEND_EMAIL_VERIFICATION);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseData);
            }

            User user = userRepository.findByEmail(email);
            if(user == null){
                responseData.failed("Email is not register in system");
                logService.saveLog(requestData, responseData, StatusCode.NOT_FOUND, this.getClass().getName(), RESEND_EMAIL_VERIFICATION);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseData);
            }

            BsiTokenVerification tokenVerification = new BsiTokenVerification();
            tokenVerification.generateToken();
            tokenVerification.setUser(user);
            tokenVerification.setIdToken(null);
            tokenVerification.setTokenType(TokenVerificationType.EMAIL_VERIFICATION);
            emailUtility.sendEmailVerification(user, tokenVerification.getToken());

            tokenRepository.save(tokenVerification);
            responseData.success();
        }catch (Exception e){
            responseData.failed(e.getMessage());
            e.printStackTrace();
            logService.saveLog(requestData, responseData, StatusCode.INTERNAL_SERVER_ERROR, this.getClass().getName(), REGISTER);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseData);
        }
        logService.saveLog(requestData, responseData, StatusCode.CREATED, this.getClass().getName(), REGISTER);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseData);
    }

    public ResponseEntity<ResponseHandling> deleteUser(String idUser){
        ResponseHandling responseData = new ResponseHandling();
        RequestData<Map<String, Object>> requestData = new RequestData<>();
        Map<String, Object> request = new HashMap<>();
        request.put("id", idUser);
        requestData.setPayload(request);

        try {
            if(idUser.equals(null) || idUser.equals("")){
                responseData.failed("ID is required");
                logService.saveLog(requestData, responseData, StatusCode.BAD_REQUEST, this.getClass().getName(), DELETE_USER);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseData);
            }

            User user = userRepository.findById(idUser).orElse(null);
            if(user == null){
                responseData.failed("User not found");
                logService.saveLog(requestData, responseData, StatusCode.NOT_FOUND, this.getClass().getName(), DELETE_USER);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseData);
            }

            user.setAccountInactive(true);
            userRepository.save(user);
            responseData.success("User has been deleted");

        }catch (Exception e){
            responseData.failed(e.getMessage());
            e.printStackTrace();
            logService.saveLog(requestData, responseData, StatusCode.INTERNAL_SERVER_ERROR, this.getClass().getName(), DELETE_USER);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseData);
        }
        logService.saveLog(requestData, responseData, StatusCode.OK, this.getClass().getName(), DELETE_USER);
        return ResponseEntity.ok(responseData);
    }
}
