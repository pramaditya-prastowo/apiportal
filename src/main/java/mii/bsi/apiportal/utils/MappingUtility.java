package mii.bsi.apiportal.utils;

import mii.bsi.apiportal.constant.MappingUtils;
import mii.bsi.apiportal.domain.model.FilterGetData;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class MappingUtility implements MappingUtils {

    @Override
    public FilterGetData needsHelpMap(Integer pageNumber, Integer pageSize, String orderBy, String sort,
                                      String email, String name, String noHp, String companyName, String status) {
        Map<String, String> filterBy = new HashMap<String, String>(){{
            put("email", email);
            put("name", name);
            put("mobile_phone", noHp);
            put("company_name", companyName);
            put("status", status);
        }};

        return new FilterGetData(pageNumber, pageSize, orderBy, sort, filterBy);
    }

    @Override
    public FilterGetData usersMap(Integer pageNumber, Integer pageSize, String orderBy, String sort,
                                  String email, String name, String accountActive, String principal) {
        Map<String, String> filterBy = new HashMap<String,String>(){{
            put("email", email);
            put("name", name);
            put("account_inactive", accountActive);
            put("auth_principal", principal);
        }};
        
        return new FilterGetData(pageNumber, pageSize, orderBy, sort, filterBy);
    }
}
