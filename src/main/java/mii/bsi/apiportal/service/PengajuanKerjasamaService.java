package mii.bsi.apiportal.service;

import mii.bsi.apiportal.constant.NofiticationType;
import mii.bsi.apiportal.constant.StatusCode;
import mii.bsi.apiportal.domain.*;
import mii.bsi.apiportal.domain.model.ApprovalGroupType;
import mii.bsi.apiportal.domain.model.Roles;
import mii.bsi.apiportal.domain.model.ApprovalStatus;
import mii.bsi.apiportal.domain.task.TaskApprover;
import mii.bsi.apiportal.domain.task.TaskMaker;
import mii.bsi.apiportal.domain.task.TaskType;
import mii.bsi.apiportal.dto.ApprovalKerjasamaRequest;
import mii.bsi.apiportal.repository.*;
import mii.bsi.apiportal.utils.CustomError;
import mii.bsi.apiportal.utils.RequestData;
import mii.bsi.apiportal.utils.ResponseHandling;
import mii.bsi.apiportal.utils.TemplateNotification;
import mii.bsi.apiportal.validation.UserValidation;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private ApprovalMatrixRepository approvalMatrixRepository;
    @Autowired
    private MenuRepository menuRepository;
    @Autowired
    private ApprovalMatrixDetailRepository approvalMatrixDetailRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ApprovalGroupRepository approvalGroupRepository;
    @Autowired
    private TaskMakerRepository taskMakerRepository;
    @Autowired
    private TaskApproverRepository taskApproverRepository;
    @Autowired
    private TaskService taskService;
    @Autowired
    private PortalNotificationRepository portalNotificationRepository;
    @Autowired
    private TemplateNotification templateNotification;
    @Autowired
    private SystemNotificationRepository systemNotificationRepository;

    public static final String CREATE = "Create";
    public static final String APPROVE = "Approve";
    public static final String GET_ALL = "Get All";
    public static final String GET_BY_ID = "Get By ID";
    public static final String UPDATE_STATUS = "Update Status";
    public static final String CHECK_AVAILABLE_CREATE = "Check Available Create";

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

            Menu menu = menuRepository.findByPermissionName("MAINTAIN_KERJASAMA");
            ApprovalMatrix approvalMatrix = approvalMatrixRepository.findByMenuId(menu.getId());



            TaskMaker taskmaker =  taskService.createTaskMaker(TaskType.PENGAJUAN_KERJASAMA,UUID.randomUUID().toString(),"Pengajuan Kerjasama oleh Mitra",
                    pengajuanKerjasama.getId().toString(),  PengajuanKerjasama.ENTITY_NAME, this.getClass().getName(), 1,menu,
                    approvalMatrix,user,null, null, null);

            List<ApprovalMatrixDetail> approvalMatrixDetails = approvalMatrixDetailRepository.findByMatrixId(approvalMatrix.getId());
            for (ApprovalMatrixDetail detail: approvalMatrixDetails) {
                List<ApprovalGroup> groups = new ArrayList<>();
                if(detail.getApprovalGroupType().equals(ApprovalGroupType.ANY_GROUP)){
                    groups = approvalGroupRepository.findAll();
                }else{
                    groups = approvalGroupRepository.findByMatrixDetailId(detail.getId());
                }
                detail.setSelectedGroup(groups);
                for (ApprovalGroup group: groups) {
                    List<User> userList = userRepository.findByGroupId(group.getGroup().getId());
                    for (User userGroup: userList) {
                        taskService.createTaskApprover(taskmaker,userGroup, detail.getSequence(), "APPROVED", detail.getSequence(), menu);

                        //Admin Notification
                        PortalNotification portalNotificationMitra = templateNotification.notificationKerjasama(userGroup,
                                "Permintaan Pengajuan Kerjasama oleh Mitra dari "+ pengajuanKerjasama.getCompanyName()+"" +
                                        " Mohon untuk segera diproses", NofiticationType.INFO, taskmaker.getMenu(),"");
                        portalNotificationRepository.save(portalNotificationMitra);
                        SystemNotification systemNotification = templateNotification.systemNotificationKerjasama(userGroup,
                                "Permintaan Approve Kerjasama", "Permintaan Pengajuan Kerjasama oleh Mitra dari "+ pengajuanKerjasama.getCompanyName()+"" +
                                        " Mohon untuk segera diproses", "Pengajuan Kerjasama");
                        systemNotificationRepository.save(systemNotification);

                    }
                }
            }
            String bodyMitra = "Pengajuan kerjasama anda berhasil terkirim, Mohon untuk menunggu proses pengajuan setidaknya 2x24 jam";
            //Mitra Notification
            PortalNotification portalNotificationMitra = templateNotification.notificationKerjasama(user,
                    bodyMitra, NofiticationType.INFO, taskmaker.getMenu(),"/"+pengajuanKerjasama.getId());
            portalNotificationRepository.save(portalNotificationMitra);
            SystemNotification systemNotificationMitra = templateNotification.systemNotificationKerjasama(user,
                    "Pengajuan Kerjasama", bodyMitra,"Pengajuan Kerjasama" );
            systemNotificationRepository.save(systemNotificationMitra);
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

    public ResponseEntity<ResponseHandling> approveKerjasama(String token, ApprovalKerjasamaRequest request){
        ResponseHandling responseData = new ResponseHandling<>();
        RequestData<ApprovalKerjasamaRequest> requestData = new RequestData<>();

        try {
            User user = userValidation.getUserFromToken(token);
            if(user == null){
                responseData.failed("User Not Found");
                logService.saveLog(requestData, responseData, StatusCode.NOT_FOUND, this.getClass().getName(),
                        APPROVE);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseData);
            }

            if(!userValidation.isAdmin(token)){
                responseData.failed("Access denied");
                logService.saveLog(requestData, responseData, StatusCode.FORBIDDEN, this.getClass().getName(),
                        APPROVE);
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(responseData);
            }

            PengajuanKerjasama pengajuanKerjasama = pengajuanKerjasamaRepository.findById(request.getPekerId()).orElse(null);
            if(pengajuanKerjasama == null){
                responseData.failed("Pengajuan tidak ditemukan");
                logService.saveLog(requestData, responseData, StatusCode.NOT_FOUND, this.getClass().getName(),
                        APPROVE);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseData);
            }
            TaskMaker taskMaker = taskMakerRepository.findById(request.getActivityId()).orElse(null);
            if(taskMaker == null){
                responseData.failed("Tugas tidak ditemukan");
                logService.saveLog(requestData, responseData, StatusCode.NOT_FOUND, this.getClass().getName(),
                        APPROVE);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseData);
            }

            ApprovalMatrix approvalMatrix = approvalMatrixRepository.findByMenuId(taskMaker.getMenu().getId());
            if(approvalMatrix == null){
                responseData.failed("Matrix tidak ditemukan");
                logService.saveLog(requestData, responseData, StatusCode.NOT_FOUND, this.getClass().getName(),
                        APPROVE);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseData);
            }
            List<TaskApprover> taskApprovers = taskApproverRepository.findByActivityId(taskMaker.getActivityId());
            int countApprove = 0;
            for (TaskApprover approver: taskApprovers) {
                if(approver.getApprovalStatus().equals(ApprovalStatus.DISETUJUI)){
                    countApprove++;
                }
                if(approver.getApprover().getId().equals(user.getId())){
                    if(approver.getApprovalStatus().equals(ApprovalStatus.MENUNGGU_PERSETUJUAN)){
                        approver.setApprovalStatus(request.getApprovalStatus());
                        if(request.getApprovalStatus().equals(ApprovalStatus.DISETUJUI)){
                            approver.setApproveDate(new Date());
                            countApprove++;
                        }
                        taskApproverRepository.save(approver);
                    }
                }
            }
            if(countApprove == taskApprovers.size()){
                taskMaker.setApprovalStatus(ApprovalStatus.DISETUJUI);
                taskMakerRepository.save(taskMaker);
            }

            responseData.success();

        }catch (Exception e){
            e.printStackTrace();
            responseData.failed(e.getMessage());
            logService.saveLog(requestData, responseData, StatusCode.INTERNAL_SERVER_ERROR, this.getClass().getName(),
                    APPROVE);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseData);
        }
        logService.saveLog(requestData, responseData, StatusCode.OK, this.getClass().getName(),
                APPROVE);
        return ResponseEntity.ok(responseData);
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

            pengajuanKerjasama.setStatus(ApprovalStatus.valueOf(status));
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

    public ResponseEntity<ResponseHandling<Integer>> checkAvailableCreate(String token){
        ResponseHandling<Integer> responseData = new ResponseHandling<>();
        RequestData requestData = new RequestData<>();

        try {
            User user = userValidation.getUserFromToken(token);

            if(user == null){
                responseData.failed("User tidak ditemukan");
                logService.saveLog(requestData, responseData, StatusCode.NOT_FOUND, this.getClass().getName(),
                        CHECK_AVAILABLE_CREATE);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseData);
            }

            List<PengajuanKerjasama> kerjasamaList = pengajuanKerjasamaRepository.findByCreatedBy(user.getId());
            int count = 0;
            if(kerjasamaList.size() > 0){
                for (PengajuanKerjasama kerjasama: kerjasamaList) {
                    if(!kerjasama.getStatus().equals(ApprovalStatus.SELESAI)){
                        count ++ ;
                    }
                }
            }
            responseData.success();
            responseData.setPayload(count);

        }catch (Exception e){
            e.printStackTrace();
            responseData.failed(e.getMessage());
            logService.saveLog(requestData, responseData, StatusCode.INTERNAL_SERVER_ERROR, this.getClass().getName(),
                    CHECK_AVAILABLE_CREATE);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseData);
        }
        logService.saveLog(requestData, responseData, StatusCode.OK, this.getClass().getName(),
                CHECK_AVAILABLE_CREATE);
        return ResponseEntity.status(HttpStatus.OK).body(responseData);
    }
}
