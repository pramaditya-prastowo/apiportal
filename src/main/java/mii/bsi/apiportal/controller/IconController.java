package mii.bsi.apiportal.controller;

import mii.bsi.apiportal.domain.Icon;
import mii.bsi.apiportal.utils.ResponseHandling;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1.0/icon")
public class IconController {

    @PostMapping
    public ResponseEntity<ResponseHandling> createIcon(@Valid @RequestBody Icon icon,
                                                       @RequestHeader(HttpHeaders.AUTHORIZATION) String token,
                                                       Errors errors){
        return ResponseEntity.ok(new ResponseHandling());
    }
}
