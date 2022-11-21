package mii.bsi.apiportal.service;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;

import mii.bsi.apiportal.domain.User;
import mii.bsi.apiportal.repository.UserRepository;
import mii.bsi.apiportal.utils.ResponseHandling;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public ResponseHandling<User> create(@Valid User user, Errors errors) {
        ResponseHandling<User> responseHandling = new ResponseHandling<>();
        System.out.println(user);
        try {
            String sequence = userRepository.getUserSequence();
            String pattern = "yyyyMMddHHmmss";
            SimpleDateFormat timestamp = new SimpleDateFormat(pattern);
            String date = timestamp.format(new Date());
            String idUser = date.concat(StringUtils.leftPad(sequence, 4, "0"));
            user.setId(idUser);
            user.setAccountInactive("FALSE");
            user.setAccountLokced("FALSE");
            user.setRetryPasswordCount(0);
            responseHandling.setPayload(userRepository.save(user));
            responseHandling.setResponseCode("00");
            responseHandling.setReseponseMessage("success");
        } catch (Exception e) {
            errors.hasErrors();
            for (ObjectError err : errors.getAllErrors()) {
                responseHandling.getMessageError().add(err.getDefaultMessage());
            }
            responseHandling.setResponseCode("99");
            responseHandling.setReseponseMessage("failed");
            responseHandling.setPayload(user);
        }
        return responseHandling;
    }

    public ResponseHandling<User> update(User user) {
        ResponseHandling<User> responseHandling = new ResponseHandling<>();
        try {
            String pattern = "yyyyMMddHHmmss";
            SimpleDateFormat timestamp = new SimpleDateFormat(pattern);
            String date = timestamp.format(new Date());
            user.getId();
            user.setUpdateDate(date);
            responseHandling.setPayload(userRepository.save(user));
            responseHandling.setResponseCode("00");
            responseHandling.setReseponseMessage("success");
        } catch (Exception e) {
            responseHandling.setResponseCode("99");
            responseHandling.setReseponseMessage("failed");
            responseHandling.setPayload(user);
        }
        return responseHandling;
    }

    public ResponseHandling<Iterable<User>> getAll() {
        ResponseHandling<Iterable<User>> responseHandling = new ResponseHandling<>();
        try {
            responseHandling.setPayload(userRepository.findAll());
            responseHandling.setResponseCode("00");
            responseHandling.setReseponseMessage("success");
        } catch (Exception e) {
            responseHandling.setReseponseMessage("failed");
            responseHandling.setResponseCode("99");
        }
        return responseHandling;
    }

    public ResponseHandling<User> getById(String id) {
        ResponseHandling<User> responseHandling = new ResponseHandling<>();
        try {
            responseHandling.setPayload(userRepository.findById(id).get());
            responseHandling.setResponseCode("00");
            responseHandling.setReseponseMessage("success");
        } catch (Exception e) {
            responseHandling.setResponseCode("99");
            responseHandling.setReseponseMessage("failed");
        }
        return responseHandling;
    }
}
