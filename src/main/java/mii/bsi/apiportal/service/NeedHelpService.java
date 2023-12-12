package mii.bsi.apiportal.service;

import io.jsonwebtoken.Claims;
import mii.bsi.apiportal.constant.StatusCode;
import mii.bsi.apiportal.constant.sql.BuilderHelpQuery;
import mii.bsi.apiportal.constant.sql.ConstantQuery;
import mii.bsi.apiportal.constant.sql.MyPagination;
import mii.bsi.apiportal.domain.NeedHelp;
import mii.bsi.apiportal.domain.User;
import mii.bsi.apiportal.domain.model.FilterGetData;
import mii.bsi.apiportal.domain.model.Roles;
import mii.bsi.apiportal.repository.NeedHelpRepository;
import mii.bsi.apiportal.utils.*;
import mii.bsi.apiportal.validation.UserValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class NeedHelpService {

    @Autowired
    private LogService logService;

    @Autowired
    private NeedHelpRepository needHelpRepository;

    @Autowired
    private JwtUtility jwtUtility;
    @Autowired
    private UserValidation userValidation;

    @Autowired
    private MyPagination<NeedHelp> myPagination;

    @Autowired
    private BuilderHelpQuery builderHelp;

    public static final String CREATE = "Post Need Help";
    public static final String GET = "Get Need Help";

    public static final String UPDATE = "UPDATE";
    public static final String GET_BY_ID = "Get By ID";

    public ResponseEntity<ResponseHandling> postHelp(NeedHelp needHelp, Errors errors){
        ResponseHandling responseData = new ResponseHandling<>();
        RequestData<NeedHelp> requestData = new RequestData<>();
        requestData.setPayload(needHelp);

        try {
            if (errors.hasErrors()) {
                responseData.failed(CustomError.validRequest(errors), "Bad Request");
                logService.saveLog(requestData, responseData, StatusCode.BAD_REQUEST, this.getClass().getName(),
                        CREATE);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseData);
            }

            needHelp.setCreateDate(new Date());
            needHelp.setStatus(0);
            needHelpRepository.save(needHelp);
            responseData.success();

        }catch (Exception e){
            e.printStackTrace();
            responseData.failed(e.getMessage());
            logService.saveLog(requestData, responseData, StatusCode.INTERNAL_SERVER_ERROR, this.getClass().getName(),
                    CREATE);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseData);
        }
        logService.saveLog(requestData, responseData, StatusCode.OK, this.getClass().getName(),
                CREATE);
        return ResponseEntity.ok(responseData);
    }

    public ResponseEntity<ResponseHandling<List<NeedHelp>>> getHelp(String token){
        ResponseHandling<List<NeedHelp>> responseData = new ResponseHandling<>();
        RequestData requestData = new RequestData();
        try {

            if(!userValidation.isAdmin(token)){
                responseData.failed("Access denied");
                logService.saveLog(requestData, responseData, StatusCode.FORBIDDEN, this.getClass().getName(),
                        GET);
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(responseData);
            }

            List<NeedHelp> resultList = needHelpRepository.findAll();
            resultList.sort(Comparator.comparing(NeedHelp::getStatus));
            responseData.setPayload(resultList);
            responseData.success();

        }catch (Exception e){
            e.printStackTrace();
            responseData.failed(e.getMessage());
            logService.saveLog(requestData, responseData, StatusCode.INTERNAL_SERVER_ERROR, this.getClass().getName(),
                    GET);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseData);
        }
        logService.saveLog(requestData, responseData, StatusCode.OK, this.getClass().getName(),
                GET);
        return ResponseEntity.ok(responseData);
    }

    public ResponseEntity<ResponseHandling<RowDataResponse<NeedHelp>>> getHelpFilter(String token, FilterGetData filter){
        ResponseHandling<RowDataResponse<NeedHelp>> responseData = new ResponseHandling<>();
        RequestData requestData = new RequestData();
        try {
            if(!userValidation.isAdmin(token)){
                responseData.failed("Access denied");
                logService.saveLog(requestData, responseData, StatusCode.FORBIDDEN, this.getClass().getName(),
                        GET);
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(responseData);
            }

//            MapSqlParameterSource paramSource = new MapSqlParameterSource();
//            for(Map.Entry<String,String> itr : filter.getFilter().entrySet()){
//                paramSource.addValue(itr.getKey(), itr.getValue());
//            }
//
//            paramSource.addValue("pageNumber", filter.getPageNumber());
//            paramSource.addValue("pageSize", filter.getPageSize());
////            List<NeedHelp> resultList = needHelpRepository.findAll();
////            responseData.setPayload(resultList);
////            responseData.success();
            String sql = builderHelp.getWithFilter(ConstantQuery.needHelp, ConstantQuery.needHelpOrder, filter);
            RowDataResponse<NeedHelp> rowDataResponse = myPagination.getData(filter,sql, NeedHelp.class);
            responseData.success();
            responseData.setPayload(rowDataResponse);
        }catch (Exception e){
            e.printStackTrace();
            responseData.failed(e.getMessage());
            logService.saveLog(requestData, responseData, StatusCode.INTERNAL_SERVER_ERROR, this.getClass().getName(),
                    GET);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseData);
        }
        logService.saveLog(requestData, responseData, StatusCode.OK, this.getClass().getName(),
                GET);
        return ResponseEntity.ok(responseData);
    }

    public ResponseEntity<ResponseHandling<NeedHelp>> getById(String token,  Long id){
        ResponseHandling<NeedHelp> responseData = new ResponseHandling<>();
        RequestData<Map<String, Object>> requestData = new RequestData<>();
        requestData.setPayload(logService.setValueRequest("id", id));

        try {
            if(!userValidation.isAdmin(token)){
                responseData.failed("Access denied");
                logService.saveLog(requestData, responseData, StatusCode.FORBIDDEN, this.getClass().getName(),
                        GET_BY_ID);
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(responseData);
            }

            NeedHelp help = needHelpRepository.findById(id).orElse(null);
            if(help == null){
                responseData.failed("Data tidak ditemukan");
                logService.saveLog(requestData, responseData, StatusCode.BAD_REQUEST, this.getClass().getName(),
                        GET_BY_ID);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseData);
            }
            responseData.success();
            responseData.setPayload(help);

        }catch (Exception e){
            e.printStackTrace();
            responseData.failed(e.getMessage());
            logService.saveLog(requestData, responseData, StatusCode.INTERNAL_SERVER_ERROR, this.getClass().getName(),
                    GET_BY_ID);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseData);
        }
        logService.saveLog(requestData, responseData, StatusCode.OK, this.getClass().getName(),
                GET_BY_ID);
        return ResponseEntity.ok(responseData);
    }

    public ResponseEntity<ResponseHandling> updateStatusHelp(String token, NeedHelp help){
        ResponseHandling responseData = new ResponseHandling<>();
        RequestData<NeedHelp> requestData = new RequestData<>();
        requestData.setPayload(help);

        try {
            if(!userValidation.isAdmin(token)){
                responseData.failed("Access denied");
                logService.saveLog(requestData, responseData, StatusCode.FORBIDDEN, this.getClass().getName(),
                        UPDATE);
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(responseData);
            }

            NeedHelp helpDb = needHelpRepository.findById(help.getId()).orElse(null);
            if(helpDb == null){
                responseData.failed("Data tidak ditemukan");
                logService.saveLog(requestData, responseData, StatusCode.BAD_REQUEST, this.getClass().getName(),
                        UPDATE);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseData);
            }
            helpDb.setStatus(help.getStatus());
            helpDb.setUpdateDate(new Date());
            needHelpRepository.save(helpDb);
            responseData.success();

        }catch (Exception e){
            e.printStackTrace();
            responseData.failed(e.getMessage());
            logService.saveLog(requestData, responseData, StatusCode.INTERNAL_SERVER_ERROR, this.getClass().getName(),
                    UPDATE);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseData);
        }
        logService.saveLog(requestData, responseData, StatusCode.OK, this.getClass().getName(),
                UPDATE);
        return ResponseEntity.ok(responseData);
    }
}
