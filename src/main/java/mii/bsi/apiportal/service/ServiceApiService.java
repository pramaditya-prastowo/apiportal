package mii.bsi.apiportal.service;

import java.util.Date;

import mii.bsi.apiportal.utils.JwtUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;

import mii.bsi.apiportal.constant.StatusCode;
import mii.bsi.apiportal.domain.ServiceApiDomain;
import mii.bsi.apiportal.repository.ServiceApiRepository;
import mii.bsi.apiportal.utils.CustomError;
import mii.bsi.apiportal.utils.RequestData;
import mii.bsi.apiportal.utils.ResponseHandling;

@Service
public class ServiceApiService {
    @Autowired
    private LogService logService;

    @Autowired
    private ServiceApiRepository serviceApiRepository;

    @Autowired
    private JwtUtility jwtUtility;

    public static final String CREATE = "Create";
    public static final String GETALL = "Get All";
    public static final String GETBYID = "Get By Id";

    public ResponseEntity<ResponseHandling<ServiceApiDomain>> create(ServiceApiDomain serviceApi,String token, Errors errors) {
        ResponseHandling<ServiceApiDomain> responseData = new ResponseHandling<>();
        RequestData<ServiceApiDomain> requestData = new RequestData<>();
        requestData.setPayload(serviceApi);

        try {
            if (errors.hasErrors()) {
                responseData.failed(CustomError.validRequest(errors), "Bad Request");
                logService.saveLog(requestData, responseData, StatusCode.BAD_REQUEST, this.getClass().getName(),
                        CREATE);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseData);
            }
            final String username = jwtUtility.getUsernameFromToken(token);
            serviceApi.setCreateDate(new Date());
            serviceApi.setCreateBy(username.split("@")[0]);


            serviceApiRepository.save(serviceApi);
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

    public ResponseEntity<ResponseHandling<ServiceApiDomain>> update(ServiceApiDomain serviceApi, Errors errors) {
        ResponseHandling<ServiceApiDomain> responseData = new ResponseHandling<>();
        RequestData<ServiceApiDomain> requestData = new RequestData<>();
        requestData.setPayload(serviceApi);

        try {
            if (errors.hasErrors()) {
                responseData.failed(CustomError.validRequest(errors), "Bad Request");
                logService.saveLog(requestData, responseData, StatusCode.BAD_REQUEST, this.getClass().getName(),
                        CREATE);
            }
            serviceApi.getId();
            serviceApi.setUpdateDate(new Date());
            serviceApiRepository.save(serviceApi);
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

    public ResponseEntity<ResponseHandling<Iterable<ServiceApiDomain>>> getAll() {
        ResponseHandling<Iterable<ServiceApiDomain>> responseData = new ResponseHandling<>();
        RequestData<ServiceApiDomain> requestData = new RequestData<>();
        try {
            responseData.setPayload(serviceApiRepository.findAll());
            responseData.success();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseData);
        }
        logService.saveLog(requestData, responseData, StatusCode.OK, this.getClass().getName(), GETALL);
        return ResponseEntity.status(HttpStatus.OK).body(responseData);
    }

    public ResponseEntity<ResponseHandling<ServiceApiDomain>> getById(String id) {
        ResponseHandling<ServiceApiDomain> responseData = new ResponseHandling<>();
        RequestData<ServiceApiDomain> requestData = new RequestData<>();
        try {
            responseData.setPayload(serviceApiRepository.findById(id.toString()).get());
            responseData.success();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseData);
        }
        logService.saveLog(requestData, responseData, StatusCode.OK, this.getClass().getName(), GETBYID);
        return ResponseEntity.status(HttpStatus.OK).body(responseData);
    }
}
