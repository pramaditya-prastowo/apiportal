package mii.bsi.apiportal.controller;

import mii.bsi.apiportal.service.EncryptService;
import mii.bsi.apiportal.utils.ResponseHandling;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/v1.0/util")
public class EncryptionController {

    @Autowired
    private EncryptService encryptService;

    @GetMapping(value = "/enc")
    public ResponseEntity<ResponseHandling<String>> encrypt(@RequestParam String value){
        return encryptService.encrypt(value);
    }

    @GetMapping(value = "/dec")
    public ResponseEntity<ResponseHandling<String>> decrypt(@RequestParam String value){
        return encryptService.decrypt(value);
    }
}
