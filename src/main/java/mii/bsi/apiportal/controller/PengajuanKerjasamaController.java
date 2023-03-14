package mii.bsi.apiportal.controller;

import mii.bsi.apiportal.domain.PengajuanKerjasama;
import mii.bsi.apiportal.service.PengajuanKerjasamaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1.0/pengajuan-kerjasama")
public class PengajuanKerjasamaController {

    @Autowired
    private PengajuanKerjasamaService pengajuanKerjasamaService;

    @GetMapping
    public List<PengajuanKerjasama> getAllPengajuanKerjasama() {
        return pengajuanKerjasamaService.getAllPengajuanKerjasama();
    }

    @GetMapping("/{id}")
    public Optional<PengajuanKerjasama> getPengajuanKerjasamaById(@PathVariable Long id) {
        return pengajuanKerjasamaService.getPengajuanKerjasamaById(id);
    }

    @PostMapping
    public void addPengajuanKerjasama(@RequestBody PengajuanKerjasama pengajuanKerjasama) {
        pengajuanKerjasamaService.addPengajuanKerjasama(pengajuanKerjasama);
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
