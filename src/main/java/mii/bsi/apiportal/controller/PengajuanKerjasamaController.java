package mii.bsi.apiportal.controller;

import mii.bsi.apiportal.domain.PengajuanKerjasama;
import mii.bsi.apiportal.service.PengajuanKerjasamaService;
import mii.bsi.apiportal.utils.ResponseHandling;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1.0/kerjasama")
public class PengajuanKerjasamaController {

    @Autowired
    private PengajuanKerjasamaService pengajuanKerjasamaService;

    @GetMapping
    public ResponseEntity<ResponseHandling<List<PengajuanKerjasama>>> getAllPengajuanKerjasama(@RequestHeader(HttpHeaders.AUTHORIZATION) String token,
                                                                                               @RequestParam String userId) {
        return pengajuanKerjasamaService.getAllPengajuanKerjasama(token.substring(7), userId);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseHandling<PengajuanKerjasama>> getPengajuanKerjasamaById(@RequestHeader(HttpHeaders.AUTHORIZATION) String token, @PathVariable Long id) {
        return pengajuanKerjasamaService.getPengajuanKerjasamaById(token.substring(7), id);
    }

    @PostMapping
    public ResponseEntity<ResponseHandling> addPengajuanKerjasama(@Valid @RequestBody PengajuanKerjasama pengajuanKerjasama,
                                                                  @RequestHeader(HttpHeaders.AUTHORIZATION) String token, Errors errors) {
        return pengajuanKerjasamaService.addPengajuanKerjasama(pengajuanKerjasama, token.substring(7), errors);
    }

    @PutMapping("/{id}")
    public void updatePengajunKerjasama(@PathVariable Long id, @RequestBody PengajuanKerjasama pengajuanKerjasama) {
        pengajuanKerjasamaService.updatePengajuanKerjasama(id, pengajuanKerjasama);
    }

    @DeleteMapping("/{id}")
    public void deletePengajuanKerjasama(@PathVariable Long id) {
        pengajuanKerjasamaService.deletePengajuanKerjasama(id);
    }

}
