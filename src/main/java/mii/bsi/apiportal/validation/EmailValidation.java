package mii.bsi.apiportal.validation;

import mii.bsi.apiportal.domain.BsmApiConfig;
import mii.bsi.apiportal.service.BsmApiConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
@Service
public class EmailValidation {


    @Autowired
    private BsmApiConfigService apiConfigService;

//    private List<String> blackListEmail = Arrays.asList("gmail","outlook", "yahoo", "hotmail","zoho", "mail","email",
//            "aol", "yandex","lycos", "protonmail","tutanota", "tutamail","tuta","keemail","icloud");

    public boolean validEmail(String email){
        BsmApiConfig bsmApiConfig = apiConfigService.getConfig("email.blacklist", "EMAIL");

        String[] blackListEmail = bsmApiConfig.getValue().split("[,]");

        String[] splitEmail = email.split("@");
        String[] splitDomain = splitEmail[1].split("[.]");
        for (String data: blackListEmail) {
            if(data.equals(splitDomain[0])){
                return false;
            }
        }
        return true;
    }
}
