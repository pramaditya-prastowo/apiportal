package mii.bsi.apiportal.constant.sql;

import mii.bsi.apiportal.domain.model.FilterGetData;

public interface BuilderQuery {
    String getWithFilter(String sql, String order, FilterGetData filter);
}
