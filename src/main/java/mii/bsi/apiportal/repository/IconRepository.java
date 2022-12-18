package mii.bsi.apiportal.repository;

import mii.bsi.apiportal.domain.Icon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IconRepository extends JpaRepository<Icon, Long> {
}
