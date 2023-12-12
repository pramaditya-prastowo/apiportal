package mii.bsi.apiportal.service;

import mii.bsi.apiportal.domain.BsmApiConfig;
import mii.bsi.apiportal.dto.BsmApiConfigDTO;
import mii.bsi.apiportal.utils.ResponseHandling;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;

import java.util.List;

public interface BsmApiConfigService {

    ResponseEntity<ResponseHandling<List<BsmApiConfig>>> getAll(String token);
    ResponseEntity<ResponseHandling<BsmApiConfig>> getByKeyname(String token, String keyname);
    ResponseEntity<ResponseHandling> createParam(String token, BsmApiConfig request, Errors errors);
    ResponseEntity<ResponseHandling> updateParam(String token, BsmApiConfig request, Errors errors);
    ResponseEntity<ResponseHandling> deleteParam(String token, String keyname);
    BsmApiConfigDTO readData(String keyName, String cacheManager, String cacheName);
    BsmApiConfig getConfig(String keyname, String keygroup);
    BsmApiConfig getConfigByKeyName(String keyName);
}
