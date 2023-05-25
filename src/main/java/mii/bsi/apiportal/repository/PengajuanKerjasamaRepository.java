package mii.bsi.apiportal.repository;

import mii.bsi.apiportal.domain.PengajuanKerjasama;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PengajuanKerjasamaRepository extends JpaRepository<PengajuanKerjasama, Long> {

//    @Query(value="select * from bsi_pengajuan_kerjasama_api_portal where created_by = ?", nativeQuery = true)
    List<PengajuanKerjasama> findByCreatedBy(String userId);

    @Query(value="select * from bsi_pengajuan_kerjasama_api_portal where peker_id = ?", nativeQuery = true)
    PengajuanKerjasama findById(String id);

//    @Query(value="select * from bsi_pengajuan_kerjasama_api_portal where created_by = ?", nativeQuery = true)
//    List<PengajuanKerjasama> findByCreated(String userId);
}
