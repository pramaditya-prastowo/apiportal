package mii.bsi.apiportal.repository;

import mii.bsi.apiportal.domain.FAQ;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FaqRepository extends JpaRepository<FAQ, Long> {

    Optional<FAQ> findById(Long id);
}
