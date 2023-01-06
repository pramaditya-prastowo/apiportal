package mii.bsi.apiportal.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CountDashboard {
    private String title;
    private int count;
    private String uri;
}
