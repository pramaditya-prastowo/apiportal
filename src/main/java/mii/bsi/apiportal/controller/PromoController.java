package mii.bsi.apiportal.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import mii.bsi.apiportal.domain.Promo;
import mii.bsi.apiportal.service.PromoService;
import mii.bsi.apiportal.utils.ResponseHandling;

@RestController
@RequestMapping("/api/v1.0/promo")
public class PromoController {
    @Autowired
    private PromoService promoService;


    @PostMapping
    public ResponseEntity<ResponseHandling> create(@Valid @RequestBody Promo promo,
                                                          @RequestHeader(HttpHeaders.AUTHORIZATION) String token,
            Errors errors) {
        return promoService.create(token.substring(7), promo, errors);

    }

    @PatchMapping
    public ResponseEntity<ResponseHandling> update(@Valid @RequestBody Promo promo,
                                                   @RequestHeader(HttpHeaders.AUTHORIZATION) String token,
                                                   Errors errors) {
        return promoService.create(token.substring(7), promo, errors);

    }

    @GetMapping
    public ResponseEntity<ResponseHandling<Iterable<Promo>>> getAll(Promo promo) {
        return promoService.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseHandling<Promo>> getById(@PathVariable("id") String id) {
        return promoService.getById(id);
    }
}
