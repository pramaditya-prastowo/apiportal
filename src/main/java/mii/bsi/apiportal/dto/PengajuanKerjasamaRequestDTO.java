package mii.bsi.apiportal.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import mii.bsi.apiportal.domain.DocKerjasama;
import mii.bsi.apiportal.domain.PengajuanKerjasama;
import mii.bsi.apiportal.domain.ServiceApiDomain;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PengajuanKerjasamaRequestDTO {
    private PengajuanKerjasama pengajuan;
    private DocKerjasama docPengajuan;
    private List<ServiceApiDomain> selectedService;
}
