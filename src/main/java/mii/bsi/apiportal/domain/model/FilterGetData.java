package mii.bsi.apiportal.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FilterGetData {
    private Integer pageNumber;
    private Integer pageSize;
    private String orderBy;
    private String sort;
    private Map<String, String> filter;
}
