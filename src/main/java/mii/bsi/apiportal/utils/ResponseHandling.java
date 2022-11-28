package mii.bsi.apiportal.utils;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ResponseHandling<T> {
    private boolean error;
    private String responseCode;
    private String responseMessage;
    private List<String> messageError = new ArrayList<>();
    private T payload;

    public void success(){
        this.error = false;
        this.responseCode = "00";
        this.responseMessage = "Success";
    }
    public void success(String message){
        this.error = false;
        this.responseCode = "00";
        this.responseMessage = "Success";
    }

    public void failed(){
        this.error = true;
        this.responseCode = "01";
        this.responseMessage = "Failed";
    }

    public void failed(List<String> errors, String responseMessage){
        this.error = true;
        this.responseCode = "01";
        this.responseMessage = responseMessage;
        this.messageError = errors;
    }

    public void failed(String message){
        this.error = true;
        this.responseCode = "01";
        this.responseMessage = message;
    }

    public void addMessageError(String message){
        this.messageError.add(message);
    }

}
