package mii.bsi.apiportal.repository;

import mii.bsi.apiportal.domain.LogApiGw;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LogApiGwRepository extends JpaRepository<LogApiGw, Long> {
}
