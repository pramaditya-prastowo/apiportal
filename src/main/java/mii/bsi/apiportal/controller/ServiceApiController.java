package mii.bsi.apiportal.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import mii.bsi.apiportal.domain.ServiceApiDomain;
import mii.bsi.apiportal.service.ServiceApiService;
import mii.bsi.apiportal.utils.ResponseHandling;

@RestController
@RequestMapping("/api/v1.0/serviceApi")
public class ServiceApiController {
    @Autowired
    private ServiceApiService serviceApiService;

    @PostMapping
    public ResponseEntity<ResponseHandling<ServiceApiDomain>> create(@Valid @RequestBody ServiceApiDomain serviceApi,
                                                                     @RequestHeader(HttpHeaders.AUTHORIZATION) String token,
                                                                     Errors errors) {
        return serviceApiService.create(serviceApi,token.substring(7), errors);
    }

    @PatchMapping
    public ResponseEntity<ResponseHandling<ServiceApiDomain>> update(@Valid @RequestBody ServiceApiDomain serviceApi,
                                                                     Errors errors) {
        return serviceApiService.update(serviceApi, errors);
    }

    @GetMapping
    public ResponseEntity<ResponseHandling<Iterable<ServiceApiDomain>>> getAll() {
        return serviceApiService.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseHandling<ServiceApiDomain>> getById(@PathVariable("id") Long id) {
        return serviceApiService.getById(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseHandling> delete(@PathVariable("id") Long id,@RequestHeader(HttpHeaders.AUTHORIZATION) String token){
        return serviceApiService.deleteById(id, token.substring(7));
    }

    @GetMapping("/count")
    public ResponseEntity<ResponseHandling<Integer>> getCountServiceAPI(@RequestHeader(HttpHeaders.AUTHORIZATION) String token){
        return serviceApiService.getCountServiceAPI(token.substring(7));
    }
}
