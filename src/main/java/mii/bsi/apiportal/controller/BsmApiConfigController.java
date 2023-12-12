package mii.bsi.apiportal.controller;

import mii.bsi.apiportal.domain.BsmApiConfig;
import mii.bsi.apiportal.dto.BsmApiConfigDTO;
import mii.bsi.apiportal.service.BsmApiConfigService;
import mii.bsi.apiportal.service.BsmApiConfigServiceImpl;
import mii.bsi.apiportal.utils.ResponseHandling;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/config-params")
public class BsmApiConfigController {

    @Autowired
    private BsmApiConfigService bsmApiConfigService;

    @RequestMapping(method = RequestMethod.GET, value = "/readData", params = {"keyname"})
    public BsmApiConfigDTO readData(@RequestParam("keyname") String keyname) throws Exception {
        return bsmApiConfigService.readData(keyname, "BsmApiCache", "SYS_PARAM");
    }

    @GetMapping
    public ResponseEntity<ResponseHandling<List<BsmApiConfig>>> getAll(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String token)throws Exception{
        return bsmApiConfigService.getAll(token.substring(7));
    }


    @GetMapping("/{keyname}")
    public ResponseEntity<ResponseHandling<BsmApiConfig>> getByKeyname(@PathVariable("keyname") String keyname,
                                                                           @RequestHeader(HttpHeaders.AUTHORIZATION) String token){
        return bsmApiConfigService.getByKeyname(token.substring(7), keyname);
    }

    @PostMapping
    public ResponseEntity<ResponseHandling> createParam(@Valid @RequestBody BsmApiConfig request,
                                                        @RequestHeader(HttpHeaders.AUTHORIZATION) String token, Errors error){
        return bsmApiConfigService.createParam(token.substring(7), request, error);
    }

    @PatchMapping
    public ResponseEntity<ResponseHandling> updateParam(@Valid @RequestBody BsmApiConfig request,
                                                        @RequestHeader(HttpHeaders.AUTHORIZATION) String token, Errors error){
        return bsmApiConfigService.updateParam(token.substring(7), request, error);
    }

    @DeleteMapping("/{keyname}")
    public ResponseEntity<ResponseHandling> deleteParam(@PathVariable("keyname") String keyname,
                                                        @RequestHeader(HttpHeaders.AUTHORIZATION) String token){
        return bsmApiConfigService.deleteParam(token.substring(7), keyname);
    }

}
