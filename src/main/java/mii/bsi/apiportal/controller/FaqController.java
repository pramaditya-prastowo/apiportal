package mii.bsi.apiportal.controller;

import mii.bsi.apiportal.domain.FAQ;
import mii.bsi.apiportal.service.FaqService;
import mii.bsi.apiportal.utils.ResponseHandling;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController()
@RequestMapping("/api/v1.0/faq")
public class FaqController {

    @Autowired
    private FaqService faqService;

    @GetMapping
    public ResponseEntity<ResponseHandling> getFaq(){
        return faqService.getFaq();
    }

    @PostMapping
    public ResponseEntity<ResponseHandling> createFaq(@Valid @RequestBody FAQ faq,
                                                      @RequestHeader(HttpHeaders.AUTHORIZATION) String token,
                                                      Errors errors){
        return faqService.create(faq,token.substring(7), errors);
    }

    @PatchMapping
    public ResponseEntity<ResponseHandling> updateFaq(@Valid @RequestBody FAQ faq,
                                                      @RequestHeader(HttpHeaders.AUTHORIZATION) String token,
                                                      Errors errors){
        return faqService.updateFaq(faq, token.substring(7), errors);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseHandling> deleteFaq(@PathVariable("id") String id, @RequestHeader(HttpHeaders.AUTHORIZATION) String token){
        return faqService.deleteFaq(id, token.substring(7));
    }
}
