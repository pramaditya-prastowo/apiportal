package mii.bsi.apiportal.controller;

import mii.bsi.apiportal.dto.BsmApiKeyDTO;
import mii.bsi.apiportal.service.BsmApiKeyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/key")
public class BsmApiKeyController {
    @Autowired
    BsmApiKeyService bsmApiKeyService;

    @GetMapping(value = "/readData")
    public BsmApiKeyDTO readData(@RequestParam("apiKey") String apiKey) throws Exception{
        return bsmApiKeyService.readData(apiKey);
    }

    @GetMapping(value="/readDatas")
    public List<BsmApiKeyDTO> readDataAll() throws Exception{
        return bsmApiKeyService.readDataAll();
    }
}
