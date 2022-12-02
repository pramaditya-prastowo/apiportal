package mii.bsi.apiportal.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;

import mii.bsi.apiportal.constant.StatusCode;
import mii.bsi.apiportal.domain.Promo;
import mii.bsi.apiportal.repository.PromoRepository;
import mii.bsi.apiportal.utils.CustomError;
import mii.bsi.apiportal.utils.RequestData;
import mii.bsi.apiportal.utils.ResponseHandling;

@Service
public class PromoService {
    @Autowired
    private LogService logService;

    @Autowired
    private PromoRepository promoRepository;

    public static final String CREATE = "Create";
    public static final String GETALL = "Get All";
    public static final String GETBYID = "Get By Id";

    public ResponseEntity<ResponseHandling<Promo>> create(Promo promo, Errors errors) {
        ResponseHandling<Promo> responseData = new ResponseHandling<>();
        RequestData<Promo> requestData = new RequestData<>();

        try {
            if (errors.hasErrors()) {
                responseData.failed(CustomError.validRequest(errors), "Bad Request");
                logService.saveLog(requestData, responseData, StatusCode.BAD_REQUEST, this.getClass().getName(),
                        CREATE);
            }

            promoRepository.save(promo);
            responseData.success();
        } catch (Exception e) {
            responseData.failed(e.getMessage());
            logService.saveLog(requestData, responseData, StatusCode.INTERNAL_SERVER_ERROR, this.getClass().getName(),
                    CREATE);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseData);
        }

        logService.saveLog(requestData, responseData, StatusCode.CREATED, this.getClass().getName(), CREATE);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseData);
    }

    public ResponseEntity<ResponseHandling<Iterable<Promo>>> getAll() {
        ResponseHandling<Iterable<Promo>> responseData = new ResponseHandling<>();
        RequestData<Promo> requestData = new RequestData<>();

        try {
            responseData.setPayload(promoRepository.findAll());
            responseData.success();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseData);
        }
        logService.saveLog(requestData, responseData, StatusCode.OK, this.getClass().getName(), GETALL);
        return ResponseEntity.status(HttpStatus.OK).body(responseData);
    }

    public ResponseEntity<ResponseHandling<Promo>> getById(String id) {
        ResponseHandling<Promo> responseData = new ResponseHandling<>();
        RequestData<Promo> requestData = new RequestData<>();

        try {
            responseData.setPayload(promoRepository.findById(id).get());
            responseData.success();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseData);
        }
        logService.saveLog(requestData, responseData, StatusCode.OK, this.getClass().getName(), GETBYID);
        return ResponseEntity.status(HttpStatus.OK).body(responseData);
    }
}
