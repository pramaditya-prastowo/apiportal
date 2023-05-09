package mii.bsi.apiportal.service;

import mii.bsi.apiportal.constant.StatusCode;
import mii.bsi.apiportal.domain.*;
import mii.bsi.apiportal.domain.model.Roles;
import mii.bsi.apiportal.domain.model.StatusKerjasama;
import mii.bsi.apiportal.repository.DocKerjasamaRepository;
import mii.bsi.apiportal.repository.KerjasamaServiceApiRepository;
import mii.bsi.apiportal.repository.LogPengajuanKerjasamaRepository;
import mii.bsi.apiportal.repository.PengajuanKerjasamaRepository;
import mii.bsi.apiportal.utils.CustomError;
import mii.bsi.apiportal.utils.RequestData;
import mii.bsi.apiportal.utils.ResponseHandling;
import mii.bsi.apiportal.validation.UserValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;

import java.util.*;

@Service
public class PengajuanKerjasamaService {

    @Autowired
    private LogService logService;
    @Autowired
    private UserValidation userValidation;

    @Autowired
    private PengajuanKerjasamaRepository pengajuanKerjasamaRepository;
    @Autowired
    private DocKerjasamaRepository docKerjasamaRepository;
    @Autowired
    private KerjasamaServiceApiRepository kerjasamaServiceApiRepository;
    @Autowired
    private LogPengajuanKerjasamaRepository logPengajuanKerjasamaRepository;

    public static final String CREATE = "Create";
    public static final String GET_ALL = "Get All";
    public static final String GET_BY_ID = "Get By ID";
    public static final String UPDATE_STATUS = "Update Status";

    public ResponseEntity<ResponseHandling<List<PengajuanKerjasama>>> getAllPengajuanKerjasama(String token, String userId) {
        ResponseHandling<List<PengajuanKerjasama>> responseData = new ResponseHandling<>();
        RequestData<Map<String, Object>> requestData = new RequestData<>();
        if(!"".equals(userId)){
            requestData.setPayload(logService.setValueRequest("userId", userId));
        }
        try {
            User user = userValidation.getUserFromToken(token);
            if(user == null){
                responseData.failed("User not found");
                logService.saveLog(requestData, responseData, StatusCode.NOT_FOUND, this.getClass().getName(),
                        CREATE);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseData);
            }

            List<PengajuanKerjasama> kerjasamaList = new ArrayList<>();
            if(userId.equals("") && !user.getAuthPrincipal().equals(Roles.MITRA)){
                kerjasamaList = pengajuanKerjasamaRepository.findAll();
            }else{
                kerjasamaList = pengajuanKerjasamaRepository.findByCreatedBy(userId);
            }
            for (PengajuanKerjasama data: kerjasamaList) {
                List<KerjasamaServiceApi> serviceApis = kerjasamaServiceApiRepository.findByPekerId(data.getId());
                data.setServices(serviceApis);
            }

            responseData.success();
            responseData.setPayload(kerjasamaList);

        }catch (Exception e){
            e.printStackTrace();
            responseData.failed(e.getMessage());
            logService.saveLog(requestData, responseData, StatusCode.INTERNAL_SERVER_ERROR, this.getClass().getName(),
                    GET_ALL);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseData);
        }
//        logService.saveLog(requestData, responseData, StatusCode.OK, this.getClass().getName(),
//                GET_ALL);
        return ResponseEntity.status(HttpStatus.OK).body(responseData);
    }

    public ResponseEntity<ResponseHandling<PengajuanKerjasama>> getPengajuanKerjasamaById(String token, Long id) {
        ResponseHandling<PengajuanKerjasama> responseData = new ResponseHandling<>();
        RequestData<Map<String, Object>> requestData = new RequestData<>();
        requestData.setPayload(logService.setValueRequest("id", id));
        try {
            User user = userValidation.getUserFromToken(token);

            if(user == null){
                responseData.failed("User not found");
                logService.saveLog(requestData, responseData, StatusCode.NOT_FOUND, this.getClass().getName(),
                        CREATE);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseData);
            }

            PengajuanKerjasama kerjasama = pengajuanKerjasamaRepository.findById(id).get();

            List<KerjasamaServiceApi> serviceApis = kerjasamaServiceApiRepository.findByPekerId(kerjasama.getId());
            kerjasama.setServices(serviceApis);
            responseData.setPayload(kerjasama);
            responseData.success();

        }catch (Exception e){
            e.printStackTrace();
            responseData.failed(e.getMessage());
            logService.saveLog(requestData, responseData, StatusCode.INTERNAL_SERVER_ERROR, this.getClass().getName(),
                    GET_BY_ID);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseData);
        }
