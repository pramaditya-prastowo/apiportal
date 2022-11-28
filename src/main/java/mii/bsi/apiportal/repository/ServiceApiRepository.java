package mii.bsi.apiportal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import mii.bsi.apiportal.domain.ServiceApiDomain;

@Repository
public interface ServiceApiRepository extends JpaRepository<ServiceApiDomain, String> {

}
