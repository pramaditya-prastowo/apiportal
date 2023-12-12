package mii.bsi.apiportal.constant.sql;

import mii.bsi.apiportal.domain.model.FilterGetData;
import org.springframework.stereotype.Component;

@Component
public class BuilderHelpQueryImpl implements BuilderHelpQuery{
    @Override
    public String getWithFilter(String sql, String order, FilterGetData filter) {
        String query = sql;
        if (!"".equalsIgnoreCase((String) filter.getFilter().get("email")) && filter.getFilter().get("email")!=null) {
            String bankKeyFilter = "AND a.email = :email ";
            query += bankKeyFilter;
        }
        if (!"".equalsIgnoreCase((String) filter.getFilter().get("name")) && filter.getFilter().get("name")!=null) {
            String paymentMethodFilter = "AND a.name = :name ";
            query += paymentMethodFilter;
        }
        if (!"".equalsIgnoreCase((String) filter.getFilter().get("mobile_phone")) && filter.getFilter().get("mobile_phone")!=null) {
            String paymentMethodFilter = "AND a.mobile_phone = :mobile_phone ";
            query += paymentMethodFilter;
        }
        if (!"".equalsIgnoreCase((String) filter.getFilter().get("company_name")) && filter.getFilter().get("company_name")!=null) {
            String paymentMethodFilter = "AND a.company_name = :company_name ";
            query += paymentMethodFilter;
        }
        if (!"".equalsIgnoreCase((String) filter.getFilter().get("status")) && filter.getFilter().get("status")!=null) {
            int status = Integer.parseInt((String) filter.getFilter().get("status"));
            String paymentMethodFilter = "AND a.status = " + status;
            query += paymentMethodFilter;
        }
        return query + order;
    }
}