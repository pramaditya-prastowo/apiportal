package mii.bsi.apiportal.service;

import mii.bsi.apiportal.domain.BsmApiKey;
import mii.bsi.apiportal.dto.BsmApiKeyDTO;
import mii.bsi.apiportal.repository.BsmApiKeyRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class BsmApiKeyService {

    @Autowired
    private BsmApiKeyRepository bsmApiKeyConfigRepository;

    public BsmApiKeyDTO readData(String apiKey){
        String result = "";

        BsmApiKey bsmApiKey = bsmApiKeyConfigRepository.findByApiKey(apiKey);
        BsmApiKeyDTO bsmApiKeyDTO = new BsmApiKeyDTO();

        try{
            bsmApiKeyDTO = new BsmApiKeyDTO(bsmApiKey);
            bsmApiKeyDTO.setResponseCode("00");
            bsmApiKeyDTO.setResponseMessage("success");
        }catch (Exception ex) {
            bsmApiKeyDTO.setResponseCode("99");
            bsmApiKeyDTO.setResponseMessage("Error getting data!");
        }

        return bsmApiKeyDTO;
    }

    public List<BsmApiKeyDTO> readDataAll(){
        Iterable<BsmApiKey> iterable = bsmApiKeyConfigRepository.findAll();
        BsmApiKeyDTO bsmApiKeyDTO = new BsmApiKeyDTO();
        try{
            List<BsmApiKeyDTO> apiKeys = StreamSupport.stream(iterable.spliterator(), false).map(apiKey ->{
                BeanUtils.copyProperties(apiKey, bsmApiKeyDTO);
                return bsmApiKeyDTO;
            }).collect(Collectors.toList());
            return apiKeys;
        }catch (Exception ex){
            bsmApiKeyDTO.setResponseCode("99");
            bsmApiKeyDTO.setResponseMessage("Error getting data!");
        }
        return null;
    }

    public BsmApiKey getByCorpId(String corpId){
        return bsmApiKeyConfigRepository.findByCorpId(corpId);
    }
}
