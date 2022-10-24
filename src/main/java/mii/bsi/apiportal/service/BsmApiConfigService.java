package mii.bsi.apiportal.service;

import mii.bsi.apiportal.domain.BsmApiConfig;
import mii.bsi.apiportal.dto.BsmApiConfigDTO;
import mii.bsi.apiportal.repository.BsmApiConfigRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BsmApiConfigService {

    @Autowired
    private BsmApiConfigRepository bsmApiConfigRepository;

    public BsmApiConfigDTO readData(String keyName, String cacheManager, String cacheName){
        String result = "";

        BsmApiConfig bsmApiConfig = bsmApiConfigRepository.findByKeyNameAndCacheManagerAndCacheName(keyName, cacheManager, cacheName);
        BsmApiConfigDTO bsmApiConfigDTO = new BsmApiConfigDTO();

        try{
            bsmApiConfigDTO = new BsmApiConfigDTO(bsmApiConfig);
        }catch (Exception ex) {
            bsmApiConfigDTO.setValue("Error getting config!");
        }

        return bsmApiConfigDTO;
    }

}
