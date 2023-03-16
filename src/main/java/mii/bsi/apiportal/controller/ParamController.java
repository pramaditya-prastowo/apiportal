package mii.bsi.apiportal.controller;

import mii.bsi.apiportal.domain.BsmApiConfig;
import mii.bsi.apiportal.service.ParamsService;
import mii.bsi.apiportal.utils.ResponseHandling;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1.0/params")
public class ParamController {

    @Autowired
    private ParamsService paramsService;

    @GetMapping("/privacy-policy")
    public ResponseEntity<ResponseHandling<Map<String, Object>>> getPrivacyPolicy(){
        return paramsService.getPrivacyPolicy();
    }

}
