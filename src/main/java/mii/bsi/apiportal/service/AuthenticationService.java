package mii.bsi.apiportal.service;

import mii.bsi.apiportal.domain.User;
import mii.bsi.apiportal.dto.AuthenticationRequestDTO;
import mii.bsi.apiportal.dto.AuthenticationResponseDTO;
import mii.bsi.apiportal.dto.OTPEmailVerificationGenerateRequestDTO;
import mii.bsi.apiportal.dto.OTPEmailVerificationRequestDTO;
import mii.bsi.apiportal.utils.ResponseHandling;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;

import java.util.Map;

public interface AuthenticationService {
    ResponseEntity<ResponseHandling<AuthenticationResponseDTO>> authenticate(
            AuthenticationRequestDTO request, Errors errors);

    ResponseEntity<ResponseHandling> signOut(String token);

    ResponseEntity<ResponseHandling> generateOTPEmail(OTPEmailVerificationGenerateRequestDTO request, String token, Errors errors);

    ResponseEntity<ResponseHandling> verifikasiOTPEmail(OTPEmailVerificationRequestDTO request, String token, Errors errors);

}
