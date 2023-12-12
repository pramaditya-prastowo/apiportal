package mii.bsi.apiportal.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RowDataResponse <T>{
    private Integer rowData;
    private Integer totalData;
    private List<T> data;
}
