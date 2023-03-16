package mii.bsi.apiportal.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SendEmail {

    private String subject;
    private String from;
    private String to;
    private String text;
}
