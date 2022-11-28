package mii.bsi.apiportal.controller;

import mii.bsi.apiportal.dto.UpdatePasswordRequestDTO;
import mii.bsi.apiportal.service.ForgetPasswordService;
import mii.bsi.apiportal.utils.ResponseHandling;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1.0/forget_password")
public class ForgetPasswordController {

    @Autowired
    private ForgetPasswordService service;

    @PostMapping(value = "/forgetPassword")
    public ResponseEntity<ResponseHandling> forgetPassword(@RequestBody String email) {
        return service.forgetPassword(email);
    }

    @PostMapping (value ="/updatePassword")
    public ResponseEntity<ResponseHandling> updatePassword(@Valid @RequestBody UpdatePasswordRequestDTO request,
            Errors errors) {
        return service.updatePassword(request, errors);
    }
}
