package mii.bsi.apiportal.controller;

import mii.bsi.apiportal.utils.ResponseHandling;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1.0/forget_password")
public class ForgetPasswordController {

    @PostMapping
    public ResponseEntity<ResponseHandling> forgetPassword(@RequestParam String email){
        return ResponseEntity.ok(new ResponseHandling());
    }
}
