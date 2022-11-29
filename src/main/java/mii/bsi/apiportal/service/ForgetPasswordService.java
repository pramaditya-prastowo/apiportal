package mii.bsi.apiportal.service;

import mii.bsi.apiportal.constant.Params;
import mii.bsi.apiportal.constant.StatusCode;
import mii.bsi.apiportal.domain.BsiTokenVerification;
import mii.bsi.apiportal.domain.User;
import mii.bsi.apiportal.domain.model.TokenVerificationType;
import mii.bsi.apiportal.dto.UpdatePasswordRequestDTO;
import mii.bsi.apiportal.repository.BsiTokenVerificationRepository;
import mii.bsi.apiportal.repository.UserRepository;
import mii.bsi.apiportal.utils.EmailUtility;
import mii.bsi.apiportal.utils.EncryptUtility;
import mii.bsi.apiportal.utils.RequestData;
import mii.bsi.apiportal.utils.ResponseHandling;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;

import java.util.*;

@Service
public class ForgetPasswordService {

    @Autowired
    private LogService logService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailUtility emailUtility;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private BsiTokenVerificationRepository tokenRepository;

    @Autowired
    private EncryptUtility encryptUtility;

    public static String FORGET_PASSWORD = "Forget Password";
    public static String UPDATE_PASSWORD = "Update Password";

    public ResponseEntity<ResponseHandling> forgetPassword(String email){
        ResponseHandling responseData = new ResponseHandling<>();
        RequestData<Map<String, Object>> requestData = new RequestData<>();
        Map<String, Object> request = new HashMap<>();
        request.put("email", email);
        requestData.setPayload(request);

        try {

            if(email == null){
                responseData.failed("Email is Required");
                logService.saveLog(requestData, responseData, StatusCode.BAD_REQUEST, this.getClass().getName(), FORGET_PASSWORD);
                return ResponseEntity.badRequest().body(responseData);
            }

            User user =  userRepository.findByEmail(email);

            if(user == null){
                responseData.failed("User not found");
                logService.saveLog(requestData, responseData, StatusCode.NOT_FOUND, this.getClass().getName(), FORGET_PASSWORD);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseData);
            }

            BsiTokenVerification tokenVerification = new BsiTokenVerification();
            tokenVerification.generateToken();

            tokenVerification.setUser(user);
            tokenVerification.setIdToken(null);
            tokenVerification.setTokenType(TokenVerificationType.FORGET_PASSWORD);
            emailUtility.sendForgetPassword(user, encryptUtility.encryptAES(tokenVerification.getToken(), Params.PASS_KEY));

            request.put("email", email);
            requestData.setPayload(request);

            tokenRepository.save(tokenVerification);
            responseData.success();

        }catch (Exception e){
            responseData.failed(e.getMessage());
            e.printStackTrace();
            logService.saveLog(requestData, responseData, StatusCode.INTERNAL_SERVER_ERROR, this.getClass().getName(), FORGET_PASSWORD);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseData);
        }

        logService.saveLog(requestData, responseData, StatusCode.OK, this.getClass().getName(), FORGET_PASSWORD);
        return ResponseEntity.ok(responseData);

    }

    public ResponseEntity<ResponseHandling> updatePassword(UpdatePasswordRequestDTO request, Errors errors){
        ResponseHandling responseData = new ResponseHandling();
        RequestData<UpdatePasswordRequestDTO> requestData = new RequestData<>();
        requestData.setPayload(request);

        try {

            if(errors.hasErrors()){
                List<String> errorList = new ArrayList<>();
                if(errors.hasErrors()){
                    for (ObjectError error : errors.getAllErrors()){
                        errorList.add(error.getDefaultMessage());
                    }
                }
                responseData.failed("Bad Request");
                responseData.setMessageError(errorList);
                logService.saveLog(requestData, responseData, StatusCode.BAD_REQUEST, this.getClass().getName(), UPDATE_PASSWORD);
                return ResponseEntity.badRequest().body(responseData);
            }

            final String decToken = encryptUtility.decryptAES(request.getToken(), Params.PASS_KEY);

            BsiTokenVerification resultToken = tokenRepository.findByToken(decToken);
            if(resultToken == null){
                responseData.failed("Token is not valid");
                logService.saveLog(requestData, responseData, StatusCode.BAD_REQUEST, this.getClass().getName(), UPDATE_PASSWORD);
                return ResponseEntity.badRequest().body(responseData);
            }

            if(resultToken.isTokenExpired()){
                responseData.failed("Token expired");
                logService.saveLog(requestData, responseData, StatusCode.GONE, this.getClass().getName(), UPDATE_PASSWORD);
                return ResponseEntity.status(HttpStatus.GONE).body(responseData);
            }

            User user = userRepository.findByEmail(resultToken.getValidEmail());
            if(user == null){
                responseData.failed("User not found");
                logService.saveLog(requestData, responseData, StatusCode.NOT_FOUND, this.getClass().getName(), UPDATE_PASSWORD);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseData);
            }

            user.setPassword(passwordEncoder.encode(request.getPassword()));
            user.setUpdateBy(user.getId());
            user.setUpdateDate(new Date());
            userRepository.save(user);
            responseData.success("Password has been changed");

        }catch (Exception e){
            responseData.failed(e.getMessage());
            e.printStackTrace();
            logService.saveLog(requestData, responseData, StatusCode.INTERNAL_SERVER_ERROR, this.getClass().getName(), UPDATE_PASSWORD);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseData);
        }
        logService.saveLog(requestData, responseData, StatusCode.OK, this.getClass().getName(), UPDATE_PASSWORD);
        return ResponseEntity.ok(responseData);
    }
}
