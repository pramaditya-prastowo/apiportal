package mii.bsi.apiportal.service;

import lombok.extern.slf4j.Slf4j;
import mii.bsi.apiportal.constant.StatusCode;
import mii.bsi.apiportal.domain.*;
import mii.bsi.apiportal.domain.model.ApprovalGroupType;
import mii.bsi.apiportal.dto.AddMatrixRequestDTO;
import mii.bsi.apiportal.dto.DetailApprovalMatrixRequest;
import mii.bsi.apiportal.repository.*;
import mii.bsi.apiportal.utils.RequestData;
import mii.bsi.apiportal.utils.ResponseHandling;
import mii.bsi.apiportal.validation.UserValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ApprovalMatrixService {

    @Autowired
    private ApprovalMatrixRepository matrixRepository;
    @Autowired
    private ApprovalMatrixDetailRepository detailRepository;
    @Autowired
    private ApprovalGroupRepository approvalGroupRepository;

    @Autowired
    private UserValidation adminValidation;
    @Autowired
    private LogService logService;
    @Autowired
    private MenuRepository menuRepository;
    @Autowired
    private GroupsRepository groupsRepository;

    public static final String CREATE = "Create";
    public static final String UPDATE = "Update";
    public static final String GET_ALL = "Get All";
    public static final String GET_DETAIL = "Get Detail";
    public static final String DISABLE_MATRIX = "Disable Matrix";



    public ResponseEntity<ResponseHandling<List<ApprovalMatrix>>> getAll(String token){
        ResponseHandling<List<ApprovalMatrix> > responseData = new ResponseHandling();
        RequestData requestData = new RequestData<>();

        try {
            if(!adminValidation.isAdmin(token)){
                responseData.failed("Access denied");
                logService.saveLog(requestData, responseData, StatusCode.FORBIDDEN, this.getClass().getName(),
                        GET_ALL);
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(responseData);
            }
            List<ApprovalMatrix> list = matrixRepository.findAll();
            for (int i = 0; i < list.size(); i ++) {
                List<ApprovalMatrixDetail> listDetail = detailRepository.findByMatrixId(list.get(i).getId());
                for (ApprovalMatrixDetail detail: listDetail) {
                    System.out.println(detail.getId());
                    List<ApprovalGroup> listGroup = approvalGroupRepository.findByMatrixDetailId(detail.getId());
                    detail.setSelectedGroup(listGroup);
                }
                list.get(i).setDetails(listDetail);
            }

            responseData.success();
            responseData.setPayload(list);
        }catch (Exception e){
            e.printStackTrace();
            responseData.failed("Internal Server Error");
            logService.saveLog(requestData, responseData, StatusCode.INTERNAL_SERVER_ERROR, this.getClass().getName(),
                    GET_ALL);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseData);
        }
//        logService.saveLog(requestData, responseData, StatusCode.OK, this.getClass().getName(),
//                GET_ALL);
        return ResponseEntity.ok(responseData);
    }

    public ResponseEntity<ResponseHandling<ApprovalMatrix>> getDetail(String token, Long id){
        ResponseHandling<ApprovalMatrix> responseData = new ResponseHandling();
        RequestData requestData = new RequestData<>();

        try {
            if(!adminValidation.isAdmin(token)){
                responseData.failed("Access denied");
                logService.saveLog(requestData, responseData, StatusCode.FORBIDDEN, this.getClass().getName(),
                        GET_DETAIL);
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(responseData);
            }
            ApprovalMatrix data = matrixRepository.findById(id).get();
                List<ApprovalMatrixDetail> listDetail = detailRepository.findByMatrixId(data.getId());
                for (ApprovalMatrixDetail detail: listDetail) {
                    System.out.println(detail.getId());
                    List<ApprovalGroup> listGroup = approvalGroupRepository.findByMatrixDetailId(detail.getId());
                    detail.setSelectedGroup(listGroup);
                }
                data.setDetails(listDetail);

            responseData.success();
            responseData.setPayload(data);
        }catch (Exception e){
            e.printStackTrace();
            responseData.failed("Internal Server Error");
            logService.saveLog(requestData, responseData, StatusCode.INTERNAL_SERVER_ERROR, this.getClass().getName(),
                    GET_DETAIL);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseData);
        }
