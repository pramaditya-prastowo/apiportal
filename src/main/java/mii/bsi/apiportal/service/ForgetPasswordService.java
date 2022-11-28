package mii.bsi.apiportal.service;

import mii.bsi.apiportal.constant.StatusCode;
import mii.bsi.apiportal.domain.BsiTokenVerification;
import mii.bsi.apiportal.domain.User;
import mii.bsi.apiportal.domain.model.TokenVerificationType;
import mii.bsi.apiportal.repository.BsiTokenVerificationRepository;
import mii.bsi.apiportal.repository.UserRepository;
import mii.bsi.apiportal.utils.EmailUtility;
import mii.bsi.apiportal.utils.RequestData;
import mii.bsi.apiportal.utils.ResponseHandling;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class ForgetPasswordService {

    @Autowired
    private LogService logService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailUtility emailUtility;

    @Autowired
    private BsiTokenVerificationRepository tokenRepository;

    public static String FORGET_PASSWORD = "Forget Password";
    public static String UPDATE_PASSWORD = "Update Password";

    public ResponseEntity<ResponseHandling> forgetPassword(String email){
        ResponseHandling responseData = new ResponseHandling<>();
        RequestData<Map<String, Object>> requestData = new RequestData<>();
        Map<String, Object> request = new HashMap<>();


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
            emailUtility.sendForgetPassword(user, tokenVerification.getToken());

            request.put("email", email);
            requestData.setPayload(request);

            tokenRepository.save(tokenVerification);
            responseData.success();

        }catch (Exception e){
            responseData.failed(e.getMessage());
            logService.saveLog(requestData, responseData, StatusCode.INTERNAL_SERVER_ERROR, this.getClass().getName(), FORGET_PASSWORD);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseData);
        }

        logService.saveLog(requestData, responseData, StatusCode.OK, this.getClass().getName(), FORGET_PASSWORD);
        return ResponseEntity.ok(responseData);

    }
}
