package mii.bsi.apiportal.service;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import mii.bsi.apiportal.domain.User;
import mii.bsi.apiportal.repository.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User create(User user) {
        try {
            String sequence = userRepository.getUserSequence();
            String pattern = "yyyyMMddHHmmss";
            SimpleDateFormat timestamp = new SimpleDateFormat(pattern);
            String date = timestamp.format(new Date());
            String idUser = date.concat(StringUtils.leftPad(sequence, 4, "0"));
            user.setId(idUser);
            userRepository.save(user);
        } catch (Exception e) {
            
        }
        return null;
    }

    public Iterable<User> getAll() {
        return userRepository.findAll();
    }

    public User getById(String id) {
        return userRepository.findById(id).get();
    }
}
