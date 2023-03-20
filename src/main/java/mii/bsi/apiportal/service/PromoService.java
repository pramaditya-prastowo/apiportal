package mii.bsi.apiportal.service;

import mii.bsi.apiportal.domain.User;
import mii.bsi.apiportal.validation.UserValidation;
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

import java.util.Date;

@Service
public class PromoService {
    @Autowired
    private LogService logService;
    @Autowired
    UserValidation adminValidation;
    @Autowired
    private PromoRepository promoRepository;

    public static final String CREATE = "Create";
    public static final String UPDATE = "Update";
    public static final String GET_ALL = "Get All";
    public static final String GETBYID = "Get By Id";
    public static final String DELETE = "DELETE By Id";

    public ResponseEntity<ResponseHandling> create(String token, Promo promo, Errors errors) {
        ResponseHandling<Promo> responseData = new ResponseHandling<>();
        RequestData<Promo> requestData = new RequestData<>();

        try {

            if(!adminValidation.isAdmin(token)){
                responseData.failed("Access denied");
                logService.saveLog(requestData, responseData, StatusCode.FORBIDDEN, this.getClass().getName(),
                        CREATE);
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(responseData);
            }

            if (errors.hasErrors()) {
                responseData.failed(CustomError.validRequest(errors), "Bad Request");
                logService.saveLog(requestData, responseData, StatusCode.BAD_REQUEST, this.getClass().getName(),
                        CREATE);
            }

            User user = adminValidation.getUserFromToken(token);
            promo.setCreateDate(new Date());
            promo.setCreateBy(user.getId());

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

    public ResponseEntity<ResponseHandling> update(String token, Promo promo, Errors errors) {
        ResponseHandling<Promo> responseData = new ResponseHandling<>();
        RequestData<Promo> requestData = new RequestData<>();

        try {

            if(!adminValidation.isAdmin(token)){
                responseData.failed("Access denied");
                logService.saveLog(requestData, responseData, StatusCode.FORBIDDEN, this.getClass().getName(),
                        UPDATE);
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(responseData);
            }

            if (errors.hasErrors()) {
                responseData.failed(CustomError.validRequest(errors), "Bad Request");
                logService.saveLog(requestData, responseData, StatusCode.BAD_REQUEST, this.getClass().getName(),
                        UPDATE);
            }

            User user = adminValidation.getUserFromToken(token);
            Promo promoDb = promoRepository.findByIdPromo(promo.getId());
            if(promoDb == null){
                responseData.failed(CustomError.validRequest(errors), "Bad Request");
                logService.saveLog(requestData, responseData, StatusCode.BAD_REQUEST, this.getClass().getName(),
                        UPDATE);
            }
            promo.setUpdateDate(new Date());
            promo.setUpdateBy(user.getId());
            promo.setCreateBy(promoDb.getCreateBy());
            promo.setCreateDate(promoDb.getCreateDate());

            promoRepository.save(promo);
            responseData.success();
        } catch (Exception e) {
            responseData.failed(e.getMessage());
            logService.saveLog(requestData, responseData, StatusCode.INTERNAL_SERVER_ERROR, this.getClass().getName(),
                    UPDATE);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseData);
        }

        logService.saveLog(requestData, responseData, StatusCode.OK, this.getClass().getName(), UPDATE);
        return ResponseEntity.status(HttpStatus.OK).body(responseData);
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
        logService.saveLog(requestData, responseData, StatusCode.OK, this.getClass().getName(), GET_ALL);
        return ResponseEntity.status(HttpStatus.OK).body(responseData);
    }

    public ResponseEntity<ResponseHandling<Promo>> getById(String id) {
        ResponseHandling<Promo> responseData = new ResponseHandling<>();
        RequestData<Promo> requestData = new RequestData<>();

        try {
            responseData.setPayload(promoRepository.findByIdPromo(Long.parseLong(id)));
            responseData.success();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseData);
        }
        logService.saveLog(requestData, responseData, StatusCode.OK, this.getClass().getName(), GETBYID);
        return ResponseEntity.status(HttpStatus.OK).body(responseData);
    }

    public ResponseEntity<ResponseHandling> deleteById(Long id, String token){
        ResponseHandling<Promo> responseData = new ResponseHandling<>();
        RequestData<Promo> requestData = new RequestData<>();

        try {
            if(!adminValidation.isAdmin(token)){
                responseData.failed("Access denied");
                logService.saveLog(requestData, responseData, StatusCode.FORBIDDEN, this.getClass().getName(),
                        DELETE);
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(responseData);
            }

            Promo promoDb = promoRepository.findByIdPromo(id);
            if(promoDb == null){
                responseData.failed("Not Found");
                logService.saveLog(requestData, responseData, StatusCode.NOT_FOUND, this.getClass().getName(),
                        DELETE);
            }

            promoRepository.deleteById(id);
            responseData.success();
        } catch (Exception e) {
            responseData.failed(e.getMessage());
            logService.saveLog(requestData, responseData, StatusCode.INTERNAL_SERVER_ERROR, this.getClass().getName(),
                    DELETE);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseData);
        }

        logService.saveLog(requestData, responseData, StatusCode.OK, this.getClass().getName(), DELETE);
        return ResponseEntity.status(HttpStatus.OK).body(responseData);
    }
}
