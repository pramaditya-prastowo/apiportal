package mii.bsi.apiportal.apigw;

import mii.bsi.apiportal.apigw.model.ResponseApiGw;
import mii.bsi.apiportal.constant.StatusCode;
import mii.bsi.apiportal.utils.ResponseHandling;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api-gw/v1.0/dashboard")
public class DashboardAPIController {

    @Autowired
    private DashboardAPIService dashboardAPIService;

//    @GetMapping("totalServiceDaily")
//    public ResponseEntity<ResponseHandling<String>> totalServiceDaily(){
//        return dashboardAPIService.totalServiceDaily();
//    }
    @PostMapping("totalServiceByCorpId")
    public ResponseEntity<ResponseHandling<String>> totalServiceByCorpId(@RequestBody String corpId){
        return dashboardAPIService.totalServiceByCorpId(corpId);
    }
    @PostMapping("getTotalHitApiByCorpId")
    public ResponseEntity<ResponseHandling<String>> getTotalHitApiByCorpId(@RequestBody String corpId){
        return dashboardAPIService.getTotalHitApiByCorpId(corpId);
    }
    @GetMapping("detailHitServiceDaily")
    public ResponseEntity<ResponseHandling<String>> detailHitServiceDaily(){
        return dashboardAPIService.detailHitServiceDaily();
    }
    @PostMapping("detailHitServiceMonthly")
    public ResponseEntity<ResponseHandling<String>> detailHitServiceMonthly(@RequestBody String month){
        return dashboardAPIService.detailHitServiceMonthly(month);
    }
    @GetMapping("totalMitraPerService")
    public ResponseEntity<ResponseHandling<String>> totalMitraPerService(){
        return dashboardAPIService.totalMitraPerService();
    }
    @GetMapping("getTotalMitraByApi")
    public ResponseEntity<ResponseHandling<String>> getTotalMitraByApi(){
        return dashboardAPIService.getTotalMitraByApi();
    }
    @GetMapping("getTotalHitServiceByServiceName")
    public ResponseEntity<ResponseHandling<String>> getTotalHitServiceByServiceName(){
        return dashboardAPIService.getTotalHitServiceByServiceName();
    }
    @PostMapping("totalHitServiceMonthly")
    public ResponseEntity<ResponseHandling<String>> totalHitServiceMonthly(@RequestBody String month){
        return dashboardAPIService.totalHitServiceMonthly(month);
    }
}
