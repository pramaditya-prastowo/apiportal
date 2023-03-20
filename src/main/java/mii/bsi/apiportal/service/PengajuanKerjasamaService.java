package mii.bsi.apiportal.service;

import mii.bsi.apiportal.domain.PengajuanKerjasama;
import mii.bsi.apiportal.repository.PengajuanKerjasamaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PengajuanKerjasamaService {

    @Autowired
    private PengajuanKerjasamaRepository pengajuanKerjasamaRepository;

    public List<PengajuanKerjasama> getAllPengajuanKerjasama() {
        return pengajuanKerjasamaRepository.findAll();
    }

    public Optional<PengajuanKerjasama> getPengajuanKerjasamaById(Long id) {
        return pengajuanKerjasamaRepository.findById(id);
    }

    public void addPengajuanKerjasama(PengajuanKerjasama pengajuanKerjasama) {
        pengajuanKerjasamaRepository.save(pengajuanKerjasama);
    }

    public void updatePengajuanKerjasama(Long id, PengajuanKerjasama pengajuanKerjasama) {
        Optional<PengajuanKerjasama> existingPengajuanKerjasama = pengajuanKerjasamaRepository.findById(id);
        if (existingPengajuanKerjasama.isPresent()) {
            pengajuanKerjasama.setId(id);
            pengajuanKerjasamaRepository.save(pengajuanKerjasama);
        }
    }

    public void deletePengajuanKerjasama(Long id) {
        pengajuanKerjasamaRepository.deleteById(id);
    }


}
