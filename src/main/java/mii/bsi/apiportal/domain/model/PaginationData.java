package mii.bsi.apiportal.domain.model;

import mii.bsi.apiportal.utils.RowDataResponse;

import java.util.List;

public class PaginationData <T>{
    private String mainQuery;
    private String paginationQuery;
    private String totalMainData;
    private List<T> dataResult;

    public RowDataResponse<T> getRowDataResponse(){
        RowDataResponse<T> result = new RowDataResponse<>();
        result.setRowData(dataResult.size());
        result.setData(dataResult);
        result.setTotalData(Integer.parseInt(this.totalMainData));
        return result;
    }

}
