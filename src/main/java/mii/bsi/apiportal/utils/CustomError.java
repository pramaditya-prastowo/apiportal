package mii.bsi.apiportal.utils;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;

import java.util.ArrayList;
import java.util.List;

public class CustomError {

    public static List<String> validRequest(Errors errors){
        List<String> errorList = new ArrayList<>();
        if(errors.hasErrors()){
            for (ObjectError error : errors.getAllErrors()){
                errorList.add(error.getDefaultMessage());
            }
        }
        return errorList;
    }

    public static boolean isValid(List<String> list){
        if(list.isEmpty()) {
            return true;
        }
        return false;
    }
}
