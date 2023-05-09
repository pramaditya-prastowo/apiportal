package mii.bsi.apiportal.repository;

import mii.bsi.apiportal.domain.KerjasamaServiceApi;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface KerjasamaServiceApiRepository extends JpaRepository<KerjasamaServiceApi, Long> {

    List<KerjasamaServiceApi> findByPekerId(Long id);
}
