package mii.bsi.apiportal.controller;

import mii.bsi.apiportal.domain.NeedHelp;
import mii.bsi.apiportal.service.NeedHelpService;
import mii.bsi.apiportal.utils.ResponseHandling;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1.0/need-help")
public class NeedHelpController {

    @Autowired
    private NeedHelpService needHelpService;

    @PostMapping
    public ResponseEntity<ResponseHandling> postHelp(@Valid @RequestBody NeedHelp help, Errors errors){
        return needHelpService.postHelp(help, errors);
    }

    @GetMapping
    public ResponseEntity<ResponseHandling<List<NeedHelp>>> getHelp(@RequestHeader(HttpHeaders.AUTHORIZATION) String token){
        return needHelpService.getHelp(token.substring(7));
    }
}
