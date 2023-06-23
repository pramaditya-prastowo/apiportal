package mii.bsi.apiportal.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import mii.bsi.apiportal.domain.model.ApprovalStatus;

import java.util.Date;

@AllArgsConstructor
@Data
public class LogPengajuanKerjasamaDTO {

    private Long id;

    private Date createdDate;

    private String idUser;
    private String name;
    private Long pekerId;
    private String message;
    private String description;
    private ApprovalStatus status;
}
