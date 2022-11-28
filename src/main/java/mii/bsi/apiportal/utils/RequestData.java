package mii.bsi.apiportal.utils;

import lombok.Data;

@Data
public class RequestData<T> {
    private T payload;
}
