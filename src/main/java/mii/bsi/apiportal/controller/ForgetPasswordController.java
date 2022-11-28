package mii.bsi.apiportal.controller;

import mii.bsi.apiportal.service.ForgetPasswordService;
import mii.bsi.apiportal.utils.ResponseHandling;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1.0/forget_password")
public class ForgetPasswordController {

    @Autowired
    private ForgetPasswordService service;

    @PostMapping
    public ResponseEntity<ResponseHandling> forgetPassword(@RequestParam String email){
        return service.forgetPassword(email);
    }
}
