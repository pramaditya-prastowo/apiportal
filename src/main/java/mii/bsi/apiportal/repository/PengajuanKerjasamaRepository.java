package mii.bsi.apiportal.repository;

import mii.bsi.apiportal.domain.PengajuanKerjasama;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PengajuanKerjasamaRepository extends JpaRepository<PengajuanKerjasama, Long> {
}
