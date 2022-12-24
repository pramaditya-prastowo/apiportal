package mii.bsi.apiportal.repository;

import mii.bsi.apiportal.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import mii.bsi.apiportal.domain.ServiceApiDomain;

import java.util.List;

@Repository
public interface ServiceApiRepository extends JpaRepository<ServiceApiDomain, Long> {
    @Query(value = "SELECT * from bsi_service_api_api_portal where in_active = false", nativeQuery = true)
    public List<ServiceApiDomain> findByServiceApiActive();

    @Query(value = "SELECT * from bsi_service_api_api_portal where id=?1 in_active = false", nativeQuery = true)
    public ServiceApiDomain findByIdActive(Long paramLong);
}
