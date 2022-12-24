package mii.bsi.apiportal.repository;

import mii.bsi.apiportal.domain.NeedHelp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NeedHelpRepository extends JpaRepository<NeedHelp, Long> {
}