//        logService.saveLog(requestData, responseData, StatusCode.OK, this.getClass().getName(),
//                GET_BY_ID);
        return ResponseEntity.status(HttpStatus.OK).body(responseData);

    }

    public ResponseEntity<ResponseHandling> addPengajuanKerjasama(PengajuanKerjasama pengajuanKerjasama, String token, Errors errors) {
        ResponseHandling responseData = new ResponseHandling<>();
        RequestData<PengajuanKerjasama> requestData = new RequestData<>();
        requestData.setPayload(pengajuanKerjasama);
        try {
            if (errors.hasErrors()) {
                responseData.failed(CustomError.validRequest(errors), "Bad Request");
                logService.saveLog(requestData, responseData, StatusCode.BAD_REQUEST, this.getClass().getName(),
                        CREATE);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseData);
            }
            User user = userValidation.getUserFromToken(token);

            if(user == null){
                responseData.failed("User not found");
                logService.saveLog(requestData, responseData, StatusCode.NOT_FOUND, this.getClass().getName(),
                        CREATE);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseData);
            }
            DocKerjasama docKerjasama = pengajuanKerjasama.getDocPengajuan();
            docKerjasama.setCreatedBy(user.getId());
            docKerjasama.setCreatedDate(new Date());
            DocKerjasama newDocKerjasama = docKerjasamaRepository.save(docKerjasama);

            pengajuanKerjasama.setCreatedBy(user.getId());
            pengajuanKerjasama.setCreatedDate(new Date());
            pengajuanKerjasama.setDocPengajuan(newDocKerjasama);
//            pengajuanKerjasama.setDocId(newDocKerjasama.getId());

            pengajuanKerjasama = pengajuanKerjasamaRepository.save(pengajuanKerjasama);
            for (KerjasamaServiceApi data: pengajuanKerjasama.getServices()) {
                data.setPekerId(pengajuanKerjasama.getId());
                kerjasamaServiceApiRepository.save(data);
            }
            responseData.success();

        }catch (Exception e){
            e.printStackTrace();
            responseData.failed(e.getMessage());
            logService.saveLog(requestData, responseData, StatusCode.INTERNAL_SERVER_ERROR, this.getClass().getName(),
                    CREATE);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseData);
        }

        logService.saveLog(requestData, responseData, StatusCode.CREATED, this.getClass().getName(),
                CREATE);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseData);

    }

    public void updatePengajuanKerjasama(Long id, PengajuanKerjasama pengajuanKerjasama) {
        Optional<PengajuanKerjasama> existingPengajuanKerjasama = pengajuanKerjasamaRepository.findById(id);
        if (existingPengajuanKerjasama.isPresent()) {
            pengajuanKerjasama.setId(id);
            pengajuanKerjasamaRepository.save(pengajuanKerjasama);
        }
    }

    public void deletePengajuanKerjasama(Long id) {
        pengajuanKerjasamaRepository.deleteById(id);
    }

    public ResponseEntity<ResponseHandling> updateStatusPengajuan(Long id, String token, String status){
        ResponseHandling responseData = new ResponseHandling<>();
        RequestData<Map<String, Object>> requestData = new RequestData<>();
        Map<String, Object> request = logService.setValueRequest("id", id);
        request.put("status", status);
        requestData.setPayload(request);

        try {
            User user = userValidation.getUserFromToken(token);

            if("".equals(status) || status == null){
                responseData.failed("Status tidak boleh kosong");
                logService.saveLog(requestData, responseData, StatusCode.BAD_REQUEST, this.getClass().getName(),
                        UPDATE_STATUS);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseData);
            }

            if(id == null ){
                responseData.failed("ID Pengajuan tidak boleh kosong");
                logService.saveLog(requestData, responseData, StatusCode.BAD_REQUEST, this.getClass().getName(),
                        UPDATE_STATUS);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseData);
            }

            if(user == null){
                responseData.failed("User tidak ditemukan");
                logService.saveLog(requestData, responseData, StatusCode.NOT_FOUND, this.getClass().getName(),
                        UPDATE_STATUS);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseData);
            }

            PengajuanKerjasama pengajuanKerjasama = pengajuanKerjasamaRepository.findById(id).get();
            if(pengajuanKerjasama==null){
                responseData.failed("Pengajuan tidak ditemukan");
                logService.saveLog(requestData, responseData, StatusCode.NOT_FOUND, this.getClass().getName(),
                        UPDATE_STATUS);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseData);
            }
            LogPengajuanKerjasama logPengajuanKerjasama = new LogPengajuanKerjasama();
            logPengajuanKerjasama.setUser(user);
            logPengajuanKerjasama.setCreatedDate(new Date());
            logPengajuanKerjasama.setMessage(null);
            logPengajuanKerjasama.setPekerId(id);
            logPengajuanKerjasama.setDescription("Memperbaharui status pengajuan menjadi '"+status+"'");
            logPengajuanKerjasamaRepository.save(logPengajuanKerjasama);

            pengajuanKerjasama.setStatus(StatusKerjasama.valueOf(status));
            pengajuanKerjasamaRepository.save(pengajuanKerjasama);
            responseData.success();

        }catch (Exception e){
            e.printStackTrace();
            responseData.failed(e.getMessage());
            logService.saveLog(requestData, responseData, StatusCode.INTERNAL_SERVER_ERROR, this.getClass().getName(),
                    UPDATE_STATUS);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseData);
        }

        logService.saveLog(requestData, responseData, StatusCode.OK, this.getClass().getName(),
                UPDATE_STATUS);
        return ResponseEntity.status(HttpStatus.OK).body(responseData);
    }

}
