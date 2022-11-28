package mii.bsi.apiportal.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import mii.bsi.apiportal.constant.StatusCode;
import org.springframework.http.ResponseEntity;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ValidationResponse<T> {
    private boolean valid;
    private ResponseEntity<ResponseHandling<T>> response;
    private StatusCode statusCode;
}
