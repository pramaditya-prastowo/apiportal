package mii.bsi.apiportal.constant.sql;

import mii.bsi.apiportal.domain.model.FilterGetData;
import mii.bsi.apiportal.utils.RowDataResponse;

import java.util.List;

public interface MyPagination<T>{

    RowDataResponse<T> getData(FilterGetData filter, String sql, Class type);
}
