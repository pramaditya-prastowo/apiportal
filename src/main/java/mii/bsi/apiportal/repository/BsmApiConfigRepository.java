package mii.bsi.apiportal.repository;

import mii.bsi.apiportal.domain.BsmApiConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BsmApiConfigRepository extends JpaRepository<BsmApiConfig, String> {

    BsmApiConfig findByKeyNameAndCacheManagerAndCacheName(String keyname, String cacheManager, String cacheName);
}
