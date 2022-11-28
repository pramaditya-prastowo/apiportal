package mii.bsi.apiportal.repository;

import mii.bsi.apiportal.domain.BsiTokenVerification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BsiTokenVerificationRepository extends JpaRepository<BsiTokenVerification, Long> {

    public BsiTokenVerification findByToken(String paramString);
}
