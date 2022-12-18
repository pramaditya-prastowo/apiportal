package mii.bsi.apiportal.service;

import mii.bsi.apiportal.domain.Icon;
import mii.bsi.apiportal.repository.IconRepository;
import mii.bsi.apiportal.utils.RequestData;
import mii.bsi.apiportal.utils.ResponseHandling;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;

import java.util.Date;

@Service
public class IconService {

    @Autowired
    private IconRepository iconRepository;

    public ResponseEntity<ResponseHandling> create(Icon icon, String token, Errors errors){
        ResponseHandling responseData = new ResponseHandling();
        RequestData<Icon> requestData = new RequestData<>();
        requestData.setPayload(icon);


        try {

            icon.setCreateBy("");
            icon.setCreateDate(new Date());
            iconRepository.save(icon);

        }catch (Exception e){
        }

        return ResponseEntity.ok(new ResponseHandling<>());
    }
}
