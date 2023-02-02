package mii.bsi.apiportal.repository.dao.impl;

import mii.bsi.apiportal.domain.ApplicationServiceApi;
import mii.bsi.apiportal.repository.dao.DataDao;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class AppServiceApiDao extends DataDao {
    public AppServiceApiDao(JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
    }

    public List<ApplicationServiceApi> getServiceApiByAppId(Long appId){
        return super.getJdbcTemplate()
                .query("SELECT a.*, b.service_name FROM bsi_apps_service_api_portal a left join bsi_service_api_api_portal b on a.service_api_id = b.id WHERE app_id = ?",
                        new Object[]{ appId },
                        (rs, rowNum) -> new ApplicationServiceApi(
                                rs.getLong("id"),
                                rs.getLong("app_id"),
                                rs.getLong("service_api_id"),
                                rs.getString("service_name")
                        )
                );
    }
}
