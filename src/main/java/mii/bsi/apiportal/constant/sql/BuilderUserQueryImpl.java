package mii.bsi.apiportal.constant.sql;

import mii.bsi.apiportal.domain.model.FilterGetData;
import org.springframework.stereotype.Component;

@Component
public class BuilderUserQueryImpl implements BuilderUserQuery {

    public String getWithFilter(String sql, String order, FilterGetData filter){
        String query = sql;
        if (!"".equalsIgnoreCase((String) filter.getFilter().get("email")) && filter.getFilter().get("email")!=null) {
            String bankKeyFilter = "AND a.email = :email ";
            query += bankKeyFilter;
        }
        if (!"".equalsIgnoreCase((String) filter.getFilter().get("first_name")) && filter.getFilter().get("first_name")!=null) {
            String paymentMethodFilter = "AND a.first_name = :first_name ";
            query += paymentMethodFilter;
        }
        if (!filter.getFilter().get("account_inactive").equals("1")) {
            String paymentMethodFilter = "AND a.account_inactive IS TRUE ";
            query += paymentMethodFilter;
        }else{
            String paymentMethodFilter = "AND a.account_inactive IS NOT TRUE ";
            query += paymentMethodFilter;
        }
        if (!"".equalsIgnoreCase((String) filter.getFilter().get("auth_principal")) && filter.getFilter().get("auth_principal")!=null) {
            String paymentMethodFilter = "AND a.auth_principal = :auth_principal ";
            query += paymentMethodFilter;
        }

        return query + order;
    }
}
