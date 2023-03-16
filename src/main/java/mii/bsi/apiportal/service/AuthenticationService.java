package mii.bsi.apiportal.service;

import mii.bsi.apiportal.constant.Params;
import mii.bsi.apiportal.constant.StatusCode;
import mii.bsi.apiportal.domain.BsiTokenVerification;
import mii.bsi.apiportal.domain.MyUserPrincipal;
import mii.bsi.apiportal.domain.User;
import mii.bsi.apiportal.domain.model.Roles;
import mii.bsi.apiportal.domain.model.TokenVerificationType;
import mii.bsi.apiportal.dto.AuthenticationRequestDTO;
import mii.bsi.apiportal.dto.AuthenticationResponseDTO;
import mii.bsi.apiportal.dto.OTPEmailVerificationGenerateRequestDTO;
import mii.bsi.apiportal.dto.OTPEmailVerificationRequestDTO;
import mii.bsi.apiportal.repository.BsiTokenVerificationRepository;
import mii.bsi.apiportal.repository.UserRepository;
import mii.bsi.apiportal.utils.*;
import mii.bsi.apiportal.validation.AuthenticationValidation;
import mii.bsi.apiportal.validation.EmailValidation;
import mii.bsi.apiportal.validation.UserValidation;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

@Service
public class AuthenticationService {

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BsiTokenVerificationRepository tokenRepository;

    @Autowired
    private JwtUtility jwtUtility;

    @Autowired
    private AuthenticationValidation validation;

    @Autowired
    private EmailValidation emailValidation;
    @Autowired
    private UserValidation userValidation;

    @Autowired
    private LogService logService;
    @Autowired
    private EncryptUtility encryptUtility;
    @Autowired
    private EmailUtility emailUtility;

    public static final String AUTHENTICATION = "Authentication";

    public static final String SIGN_OUT = "Sign Out";
    public static final String GENERATE_OTP = "Generate OTP Email";
    public static final String VERIFICATION_OTP = "Verification OTP Email";


    public ResponseEntity<ResponseHandling<AuthenticationResponseDTO>> authenticate(
            AuthenticationRequestDTO request, Errors errors) {
        ResponseHandling<AuthenticationResponseDTO> responseData = new ResponseHandling<>();
        RequestData<AuthenticationRequestDTO> requestData = new RequestData<>();
        requestData.setPayload(request);
        try {

            ValidationResponse<AuthenticationResponseDTO> validInput = validation.validationRequest(requestData, errors);
            if(!validInput.isValid()){
                requestData.getPayload().setPassword(passwordEncoder.encode(request.getPassword()));
                logService.saveLog(requestData, validInput.getResponse().getBody(), validInput.getStatusCode() , this.getClass().getName(), AUTHENTICATION);
                return validInput.getResponse();
            }

            User user = userRepository.findByEmail(requestData.getPayload().getEmail());
//            System.out.println(user);

            ValidationResponse<AuthenticationResponseDTO> validBusiness = validation.validationBusiness(requestData, user);
            if(!validBusiness.isValid()){
                requestData.getPayload().setPassword(passwordEncoder.encode(request.getPassword()));
                logService.saveLog(requestData, validBusiness.getResponse().getBody(), validBusiness.getStatusCode() ,this.getClass().getName(), AUTHENTICATION);
                return validBusiness.getResponse();
            }

            final UserDetails userDetails = new MyUserPrincipal(user);

            final String token =
                    jwtUtility.generateToken(userDetails, generateClaim(user));
            Date expiredAt = jwtUtility.getExpirationDateFromToken(token);
            user.setLogin(true);
            System.out.println(user);
            userRepository.save(user);

            responseData.success();
            responseData.setPayload(new AuthenticationResponseDTO(user,token, expiredAt));

        }catch (Exception e){
            responseData.failed(e.getMessage());
            e.printStackTrace();
            logService.saveLog(requestData, responseData, StatusCode.INTERNAL_SERVER_ERROR ,this.getClass().getName(), AUTHENTICATION);
            return ResponseEntity.internalServerError().body(responseData);

        }

        requestData.getPayload().setPassword(passwordEncoder.encode(request.getPassword()));
        logService.saveLog(requestData, responseData, StatusCode.OK ,this.getClass().getName(), AUTHENTICATION);
        return ResponseEntity.ok(responseData);
    }

    public ResponseEntity<ResponseHandling> signOut(String token){
        ResponseHandling responseData = new ResponseHandling<>();
        RequestData requestData = new RequestData<>();

        try {
            String username = jwtUtility.getUsernameFromToken(token);
            User user = userRepository.findByEmail(username);

            if(user == null){
                responseData.success();
                logService.saveLog(requestData, responseData, StatusCode.OK ,this.getClass().getName(), SIGN_OUT);
                return ResponseEntity.ok(responseData);
            }

            user.setLogin(false);
            userRepository.save(user);
            responseData.success();

        }catch (Exception e){
            responseData.failed(e.getMessage());
            e.printStackTrace();
            logService.saveLog(requestData, responseData, StatusCode.INTERNAL_SERVER_ERROR ,this.getClass().getName(), SIGN_OUT);
            return ResponseEntity.internalServerError().body(responseData);
        }

        logService.saveLog(requestData, responseData, StatusCode.OK ,this.getClass().getName(), SIGN_OUT);
        return ResponseEntity.ok(responseData);
    }

