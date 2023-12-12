package mii.bsi.apiportal.service;

import mii.bsi.apiportal.constant.sql.MyPagination;
import mii.bsi.apiportal.domain.model.FilterGetData;
import mii.bsi.apiportal.utils.QueryUtils;
import mii.bsi.apiportal.utils.RowDataResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class PaginationService<T> implements MyPagination {
    @Autowired
    private LogService logService;
    @Autowired
    private QueryUtils queryUtils;
    @Autowired
    private NamedParameterJdbcTemplate jdbc;

    @Override
    public RowDataResponse<T> getData(FilterGetData filter, String sql, Class type) {
        MapSqlParameterSource paramSource = new MapSqlParameterSource();
        for(Map.Entry<String,String> itr : filter.getFilter().entrySet()){
            paramSource.addValue(itr.getKey(), itr.getValue());
        }

        paramSource.addValue("pageNumber", filter.getPageNumber());
        paramSource.addValue("pageSize", filter.getPageSize());


        String pagination = queryUtils.generatePagination(sql, filter.getOrderBy(), filter.getSort());
        logService.logQuery("\r\nsql = \r\n" + pagination);

        String total = queryUtils.countTotalRows(sql);
        String totalRecord = jdbc.queryForObject(total, paramSource, String.class);
        List<T> dataRecord = jdbc.query(pagination, paramSource,
                BeanPropertyRowMapper.newInstance(type));

        RowDataResponse<T> rowDataResponse = new RowDataResponse<>();
        rowDataResponse.setRowData(dataRecord.size());
        rowDataResponse.setData(dataRecord);
        rowDataResponse.setTotalData(Integer.parseInt(totalRecord));
        return rowDataResponse;
    }
}
