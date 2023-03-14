package mii.bsi.apiportal.apigw;

import mii.bsi.apiportal.apigw.dto.CreateAppRequestDTO;
import mii.bsi.apiportal.apigw.dto.RequestTokenResponseDTO;
import mii.bsi.apiportal.utils.ResponseHandling;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api-gw/v1.0")
public class ApiGatewayController {

    @Autowired
    private ApiGatewayService apiGatewayService;

    @PostMapping("/application")
    public String createApplication(@RequestBody CreateAppRequestDTO request){
        return apiGatewayService.createApplication(request);
    }

    @PostMapping("/request-token")
    public ResponseEntity<ResponseHandling<RequestTokenResponseDTO>> requestToken(@RequestHeader(HttpHeaders.AUTHORIZATION) String token){
        return apiGatewayService.requestTokenB2B(token.substring(7));
    }
}