    public ResponseEntity<ResponseHandling> generateOTPEmail(OTPEmailVerificationGenerateRequestDTO request, String token, Errors errors){
        ResponseHandling responseData = new ResponseHandling();
        RequestData<OTPEmailVerificationGenerateRequestDTO> requestData = new RequestData<>();
        requestData.setPayload(request);

        try {

            if (errors.hasErrors()) {
                responseData.failed(CustomError.validRequest(errors), "Bad Request");
                logService.saveLog(requestData, responseData, StatusCode.BAD_REQUEST, this.getClass().getName(),
                        GENERATE_OTP);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseData);
            }

//            if(!emailValidation.validFormatEmail(request.getEmail())){
//                responseData.failed("Format Email tidak valid");
//                logService.saveLog(requestData, responseData, StatusCode.BAD_REQUEST ,this.getClass().getName(), GENERATE_OTP);
//                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseData);
//            }

            if(!emailValidation.validEmail(request.getEmail())){
                responseData.failed("Silahkan gunakan Email perusahaan anda");
                logService.saveLog(requestData, responseData, StatusCode.BAD_REQUEST ,this.getClass().getName(), GENERATE_OTP);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseData);
            }
            User user = userValidation.getUserFromToken(token);
            if(!user.getAuthPrincipal().equals(Roles.MITRA)){
                responseData.failed();
                logService.saveLog(requestData, responseData, StatusCode.FORBIDDEN ,this.getClass().getName(), GENERATE_OTP);
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(responseData);
            }

            String kodeOTP = RandomStringUtils.randomNumeric(6);
            final String encToken = encryptUtility.encryptAES(kodeOTP, Params.PASS_KEY);
            BsiTokenVerification tokenVerification = new BsiTokenVerification();
            tokenVerification.setToken(encToken);
            tokenVerification.setUser(user);
            tokenVerification.setIdToken(null);
            tokenVerification.setTokenType(TokenVerificationType.OTP_EMAIL_VERIFICATION);

            emailUtility.sendEmailOTPVerification(request.getEmail(), request.getName(), kodeOTP);
            tokenVerification.setTokenCreateDate(new Date());
            tokenRepository.save(tokenVerification);
            responseData.success();

        }catch (Exception e){
            e.printStackTrace();
            responseData.failed(e.getMessage());
            logService.saveLog(requestData, responseData, StatusCode.INTERNAL_SERVER_ERROR ,this.getClass().getName(), GENERATE_OTP);
            return ResponseEntity.internalServerError().body(responseData);
        }
        logService.saveLog(requestData, responseData, StatusCode.OK ,this.getClass().getName(), GENERATE_OTP);
        return ResponseEntity.ok(responseData);
    }

    public ResponseEntity<ResponseHandling> verifikasiOTPEmail(OTPEmailVerificationRequestDTO request, String token, Errors errors){
        ResponseHandling responseData = new ResponseHandling();
        RequestData<OTPEmailVerificationRequestDTO> requestData = new RequestData<>();
        requestData.setPayload(request);

        try {
            if (errors.hasErrors()) {
                responseData.failed(CustomError.validRequest(errors), "Bad Request");
                logService.saveLog(requestData, responseData, StatusCode.BAD_REQUEST, this.getClass().getName(),
                        VERIFICATION_OTP);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseData);
            }

            User user = userValidation.getUserFromToken(token);
            if(!user.getAuthPrincipal().equals(Roles.MITRA)){
                responseData.failed();
                logService.saveLog(requestData, responseData, StatusCode.FORBIDDEN ,this.getClass().getName(), VERIFICATION_OTP);
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(responseData);
            }

            final String encToken = encryptUtility.encryptAES(request.getKodeOtp(), Params.PASS_KEY);
            BsiTokenVerification tokenVerification = tokenRepository.findByToken(encToken);
            if(tokenVerification == null){
                responseData.failed("Kode Invalid");
                logService.saveLog(requestData, responseData, StatusCode.BAD_REQUEST, this.getClass().getName(),
                        VERIFICATION_OTP);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseData);
            }

            if(tokenVerification.isTokenExpired()){
                responseData.failed("Kode OTP sudah kadaluwarsa, silahkan kirim ulang!");
                logService.saveLog(requestData, responseData, StatusCode.OK, this.getClass().getName(),
                        VERIFICATION_OTP);
                return ResponseEntity.status(HttpStatus.OK).body(responseData);
            }

            responseData.success("Email berhasil di verifikasi");

        }catch (Exception e){
            e.printStackTrace();
            responseData.failed(e.getMessage());
            logService.saveLog(requestData, responseData, StatusCode.INTERNAL_SERVER_ERROR ,this.getClass().getName(), VERIFICATION_OTP);
            return ResponseEntity.internalServerError().body(responseData);
        }

        logService.saveLog(requestData, responseData, StatusCode.OK ,this.getClass().getName(), VERIFICATION_OTP);
        return ResponseEntity.ok(responseData);
    }

    private Map<String, Object> generateClaim(User user){
        Map<String, Object> claims = new HashMap<>();
        claims.put("uid", user.getId());
        claims.put("corporateName", user.getCorporateName());
        claims.put("role", user.getAuthPrincipal());
        return claims;
    }
}
