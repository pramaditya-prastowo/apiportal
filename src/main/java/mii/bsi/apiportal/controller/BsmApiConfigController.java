package mii.bsi.apiportal.controller;

import mii.bsi.apiportal.dto.BsmApiConfigDTO;
import mii.bsi.apiportal.service.BsmApiConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/config")
public class BsmApiConfigController {

    @Autowired
    BsmApiConfigService bsmApiConfigService;

    @RequestMapping(method = RequestMethod.GET, value = "/readData", params = {"keyname"})
    public BsmApiConfigDTO readData(@RequestParam("keyname") String keyname) throws Exception {
        return bsmApiConfigService.readData(keyname, "BsmApiCache", "SYS_PARAM");
    }
}
