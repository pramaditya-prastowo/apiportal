package mii.bsi.apiportal.repository;

import mii.bsi.apiportal.domain.BsiLogApiPortal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BsiLogApiPortalRepository extends JpaRepository<BsiLogApiPortal, Long> {

}
