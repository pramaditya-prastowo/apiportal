package mii.bsi.apiportal.validation;

import mii.bsi.apiportal.constant.StatusCode;
import mii.bsi.apiportal.domain.User;
import mii.bsi.apiportal.dto.AuthenticationRequestDTO;
import mii.bsi.apiportal.dto.AuthenticationResponseDTO;
import mii.bsi.apiportal.repository.UserRepository;
import mii.bsi.apiportal.utils.RequestData;
import mii.bsi.apiportal.utils.ResponseHandling;
import mii.bsi.apiportal.utils.ValidationResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;

import java.util.ArrayList;
import java.util.List;

@Service
public class AuthenticationValidation {

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    public ValidationResponse<AuthenticationResponseDTO> validationRequest(
            RequestData<AuthenticationRequestDTO> requestData, Errors errors){

        ValidationResponse<AuthenticationResponseDTO> validationResponse = new ValidationResponse<>();
        ResponseHandling<AuthenticationResponseDTO> responseData = new ResponseHandling<>();

        if(errors.hasErrors()){
            List<String> errorList = new ArrayList<>();
            if(errors.hasErrors()){
                for (ObjectError error : errors.getAllErrors()){
                    System.out.println(error.getDefaultMessage());
                    errorList.add(error.getDefaultMessage());
                }
            }
            responseData.failed("Permintaan tidak sesuai");
            responseData.setMessageError(errorList);
            validationResponse.setResponse(ResponseEntity.badRequest().body(responseData));
            validationResponse.setValid(false);
            validationResponse.setStatusCode(StatusCode.BAD_REQUEST);
            System.out.println(validationResponse);
            return validationResponse;
        }

        validationResponse.setValid(true);
        return validationResponse;
    }

    public ValidationResponse<AuthenticationResponseDTO> validationBusiness(
            RequestData<AuthenticationRequestDTO> requestData, User user){

        ValidationResponse<AuthenticationResponseDTO> validationResponse = new ValidationResponse<>();
        ResponseHandling<AuthenticationResponseDTO> responseData = new ResponseHandling<>();
        System.out.println(user);

        if(user == null){
            responseData.failed("Email atau password salah");
            validationResponse.setValid(false);
            validationResponse.setStatusCode(StatusCode.NOT_FOUND);
            validationResponse.setResponse(ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseData));
            return validationResponse;
        }

        if(!passwordEncoder.matches(requestData.getPayload().getPassword(), user.getPassword())){
//            user.setRetryPasswordCount(user.getRetryPasswordCount() + 1);
            int countRetryPass = user.getRetryPasswordCount();

            if((countRetryPass+ 1) > 2){
                responseData.failed("Akun anda telah terkunci");
                user.setAccountLocked(true);
                user.setRetryPasswordCount(3);
                userRepository.save(user);
                validationResponse.setValid(false);
                validationResponse.setStatusCode(StatusCode.UNAUTHORIZED);
                validationResponse.setResponse(ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(responseData));
                return validationResponse;
            }
            user.setRetryPasswordCount(user.getRetryPasswordCount() + 1);
            userRepository.save(user);
            responseData.failed("Email atau password salah");
            validationResponse.setValid(false);
            validationResponse.setStatusCode(StatusCode.UNAUTHORIZED);
            validationResponse.setResponse(ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(responseData));
            return validationResponse;
        }

        if(user.isAccountInactive()){
            responseData.failed("Akun anda sudah tidak aktif");
            validationResponse.setValid(false);
            validationResponse.setStatusCode(StatusCode.UNAUTHORIZED);
            validationResponse.setResponse(ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(responseData));
            return validationResponse;
        }

        if(user.isAccountLocked()){
            responseData.failed("Akun anda terkunci");
            validationResponse.setValid(false);
            validationResponse.setStatusCode(StatusCode.UNAUTHORIZED);
            validationResponse.setResponse(ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(responseData));
            return validationResponse;
        }

        validationResponse.setValid(true);
        return validationResponse;
    }
}
