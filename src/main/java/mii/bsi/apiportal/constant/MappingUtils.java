package mii.bsi.apiportal.constant;

import mii.bsi.apiportal.domain.model.FilterGetData;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.Map;

public interface MappingUtils {

    FilterGetData needsHelpMap(Integer pageNumber, Integer pageSize, String orderBy, String sort,
                                      String email, String name, String noHp,
                                      String companyName, String status);

    FilterGetData usersMap(Integer pageNumber, Integer pageSize, String orderBy, String sort,
                           String email, String name, String accountActive, String principal);
}
