package mii.bsi.apiportal.repository;

import mii.bsi.apiportal.domain.BsmApiKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BsmApiKeyRepository extends JpaRepository<BsmApiKey, String> {
    BsmApiKey findByApiKey(String apiKey);

}