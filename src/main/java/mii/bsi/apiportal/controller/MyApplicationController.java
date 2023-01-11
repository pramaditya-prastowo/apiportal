package mii.bsi.apiportal.controller;

import mii.bsi.apiportal.domain.Application;
import mii.bsi.apiportal.domain.ServiceApiDomain;
import mii.bsi.apiportal.service.MyAppService;
import mii.bsi.apiportal.utils.ResponseHandling;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1.0/apps")
public class MyApplicationController {

    @Autowired
    private MyAppService myAppService;

    @GetMapping
    public ResponseEntity<ResponseHandling<List<Application>>> getAll( @RequestHeader(HttpHeaders.AUTHORIZATION) String token){
        return myAppService.getAllByIdUser(token.substring(7));

    }

    @PostMapping
    public ResponseEntity<ResponseHandling<Application>> create(@Valid @RequestBody Application apps,
                                                         @RequestHeader(HttpHeaders.AUTHORIZATION) String token,
                                                         Errors errors){
        return myAppService.create(apps, token.substring(7), errors);
    }


    @GetMapping("/{id}")
    public ResponseEntity<ResponseHandling<Application>> getById(@PathVariable("id") Long id,@RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        return myAppService.getById(id, token.substring(7));
    }
}
