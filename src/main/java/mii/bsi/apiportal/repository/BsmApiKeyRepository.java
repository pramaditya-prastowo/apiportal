package mii.bsi.apiportal.repository;

import mii.bsi.apiportal.domain.BsmApiKeyConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BsmApiKeyConfigRepository extends JpaRepository<BsmApiKeyConfig, String> {
    BsmApiKeyConfig findByApiKey(String apiKey);
}
