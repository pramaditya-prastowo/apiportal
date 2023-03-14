package mii.bsi.apiportal.repository;

import mii.bsi.apiportal.domain.Application;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MyAppsRepository extends JpaRepository<Application, Long> {

    List<Application> findByUserId(String stringParams);
    Optional<Application> findById(Long paramLong);

    @Query(value = "SELECT NEXTVAL('bsi_corp_id_seq')", nativeQuery = true)
    public String getCorpIdSeq();
}
