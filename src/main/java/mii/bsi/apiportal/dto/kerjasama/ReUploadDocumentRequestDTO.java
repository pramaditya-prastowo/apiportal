package mii.bsi.apiportal.dto.kerjasama;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ReUploadDocumentRequestDTO {
    @NotEmpty(message = "idDoc is required")
    private Long idDoc;
    @NotEmpty(message = "keynameDoc is required")
    private String keynameDoc;
    @NotEmpty(message = "nameDoc is required")
    private String nameDoc;

}
