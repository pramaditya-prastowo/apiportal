package mii.bsi.apiportal.service;

import mii.bsi.apiportal.constant.StatusCode;
import mii.bsi.apiportal.domain.LogPengajuanKerjasama;
import mii.bsi.apiportal.domain.User;
import mii.bsi.apiportal.dto.LogPengajuanKerjasamaDTO;
import mii.bsi.apiportal.repository.LogPengajuanKerjasamaRepository;
import mii.bsi.apiportal.utils.RequestData;
import mii.bsi.apiportal.utils.ResponseHandling;
import mii.bsi.apiportal.validation.UserValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class LogPengajuanKerjasamaService {

    @Autowired
    private LogPengajuanKerjasamaRepository logRepository;
    @Autowired
    private LogService logService;
    @Autowired
    private UserValidation userValidation;

    public static final String GET_ALL = "Get All";

    public ResponseEntity<ResponseHandling<List<LogPengajuanKerjasamaDTO>>> getAllLogById(String token, Long id){
        ResponseHandling<List<LogPengajuanKerjasamaDTO>> responseData = new ResponseHandling<>();
        RequestData<Map<String, Object>> requestData = new RequestData<>();
        requestData.setPayload(logService.setValueRequest("id", id));

        try {
            User user = userValidation.getUserFromToken(token);

            if(user == null){
                responseData.failed("User tidak ditemukan");
                logService.saveLog(requestData, responseData, StatusCode.NOT_FOUND, this.getClass().getName(),
                        GET_ALL);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseData);
            }

            List<LogPengajuanKerjasama> listLog = logRepository.findByPekerId(id);
            List<LogPengajuanKerjasamaDTO> listResponse = new ArrayList<>();
            for (LogPengajuanKerjasama log: listLog) {
                listResponse.add(new LogPengajuanKerjasamaDTO(log.getId(), log.getCreatedDate(),
                        log.getUser().getId(), log.getUser().getFirstName()+ " "+log.getUser().getLastName(),
                        log.getPekerId(), log.getMessage(), log.getDescription()));
            }
            responseData.success();
            responseData.setPayload(listResponse);

        }catch (Exception e){
            e.printStackTrace();
            responseData.failed(e.getMessage());
            logService.saveLog(requestData, responseData, StatusCode.INTERNAL_SERVER_ERROR, this.getClass().getName(),
                    GET_ALL);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseData);
        }
        logService.saveLog(requestData, responseData, StatusCode.OK, this.getClass().getName(),
                GET_ALL);
        return ResponseEntity.ok(responseData);
    }
}
