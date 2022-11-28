package mii.bsi.apiportal.controller;

import mii.bsi.apiportal.dto.AuthenticationRequestDTO;
import mii.bsi.apiportal.dto.AuthenticationResponseDTO;
import mii.bsi.apiportal.service.AuthenticationService;
import mii.bsi.apiportal.utils.CustomError;
import mii.bsi.apiportal.utils.RequestData;
import mii.bsi.apiportal.utils.ResponseHandling;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/oauth2/v1/authentication")
public class AuthenticationController {

    @Autowired
    private AuthenticationService authenticationService;

    @PostMapping()
    public ResponseEntity<ResponseHandling<AuthenticationResponseDTO>> authentication(
            @Valid @RequestBody AuthenticationRequestDTO request, Errors errors) throws Exception {

        return authenticationService.authenticate(request, errors);
    }
}
