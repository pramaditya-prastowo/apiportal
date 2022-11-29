package mii.bsi.apiportal.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import mii.bsi.apiportal.domain.ServiceApiDomain;
import mii.bsi.apiportal.service.ServiceApiService;
import mii.bsi.apiportal.utils.ResponseHandling;

@RestController
@RequestMapping("/api/v1.0/serviceApi")
public class ServiceApiController {
    @Autowired
    private ServiceApiService serviceApiService;

    @PostMapping(value = "/create")
    public ResponseEntity<ResponseHandling<ServiceApiDomain>> create(@Valid @RequestBody ServiceApiDomain serviceApi,
            Errors errors) {
        return serviceApiService.create(serviceApi, errors);

    }

    @PostMapping(value = "/update")
    public ResponseEntity<ResponseHandling<ServiceApiDomain>> update(@Valid @RequestBody ServiceApiDomain serviceApi,
            Errors errors) {
        return serviceApiService.create(serviceApi, errors);
    }

    @GetMapping(value = "/all")
    public ResponseEntity<ResponseHandling<Iterable<ServiceApiDomain>>> getAll(ServiceApiDomain serviceApi) {
        return serviceApiService.getAll();
    }

    @GetMapping
    public ResponseEntity<ResponseHandling<ServiceApiDomain>> getById(@RequestParam String id) {
        return serviceApiService.getById(id);
    }
}
