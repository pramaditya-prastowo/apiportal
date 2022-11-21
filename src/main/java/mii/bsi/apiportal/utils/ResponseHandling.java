package mii.bsi.apiportal.utils;

import java.util.ArrayList;
import java.util.List;

public class ResponseHandling<T> {
    private String responseCode;
    private String reseponseMessage;
    private List<String> messageError = new ArrayList<>();
    private T payload;

    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    public String getReseponseMessage() {
        return reseponseMessage;
    }

    public void setReseponseMessage(String reseponseMessage) {
        this.reseponseMessage = reseponseMessage;
    }

    public List<String> getMessageError() {
        return messageError;
    }

    public void setMessageError(List<String> messageError) {
        this.messageError = messageError;
    }

    public T getPayload() {
        return payload;
    }

    public void setPayload(T payload) {
        this.payload = payload;
    }

}
