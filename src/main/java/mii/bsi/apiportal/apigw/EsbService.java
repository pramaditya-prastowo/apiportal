package mii.bsi.apiportal.apigw;

import com.fasterxml.jackson.databind.ObjectMapper;
import mii.bsi.apiportal.apigw.dto.CreateAppRequestDTO;
import mii.bsi.apiportal.apigw.model.RequestHeaderApi;
import mii.bsi.apiportal.apigw.model.ResponseApiGw;
import mii.bsi.apiportal.domain.BsmApiConfig;
import mii.bsi.apiportal.service.BsmApiConfigServiceImpl;
import mii.bsi.apiportal.service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Map;
@Service
public class EsbService {

    @Autowired
    private BsmApiConfigServiceImpl configService;
    @Value("${endpoint.insert.application}")
    private String insertApplicationPath;
    @Autowired
    private DataApiClientEsb dataApiClient;
    @Autowired
    private LogService logService;

    public ResponseApiGw createApplication(CreateAppRequestDTO request) {

        BsmApiConfig hostApiGw = configService.getConfigByKeyName("base.url.esbsandbox");
        String url = hostApiGw.getValue() + insertApplicationPath;
        System.out.println("URL : " + url);
        ResponseApiGw responseApiGw;
        try {
            ObjectMapper oMapper = new ObjectMapper();
            Map<String, String> reqMap = oMapper.convertValue(request, Map.class);
            responseApiGw = dataApiClient.insertApplication(url, new RequestHeaderApi(), logService.jsonToString(reqMap));

            return responseApiGw;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }




}
