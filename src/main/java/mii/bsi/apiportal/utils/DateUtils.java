package mii.bsi.apiportal.utils;

import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@Component
public class DateUtils {
    public String dateIsoString(Date date){
        OffsetDateTime offsetDateTime = OffsetDateTime.ofInstant(date.toInstant(), ZoneOffset.UTC);
        DateTimeFormatter outputFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssXXX");
        String formattedDate = offsetDateTime.format(outputFormat).replaceAll("Z","+00:00");

        return formattedDate;
    }
}
