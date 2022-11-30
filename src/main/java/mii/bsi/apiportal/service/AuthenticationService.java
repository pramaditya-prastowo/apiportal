package mii.bsi.apiportal.service;

import mii.bsi.apiportal.constant.StatusCode;
import mii.bsi.apiportal.domain.MyUserPrincipal;
import mii.bsi.apiportal.domain.User;
import mii.bsi.apiportal.dto.AuthenticationRequestDTO;
import mii.bsi.apiportal.dto.AuthenticationResponseDTO;
import mii.bsi.apiportal.repository.UserRepository;
import mii.bsi.apiportal.utils.JwtUtility;
import mii.bsi.apiportal.utils.RequestData;
import mii.bsi.apiportal.utils.ResponseHandling;
import mii.bsi.apiportal.utils.ValidationResponse;
import mii.bsi.apiportal.validation.AuthenticationValidation;
import org.springframework.beans.factory.annotation.Autowired;
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
    private JwtUtility jwtUtility;

    @Autowired
    private AuthenticationValidation validation;

    @Autowired
    private LogService logService;

    public static final String AUTHENTICATION = "Authentication";


    public ResponseEntity<ResponseHandling<AuthenticationResponseDTO>> authenticate(
            AuthenticationRequestDTO request, Errors errors) {
        ResponseHandling<AuthenticationResponseDTO> responseData = new ResponseHandling<>();
        RequestData<AuthenticationRequestDTO> requestData = new RequestData<>();
        requestData.setPayload(request);
        try {

            ValidationResponse<AuthenticationResponseDTO> validInput = validation.validationRequest(requestData, errors);
            if(!validInput.isValid()){
                logService.saveLog(requestData, validInput.getResponse().getBody(), validInput.getStatusCode() , this.getClass().getName(), AUTHENTICATION);
                return validInput.getResponse();
            }

            User user = userRepository.findByEmail(requestData.getPayload().getEmail());

            ValidationResponse<AuthenticationResponseDTO> validBusiness = validation.validationBusiness(requestData, user);
            if(!validBusiness.isValid()){
                logService.saveLog(requestData, validBusiness.getResponse().getBody(), validBusiness.getStatusCode() ,this.getClass().getName(), AUTHENTICATION);
                return validBusiness.getResponse();
            }

            final UserDetails userDetails = new MyUserPrincipal(user);

            final String token =
                    jwtUtility.generateToken(userDetails, generateClaim(user));
            Date expiredAt = jwtUtility.getExpirationDateFromToken(token);

            responseData.success();
            responseData.setPayload(new AuthenticationResponseDTO(user,token, expiredAt));

        }catch (Exception e){
            responseData.failed(e.getMessage());
            e.printStackTrace();
            logService.saveLog(requestData, responseData, StatusCode.INTERNAL_SERVER_ERROR ,this.getClass().getName(), AUTHENTICATION);
            return ResponseEntity.internalServerError().body(responseData);

        }
        logService.saveLog(requestData, responseData, StatusCode.OK ,this.getClass().getName(), AUTHENTICATION);
        return ResponseEntity.ok(responseData);
    }

    private Map<String, Object> generateClaim(User user){
        Map<String, Object> claims = new HashMap<>();
        claims.put("uid", user.getId());
        claims.put("corporateName", user.getCorporateName());
        return claims;
    }
}
