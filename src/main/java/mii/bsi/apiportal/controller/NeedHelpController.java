package mii.bsi.apiportal.controller;

import mii.bsi.apiportal.constant.MappingUtils;
import mii.bsi.apiportal.domain.NeedHelp;
import mii.bsi.apiportal.domain.model.FilterGetData;
import mii.bsi.apiportal.service.NeedHelpService;
import mii.bsi.apiportal.utils.ResponseHandling;
import mii.bsi.apiportal.utils.RowDataResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1.0/need-help")
public class NeedHelpController {

    @Autowired
    private NeedHelpService needHelpService;

    @Autowired
    private MappingUtils mappingUtils;

    @PostMapping
    public ResponseEntity<ResponseHandling> postHelp(@Valid @RequestBody NeedHelp help, Errors errors){
        return needHelpService.postHelp(help, errors);
    }

    @GetMapping
    public ResponseEntity<ResponseHandling<List<NeedHelp>>> getHelp(@RequestHeader(HttpHeaders.AUTHORIZATION) String token){
        return needHelpService.getHelp(token.substring(7));
    }

    @GetMapping("/filter")
    public ResponseEntity<ResponseHandling<RowDataResponse<NeedHelp>>> getHelpFilter(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String token,
            @RequestParam(required = false, name="email") String email,
            @RequestParam(required = false, name="name") String name,
            @RequestParam(required = false, name="no_hp") String noHp,
            @RequestParam(required = false, name="company_name") String companyName,
            @RequestParam(required = false, name="status") String status,
            @RequestParam(name="pageNumber") Integer pageNumber,
            @RequestParam(name="pageSize") Integer pageSize,
            @RequestParam(required = false, name="orderBy") String orderBy,
            @RequestParam(required = false, name="sort") String sort){

        FilterGetData filter = mappingUtils.needsHelpMap(pageNumber,pageSize,orderBy,sort,email,name,noHp,companyName,status);
        return needHelpService.getHelpFilter(token.substring(7), filter);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseHandling<NeedHelp>> getHelpById(@RequestHeader(HttpHeaders.AUTHORIZATION) String token, @PathVariable("id") Long id){
        return needHelpService.getById(token.substring(7), id);
    }

    @PatchMapping
    public ResponseEntity<ResponseHandling> updateStatusHelp(@RequestHeader(HttpHeaders.AUTHORIZATION) String token, @RequestBody NeedHelp help){
        return needHelpService.updateStatusHelp(token.substring(7), help);
    }
}