//        logService.saveLog(requestData, responseData, StatusCode.OK, this.getClass().getName(),
//                GET_DETAIL);
        return ResponseEntity.ok(responseData);
    }

    public ResponseEntity<ResponseHandling> createMatrix(String token, AddMatrixRequestDTO request){
        ResponseHandling responseData = new ResponseHandling();
        RequestData<AddMatrixRequestDTO> requestData = new RequestData<>();
        requestData.setPayload(request);

        try {
            User user = adminValidation.getUserFromToken(token);
            if(!adminValidation.isAdmin(token)){
                responseData.failed("Access denied");
                logService.saveLog(requestData, responseData, StatusCode.FORBIDDEN, this.getClass().getName(),
                        CREATE);
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(responseData);
            }
            Menu menu = menuRepository.getReferenceById(Long.parseLong(request.getMenuId()));
            if(menu == null){
                responseData.failed("Menu not found");
                logService.saveLog(requestData, responseData, StatusCode.NOT_FOUND, this.getClass().getName(),
                        CREATE);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseData);
            }
            ApprovalMatrix approvalMatrix = new ApprovalMatrix(menu, true,user.getId());
            approvalMatrix = matrixRepository.save(approvalMatrix);

            for (DetailApprovalMatrixRequest detail: request.getDetails()) {
                ApprovalMatrixDetail data = new ApprovalMatrixDetail();
                data.setMatrixId(approvalMatrix.getId());
                data.setSequence(detail.getNo());
                data.setApprovalGroupType(detail.getType());
                data.setNumberOfApprovalUser(detail.getCount());
                data = detailRepository.save(data);
                data.setSelectedGroup(new ArrayList<>());
//                data.setSelectedGroup();
                for (Groups group : detail.getGroup()) {
                    Groups existGroup = groupsRepository.getReferenceById(group.getId());
                    ApprovalGroup approvalGroup = new ApprovalGroup();
                    approvalGroup.setGroup(existGroup);
                    approvalGroup.setMatrixDetailId(data.getId());
                    data.getSelectedGroup().add(approvalGroupRepository.save(approvalGroup));
                }
                detailRepository.save(data);
            }
            responseData.success();

        }catch (Exception e){
            e.printStackTrace();
            responseData.failed("Internal Server Error");
            logService.saveLog(requestData, responseData, StatusCode.INTERNAL_SERVER_ERROR, this.getClass().getName(),
                    CREATE);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseData);
        }
        logService.saveLog(requestData, responseData, StatusCode.CREATED, this.getClass().getName(),
                CREATE);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseData);

    }

    public ResponseEntity<ResponseHandling> updateMatrix(String token,Long id, AddMatrixRequestDTO request){
        ResponseHandling responseData = new ResponseHandling();
        RequestData<AddMatrixRequestDTO> requestData = new RequestData<>();
        requestData.setPayload(request);

        try {
            User user = adminValidation.getUserFromToken(token);
            if(!adminValidation.isAdmin(token)){
                responseData.failed("Access denied");
                logService.saveLog(requestData, responseData, StatusCode.FORBIDDEN, this.getClass().getName(),
                        UPDATE);
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(responseData);
            }

            ApprovalMatrix existMatrix = matrixRepository.getReferenceById(request.getId());
            if(existMatrix == null){
                responseData.failed("Matrix not found");
                logService.saveLog(requestData, responseData, StatusCode.NOT_FOUND, this.getClass().getName(),
                        UPDATE);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseData);
            }

            Menu menu = menuRepository.getReferenceById(Long.parseLong(request.getMenuId()));
            if(menu == null){
                responseData.failed("Menu not found");
                logService.saveLog(requestData, responseData, StatusCode.NOT_FOUND, this.getClass().getName(),
                        UPDATE);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseData);
            }
            ApprovalMatrix approvalMatrix = existMatrix;
            approvalMatrix.setMenu(menu);
            approvalMatrix.setUpdatedBy(user.getId());
            approvalMatrix.setUpdatedDate(new Date());
            approvalMatrix = matrixRepository.save(approvalMatrix);

            detailRepository.deleteByMatrixId(approvalMatrix.getId());

            for (DetailApprovalMatrixRequest detail: request.getDetails()) {
                ApprovalMatrixDetail data = new ApprovalMatrixDetail();
                data.setId(detail.getId());
                data.setMatrixId(approvalMatrix.getId());
                data.setSequence(detail.getNo());
                data.setApprovalGroupType(detail.getType());
                data.setNumberOfApprovalUser(detail.getCount());
                data = detailRepository.save(data);
                data.setSelectedGroup(new ArrayList<>());
//                groupsRepository.deleteByMatrixDetailId(detail.getId());
                System.out.println(detail.getId());
                approvalGroupRepository.deleteByMatrixDetailId(detail.getId());
//                data.setSelectedGroup();
                for (Groups group : detail.getGroup()) {
                    Groups existGroup = groupsRepository.getReferenceById(group.getId());
                    ApprovalGroup approvalGroup = new ApprovalGroup();
                    approvalGroup.setGroup(existGroup);
                    approvalGroup.setMatrixDetailId(data.getId());
                    data.getSelectedGroup().add(approvalGroupRepository.save(approvalGroup));
                }
                detailRepository.save(data);
            }
            responseData.success();

        }catch (Exception e){
            e.printStackTrace();
            responseData.failed("Internal Server Error");
            logService.saveLog(requestData, responseData, StatusCode.INTERNAL_SERVER_ERROR, this.getClass().getName(),
                    UPDATE);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseData);
        }
        logService.saveLog(requestData, responseData, StatusCode.CREATED, this.getClass().getName(),
                UPDATE);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseData);

    }



    public ResponseEntity<ResponseHandling> disableMatrix(String token, Long id){
        ResponseHandling responseData = new ResponseHandling();
        RequestData<Map<String, Object>> requestData = new RequestData<>();
        requestData.setPayload(logService.setValueRequest("id", id));

        try {
            User user = adminValidation.getUserFromToken(token);
            if(!adminValidation.isAdmin(token)){
                responseData.failed("Access denied");
                logService.saveLog(requestData, responseData, StatusCode.FORBIDDEN, this.getClass().getName(),
                        DISABLE_MATRIX);
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(responseData);
            }

            ApprovalMatrix matrix = matrixRepository.getReferenceById(id);
            if(matrix == null){
                responseData.failed("Not Found");
                logService.saveLog(requestData, responseData, StatusCode.NOT_FOUND, this.getClass().getName(),
                        DISABLE_MATRIX);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseData);
            }
            boolean newStatus = !matrix.getNeedApproval();
            matrix.setNeedApproval(newStatus);
            matrix.setUpdatedDate(new Date());
            matrix.setUpdatedBy(user.getId());
            responseData.success();

        }catch (Exception e){
            e.printStackTrace();
            responseData.failed("Internal Server Error");
            logService.saveLog(requestData, responseData, StatusCode.INTERNAL_SERVER_ERROR, this.getClass().getName(),
                    DISABLE_MATRIX);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseData);
        }
        logService.saveLog(requestData, responseData, StatusCode.OK, this.getClass().getName(),
                DISABLE_MATRIX);
        return ResponseEntity.ok(responseData);
    }

    List<Groups> getListGroupBySequence(int sequence, DetailApprovalMatrixRequest detailRequest){
        return detailRequest.getGroup();
    }
}
