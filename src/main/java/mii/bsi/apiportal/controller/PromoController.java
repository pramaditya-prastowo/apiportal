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

import mii.bsi.apiportal.domain.Promo;
import mii.bsi.apiportal.service.PromoService;
import mii.bsi.apiportal.utils.ResponseHandling;

@RestController
@RequestMapping("/api/v1.0/promo")
public class PromoController {
    @Autowired
    private PromoService promoService;

    @PostMapping(value = "/create")
    public ResponseEntity<ResponseHandling<Promo>> create(@Valid @RequestBody Promo promo,
            Errors errors) {
        return promoService.create(promo, errors);

    }

    @GetMapping(value = "/all")
    public ResponseEntity<ResponseHandling<Iterable<Promo>>> getAll(Promo promo) {
        return promoService.getAll();
    }

    @GetMapping
    public ResponseEntity<ResponseHandling<Promo>> getById(@RequestParam String id) {
        return promoService.getById(id);
    }
}
