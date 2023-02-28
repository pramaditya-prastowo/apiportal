package mii.bsi.apiportal.repository;

import mii.bsi.apiportal.domain.BsmApiConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BsmApiConfigRepository extends JpaRepository<BsmApiConfig, String> {

    BsmApiConfig findByKeynameAndCacheManagerAndCacheName(String keyname, String cacheManager, String cacheName);

    @Query(value = "SELECT * from bsm_api_config where keyname=?1 and keygroup=?2", nativeQuery = true)
    BsmApiConfig findByKeynameAndKeygroup(String keyname, String keygroup);

    @Query(value = "SELECT * from bsm_api_config where keygroup=?1", nativeQuery = true)
    List<BsmApiConfig> findByKeygroup(String keygroup);
}
