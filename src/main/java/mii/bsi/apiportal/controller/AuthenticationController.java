package mii.bsi.apiportal.controller;

import mii.bsi.apiportal.dto.AuthenticationRequestDTO;
import mii.bsi.apiportal.dto.AuthenticationResponseDTO;
import mii.bsi.apiportal.service.AuthenticationService;
import mii.bsi.apiportal.utils.CustomError;
import mii.bsi.apiportal.utils.RequestData;
import mii.bsi.apiportal.utils.ResponseHandling;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1.0/auth")
public class AuthenticationController {

    @Autowired
    private AuthenticationService authenticationService;

    @PostMapping(value = "/authentication")
    public ResponseEntity<ResponseHandling<AuthenticationResponseDTO>> authentication(
            @Valid @RequestBody AuthenticationRequestDTO request, Errors errors) throws Exception {

        return authenticationService.authenticate(request, errors);
    }

    @PostMapping(value = "/sign-out")
    public ResponseEntity<ResponseHandling> signOut(@RequestHeader(HttpHeaders.AUTHORIZATION) String token){
        return authenticationService.signOut(token.substring(7));
    }

}
