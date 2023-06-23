package mii.bsi.apiportal.service;

import mii.bsi.apiportal.constant.DocumentName;
import mii.bsi.apiportal.constant.NofiticationType;
import mii.bsi.apiportal.constant.StatusCode;
import mii.bsi.apiportal.domain.*;
import mii.bsi.apiportal.domain.model.ApprovalGroupType;
import mii.bsi.apiportal.domain.model.Roles;
import mii.bsi.apiportal.domain.model.ApprovalStatus;
import mii.bsi.apiportal.domain.task.TaskApprovalHistory;
import mii.bsi.apiportal.domain.task.TaskApprover;
import mii.bsi.apiportal.domain.task.TaskMaker;
import mii.bsi.apiportal.domain.task.TaskType;
import mii.bsi.apiportal.dto.ApprovalKerjasamaRequest;
import mii.bsi.apiportal.dto.kerjasama.ReUploadDocumentRequestDTO;
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
    @Autowired
    private TaskApprovalHistoryRepository taskApprovalHistoryRepository;

    public static final String CREATE = "Create";
    public static final String APPROVE = "Approve";
    public static final String REJECT = "Reject";
    public static final String HOLD = "Hold";
    public static final String GET_ALL = "Get All";
    public static final String GET_BY_ID = "Get By ID";
    public static final String UPDATE_STATUS = "Update Status";
    public static final String CHECK_AVAILABLE_CREATE = "Check Available Create";
    public static final String RE_UPLOAD = "Re Upload Data";
    public static final String RE_UPLOAD_DOCUMENT = "Re Upload Document";

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
                        PortalNotification portalNotificationAdmin = templateNotification.notificationKerjasama(userGroup,
                                "Permintaan Pengajuan Kerjasama oleh Mitra dari "+ pengajuanKerjasama.getCompanyName()+"" +
                                        " Mohon untuk segera diproses", NofiticationType.INFO, taskmaker,"");
                        portalNotificationRepository.save(portalNotificationAdmin);
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
                    bodyMitra, NofiticationType.INFO, taskmaker,"/"+pengajuanKerjasama.getId());
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

    public ResponseEntity<ResponseHandling> reUploadPengajuanKerjasama(PengajuanKerjasama pengajuanKerjasama, String token, Errors errors){
        ResponseHandling responseData = new ResponseHandling<>();
        RequestData<PengajuanKerjasama> requestData = new RequestData<>();
        requestData.setPayload(pengajuanKerjasama);

        try {
            if (errors.hasErrors()) {
                responseData.failed(CustomError.validRequest(errors), "Bad Request");
                logService.saveLog(requestData, responseData, StatusCode.BAD_REQUEST, this.getClass().getName(),
                        RE_UPLOAD);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseData);
            }
            User user = userValidation.getUserFromToken(token);

            if(user == null){
                responseData.failed("User not found");
                logService.saveLog(requestData, responseData, StatusCode.NOT_FOUND, this.getClass().getName(),
                        RE_UPLOAD);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseData);
            }
            PengajuanKerjasama kerjasama = pengajuanKerjasamaRepository.getReferenceById(pengajuanKerjasama.getId());
            if(kerjasama == null){
                responseData.failed("Pengajuan Kerjasama not found");
                logService.saveLog(requestData, responseData, StatusCode.NOT_FOUND, this.getClass().getName(),
                        RE_UPLOAD);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseData);
            }

            if(!(kerjasama.getCreatedBy().equals(user.getId()))){
                responseData.failed("Aktivitas tidak diizinkan");
                logService.saveLog(requestData, responseData, StatusCode.FORBIDDEN, this.getClass().getName(),
                        RE_UPLOAD_DOCUMENT);
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(responseData);
            }

            String perubahanData = compareChange(kerjasama, pengajuanKerjasama);
            TaskMaker taskMaker = taskMakerRepository.findByEntityNameAndEntityId("PengajuanKerjasama",kerjasama.getId().toString());
            taskMaker.setApprovalStatus(ApprovalStatus.PENGAJUAN_ULANG);
            LogPengajuanKerjasama logKerjasama = new LogPengajuanKerjasama(kerjasama.getId(), user,
                    "Mitra melakukan perubahan data <br>" + perubahanData, "Ada perubahan data.",ApprovalStatus.PENGAJUAN_ULANG);
            logPengajuanKerjasamaRepository.save(logKerjasama);
            pengajuanKerjasamaRepository.save(setUpdateData(kerjasama, pengajuanKerjasama, user,ApprovalStatus.PENGAJUAN_ULANG));
            taskMakerRepository.save(taskMaker);
            responseData.success();

        }catch (Exception e){
            e.printStackTrace();
            responseData.failed(e.getMessage());
            logService.saveLog(requestData, responseData, StatusCode.INTERNAL_SERVER_ERROR, this.getClass().getName(),
                    RE_UPLOAD);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseData);
        }

        logService.saveLog(requestData, responseData, StatusCode.OK, this.getClass().getName(),
                RE_UPLOAD);
        return ResponseEntity.ok(responseData);
    }

    public ResponseEntity<ResponseHandling> reUploadDocumentKerjasama(ReUploadDocumentRequestDTO request, String token, Errors errors){
        ResponseHandling responseData = new ResponseHandling<>();
        RequestData<ReUploadDocumentRequestDTO> requestData = new RequestData<>();
        requestData.setPayload(request);

        try {
            if (errors.hasErrors()) {
                responseData.failed(CustomError.validRequest(errors), "Bad Request");
                logService.saveLog(requestData, responseData, StatusCode.BAD_REQUEST, this.getClass().getName(),
                        RE_UPLOAD_DOCUMENT);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseData);
            }
            User user = userValidation.getUserFromToken(token);

            if(user == null){
                responseData.failed("User not found");
                logService.saveLog(requestData, responseData, StatusCode.NOT_FOUND, this.getClass().getName(),
                        RE_UPLOAD_DOCUMENT);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseData);
            }

            DocKerjasama docKerjasama = docKerjasamaRepository.getReferenceById(request.getIdDoc());
            if(docKerjasama == null){
                responseData.failed("Document not found");
                logService.saveLog(requestData, responseData, StatusCode.NOT_FOUND, this.getClass().getName(),
                        RE_UPLOAD_DOCUMENT);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseData);
            }

            if(!(docKerjasama.getCreatedBy().equals(user.getId()))){
                responseData.failed("Aktivitas tidak diizinkan");
                logService.saveLog(requestData, responseData, StatusCode.FORBIDDEN, this.getClass().getName(),
                        RE_UPLOAD_DOCUMENT);
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(responseData);
            }

            PengajuanKerjasama kerjasama = pengajuanKerjasamaRepository.findByDocId(docKerjasama.getId());

            docKerjasama = setUpdateDoc(request.getKeynameDoc(), request.getNameDoc(), docKerjasama);
            docKerjasamaRepository.save(docKerjasama);
            LogPengajuanKerjasama logKerjasama = new LogPengajuanKerjasama(kerjasama.getId(), user,
                    "Mitra melakukan upload ulang dokumen "+ getByKeyName(request.getKeynameDoc()).toString(), "Ada perubahan document.",ApprovalStatus.PENGAJUAN_ULANG);
            logPengajuanKerjasamaRepository.save(logKerjasama);
            responseData.success();

        }catch (Exception e){
            e.printStackTrace();
            responseData.failed(e.getMessage());
            logService.saveLog(requestData, responseData, StatusCode.INTERNAL_SERVER_ERROR, this.getClass().getName(),
                    RE_UPLOAD_DOCUMENT);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseData);
        }

        logService.saveLog(requestData, responseData, StatusCode.OK, this.getClass().getName(),
                RE_UPLOAD_DOCUMENT);
        return ResponseEntity.ok(responseData);
    }

    public ResponseEntity<ResponseHandling> approveKerjasama(String token, ApprovalKerjasamaRequest request){
        ResponseHandling responseData = new ResponseHandling<>();
        RequestData<ApprovalKerjasamaRequest> requestData = new RequestData<>();
        request.setApprovalStatus(ApprovalStatus.DISETUJUI);
        requestData.setPayload(request);
//        responseData.success();
//        return ResponseEntity.ok(responseData);

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

            List<ApprovalMatrixDetail> matrixDetails = approvalMatrixDetailRepository.findByMatrixId(approvalMatrix.getId());
            int currentLevel = taskMaker.getCurrentApprovalLevel();
            int countSequence = matrixDetails.size();
            List<TaskApprover> taskApprovers = taskApproverRepository.findByTaskMakerAndSequence(
                    taskMaker.getActivityId(), currentLevel);
            int countApprove = 0;

            System.out.println("Check Point, count approver = "+taskApprovers.size());

            for (TaskApprover approver: taskApprovers) {
                if(approver.getApprovalStatus().equals(ApprovalStatus.DISETUJUI)){
                    countApprove++;
                    System.out.println(countApprove);
                }
                System.out.println(approver.getApprover().getId() + " = "+ user.getId());
                if(approver.getApprover().getId().equals(user.getId())){
                    System.out.println("ID SAMA");
                    System.out.println(approver.getApprovalStatus() + " = "+ ApprovalStatus.MENUNGGU_PERSETUJUAN);
                    if(approver.getApprovalStatus().equals(ApprovalStatus.MENUNGGU_PERSETUJUAN)){
                        System.out.println("Status SAMA");
                        approver.setApprovalStatus(request.getApprovalStatus());
                        System.out.println(request.getApprovalStatus() + " = "+ ApprovalStatus.DISETUJUI);
                        if(request.getApprovalStatus().equals(ApprovalStatus.DISETUJUI)){

                            System.out.println("Status SAMA DISETUJUI");
                            approver.setApproveDate(new Date());
                            approver.setApprovalStatus(ApprovalStatus.DISETUJUI);
                            countApprove++;
                        }
                        String bodyApprover = "Anda telah menyetujui pengajuan kerjasama dari perusahaan "
                                +pengajuanKerjasama.getCompanyName();
                        PortalNotification portalNotificationApprover = templateNotification.actionKerjasama(user,
                                bodyApprover, NofiticationType.INFO,ApprovalStatus.DISETUJUI, taskMaker,"/"+pengajuanKerjasama.getId());
                        LogPengajuanKerjasama logPengajuanKerjasama = new LogPengajuanKerjasama(pengajuanKerjasama.getId(),
                                user,null,"Pengajuan kerjasama disetujui!",ApprovalStatus.DISETUJUI);
                        TaskApprovalHistory taskApprovalHistory = new TaskApprovalHistory(taskMaker.getCurrentApprovalLevel(),
                                user, ApprovalStatus.DISETUJUI, taskMaker, user.getId());

                        taskApproverRepository.save(approver);
                        logPengajuanKerjasamaRepository.save(logPengajuanKerjasama);
                        portalNotificationRepository.save(portalNotificationApprover);
                        taskApprovalHistoryRepository.save(taskApprovalHistory);

                        System.out.println("oke log dan notifikasi");
                    }
                }
            }
            if(currentLevel == countSequence){
                System.out.println("masuk final");

                System.out.println(countApprove + " = "+matrixDetails.get(currentLevel-1).getNumberOfApprovalUser());
                if(countApprove == matrixDetails.get(currentLevel-1).getNumberOfApprovalUser()){

                    taskMaker.setApprovalStatus(ApprovalStatus.DISETUJUI);
                    taskMakerRepository.save(taskMaker);

                    String bodyMitra = "Pengajuan kerjasama disetujui!";
                    PortalNotification portalNotificationMitra = templateNotification.actionKerjasama(taskMaker.getMaker(),
                            bodyMitra, NofiticationType.INFO, ApprovalStatus.DISETUJUI,taskMaker,"/"+pengajuanKerjasama.getId());
                    portalNotificationRepository.save(portalNotificationMitra);
                    pengajuanKerjasama.setStatus(ApprovalStatus.DISETUJUI);
                    pengajuanKerjasama.setUpdatedDate(new Date());
                    pengajuanKerjasama.setUpdatedBy(user.getId());
                    pengajuanKerjasamaRepository.save(pengajuanKerjasama);
                }
            }else if(currentLevel < countSequence){
                System.out.println(countApprove + " = "+matrixDetails.get(currentLevel-1).getNumberOfApprovalUser());
                if(countApprove == matrixDetails.get(currentLevel-1).getNumberOfApprovalUser()){
                    System.out.println("next sequence");
                    taskMaker.setCurrentApprovalLevel(currentLevel + 1);
                    taskMakerRepository.save(taskMaker);
                }

            }
            System.out.println(currentLevel+ " < "+ countSequence);


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

    public ResponseEntity<ResponseHandling> rejectKerjasama(String token, ApprovalKerjasamaRequest request){
        ResponseHandling responseData = new ResponseHandling<>();
        RequestData<ApprovalKerjasamaRequest> requestData = new RequestData<>();

        try {
            User user = userValidation.getUserFromToken(token);
            if(user == null){
                responseData.failed("User Not Found");
                logService.saveLog(requestData, responseData, StatusCode.NOT_FOUND, this.getClass().getName(),
                        REJECT);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseData);
            }

            if(!userValidation.isAdmin(token)){
                responseData.failed("Access denied");
                logService.saveLog(requestData, responseData, StatusCode.FORBIDDEN, this.getClass().getName(),
                        REJECT);
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(responseData);
            }

            PengajuanKerjasama pengajuanKerjasama = pengajuanKerjasamaRepository.findById(request.getPekerId()).orElse(null);
            if(pengajuanKerjasama == null){
                responseData.failed("Pengajuan tidak ditemukan");
                logService.saveLog(requestData, responseData, StatusCode.NOT_FOUND, this.getClass().getName(),
                        REJECT);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseData);
            }
            TaskMaker taskMaker = taskMakerRepository.findById(request.getActivityId()).orElse(null);
            if(taskMaker == null){
                responseData.failed("Tugas tidak ditemukan");
                logService.saveLog(requestData, responseData, StatusCode.NOT_FOUND, this.getClass().getName(),
                        REJECT);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseData);
            }

            ApprovalMatrix approvalMatrix = approvalMatrixRepository.findByMenuId(taskMaker.getMenu().getId());
            if(approvalMatrix == null){
                responseData.failed("Matrix tidak ditemukan");
                logService.saveLog(requestData, responseData, StatusCode.NOT_FOUND, this.getClass().getName(),
                        REJECT);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseData);
            }
            pengajuanKerjasama.updateStatus(ApprovalStatus.DITOLAK, user.getId());

            taskMaker.setApprovalStatus(ApprovalStatus.DITOLAK);

            //log
            LogPengajuanKerjasama logPengajuanKerjasama = new LogPengajuanKerjasama(pengajuanKerjasama.getId(),
                    user,request.getMessage(),"Pengajuan kerjasama ditolak!",ApprovalStatus.DITOLAK);

            TaskApprover taskApprover = taskApproverRepository.findByTaskMakerAndApproverAndSequence(taskMaker.getActivityId(),
                    user.getId(),taskMaker.getCurrentApprovalLevel());
            taskApprover.setApprovalStatus(ApprovalStatus.DITOLAK);

            TaskApprovalHistory taskApprovalHistory = new TaskApprovalHistory(taskMaker.getCurrentApprovalLevel(),
                    user, ApprovalStatus.DITOLAK, taskMaker, user.getId());

            pengajuanKerjasamaRepository.save(pengajuanKerjasama);
            taskMakerRepository.save(taskMaker);
            taskApproverRepository.save(taskApprover);
            logPengajuanKerjasamaRepository.save(logPengajuanKerjasama);
            taskApprovalHistoryRepository.save(taskApprovalHistory);
            String bodyApprover = "Anda menolak pengajuan kerjasama dari perusahaan "+pengajuanKerjasama.getCompanyName() +" dengan alasan berikut " +logPengajuanKerjasama.getMessage();
            String bodyMitra = logPengajuanKerjasama.getDescription()+ " Dengan alasan berikut " +logPengajuanKerjasama.getMessage();


            PortalNotification portalNotificationApprover = templateNotification.actionKerjasama(user,
                    bodyApprover, NofiticationType.INFO, ApprovalStatus.DITOLAK,taskMaker,"/"+pengajuanKerjasama.getId());
            PortalNotification portalNotificationMitra = templateNotification.actionKerjasama(taskMaker.getMaker(),
                    bodyMitra, NofiticationType.INFO, ApprovalStatus.DITOLAK,taskMaker,"/"+pengajuanKerjasama.getId());

            portalNotificationRepository.save(portalNotificationApprover);
            portalNotificationRepository.save(portalNotificationMitra);
            responseData.success();

        }catch (Exception e){
            e.printStackTrace();
            responseData.failed(e.getMessage());
            logService.saveLog(requestData, responseData, StatusCode.INTERNAL_SERVER_ERROR, this.getClass().getName(),
                    REJECT);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseData);
        }
        logService.saveLog(requestData, responseData, StatusCode.OK, this.getClass().getName(),
                REJECT);
        return ResponseEntity.ok(responseData);
    }

    public ResponseEntity<ResponseHandling> holdKerjasama(String token, ApprovalKerjasamaRequest request){
        ResponseHandling responseData = new ResponseHandling<>();
        RequestData<ApprovalKerjasamaRequest> requestData = new RequestData<>();

        try {
            User user = userValidation.getUserFromToken(token);
            if(user == null){
                responseData.failed("User Not Found");
                logService.saveLog(requestData, responseData, StatusCode.NOT_FOUND, this.getClass().getName(),
                        HOLD);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseData);
            }

            if(!userValidation.isAdmin(token)){
                responseData.failed("Access denied");
                logService.saveLog(requestData, responseData, StatusCode.FORBIDDEN, this.getClass().getName(),
                        HOLD);
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(responseData);
            }

            PengajuanKerjasama pengajuanKerjasama = pengajuanKerjasamaRepository.findById(request.getPekerId()).orElse(null);
            if(pengajuanKerjasama == null){
                responseData.failed("Pengajuan tidak ditemukan");
                logService.saveLog(requestData, responseData, StatusCode.NOT_FOUND, this.getClass().getName(),
                        HOLD);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseData);
            }
            TaskMaker taskMaker = taskMakerRepository.findById(request.getActivityId()).orElse(null);
            if(taskMaker == null){
                responseData.failed("Tugas tidak ditemukan");
                logService.saveLog(requestData, responseData, StatusCode.NOT_FOUND, this.getClass().getName(),
                        HOLD);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseData);
            }

            ApprovalMatrix approvalMatrix = approvalMatrixRepository.findByMenuId(taskMaker.getMenu().getId());
            if(approvalMatrix == null){
                responseData.failed("Matrix tidak ditemukan");
                logService.saveLog(requestData, responseData, StatusCode.NOT_FOUND, this.getClass().getName(),
                        HOLD);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseData);
            }

            pengajuanKerjasama.updateStatus(ApprovalStatus.DITAHAN, user.getId());

            taskMaker.setApprovalStatus(ApprovalStatus.DITAHAN);

            TaskApprover taskApprover = taskApproverRepository.findByTaskMakerAndApproverAndSequence(taskMaker.getActivityId(),
                    user.getId(),taskMaker.getCurrentApprovalLevel());
            taskApprover.setApprovalStatus(ApprovalStatus.DITAHAN);
            //log
            LogPengajuanKerjasama logPengajuanKerjasama = new LogPengajuanKerjasama(pengajuanKerjasama.getId(),
                    user, request.getMessage(), "Pengajuan kerjasama ditahan!",ApprovalStatus.DITAHAN);


            TaskApprovalHistory taskApprovalHistory = new TaskApprovalHistory(taskMaker.getCurrentApprovalLevel(),
                    user, ApprovalStatus.DITAHAN, taskMaker, user.getId());

            taskApprovalHistoryRepository.save(taskApprovalHistory);
            logPengajuanKerjasamaRepository.save(logPengajuanKerjasama);
            pengajuanKerjasamaRepository.save(pengajuanKerjasama);
            taskMakerRepository.save(taskMaker);
            taskApproverRepository.save(taskApprover);
            String bodyApprover = "Anda menahan pengajuan kerjasama dari perusahaan "+pengajuanKerjasama.getCompanyName() +" dengan alasan berikut " +logPengajuanKerjasama.getMessage();
            String bodyMitra = logPengajuanKerjasama.getDescription()+ " Dengan alasan berikut " +logPengajuanKerjasama.getMessage();


            PortalNotification portalNotificationApprover = templateNotification.actionKerjasama(user,
                    bodyApprover, NofiticationType.INFO, ApprovalStatus.DITAHAN,taskMaker,"/"+pengajuanKerjasama.getId());
            PortalNotification portalNotificationMitra = templateNotification.actionKerjasama(taskMaker.getMaker(),
                    bodyMitra, NofiticationType.INFO,ApprovalStatus.DITAHAN, taskMaker,"/"+pengajuanKerjasama.getId());

            portalNotificationRepository.save(portalNotificationApprover);
            portalNotificationRepository.save(portalNotificationMitra);
            responseData.success();

        }catch (Exception e){
            e.printStackTrace();
            responseData.failed(e.getMessage());
            logService.saveLog(requestData, responseData, StatusCode.INTERNAL_SERVER_ERROR, this.getClass().getName(),
                    HOLD);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseData);
        }
        logService.saveLog(requestData, responseData, StatusCode.OK, this.getClass().getName(),
                HOLD);
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
            logPengajuanKerjasama.setStatus(ApprovalStatus.valueOf(status));
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
                    if(!(kerjasama.getStatus().equals(ApprovalStatus.SELESAI) ||
                            kerjasama.getStatus().equals(ApprovalStatus.DISETUJUI) ||
                            kerjasama.getStatus().equals(ApprovalStatus.DITOLAK)) ){
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

    private DocKerjasama setUpdateDoc(String keyname,String value, DocKerjasama docKerjasama){
        switch (keyname){
            case "surat-pernyataan":
                docKerjasama.setSrtPernyataanPengajuan(value);
                break;
            case "sk-kemenkumham":
                docKerjasama.setSrtKetKemenkumham(value);
                break;
            case "akta-pendirian-perubahan":
                docKerjasama.setAktaPendirianPerubahan(value);
                break;
            case "company-profile":
                docKerjasama.setCompanyProfile(value);
                break;
            case "npwp":
                docKerjasama.setCompanyNpwp(value);
                break;
            case "ktp-pengurus":
                docKerjasama.setFotoKtpPengurus(value);
                break;
            case "ktp-pm":
                docKerjasama.setFotoKtpPM(value);
                break;
            case "siup":
                docKerjasama.setSrtSiup(value);
                break;
            case "nib":
                docKerjasama.setNib(value);
                break;
            case "hasil-sandbox":
                docKerjasama.setHasilSandbox(value);
                break;
            case "buku-tabungan":
                docKerjasama.setBukuTabungan(value);
                break;
            case "public-key":
                docKerjasama.setPublicKey(value);
                break;
            default:
                break;
        }
        return  docKerjasama;
    }

    private PengajuanKerjasama setUpdateData(PengajuanKerjasama kerjasama, PengajuanKerjasama request, User user, ApprovalStatus status){
        kerjasama.setCompanyName(request.getCompanyName());
        kerjasama.setCompanyAddress(request.getCompanyAddress());
        kerjasama.setEmployeeCount(request.getEmployeeCount());
        kerjasama.setPic1Name(request.getPic1Name());
        kerjasama.setPic1Email(request.getPic1Email());
        kerjasama.setPic1Telp(request.getPic1Telp());
        kerjasama.setPic2Name(request.getPic2Name());
        kerjasama.setPic2Email(request.getPic2Email());
        kerjasama.setPic2Telp(request.getPic2Telp());
        kerjasama.getDocPengajuan().setNoRekPerusahaan(request.getDocPengajuan().getNoRekPerusahaan());
        kerjasama.setUpdatedBy(user.getId());
        kerjasama.setUpdatedDate(new Date());
        kerjasama.setStatus(status);
        return kerjasama;
    }

    public DocumentName getByKeyName(String keyname){
        switch (keyname){
            case "surat-pernyataan":
                return DocumentName.SURAT_PERNYATAAN;
            case "sk-kemenkumham":
                return DocumentName.SK_KEMENKUMHAM;
            case "akta-pendirian-perubahan":
                return DocumentName.AKTA_PENDIRIAN_PERUBAHAN;
            case "company-profile":
                return DocumentName.COMPANY_PROFILE;
            case "npwp":
                return DocumentName.NPWP;
            case "ktp-pengurus":
                return DocumentName.KTP_PENGURUS;
            case "ktp-pm":
                return DocumentName.KTP_PM;
            case "siup":
                return DocumentName.SIUP;
            case "nib":
                return DocumentName.NIB;
            case "hasil-sandbox":
                return DocumentName.HASIL_SANDBOX;
            case "buku-tabungan":
                return DocumentName.BUKU_TABUNGAN;
            case "public-key":
                return DocumentName.PUBLIC_KEY;
            default:
                return null;
        }
    }

    private String compareChange(PengajuanKerjasama existing, PengajuanKerjasama newData){
        List<String> list = new ArrayList<>();
        if(!(existing.getCompanyName().equals(newData.getCompanyName()))){
            list.add("Nama Perusahaan: " + existing.getCompanyName()+ " -> "+ newData.getCompanyName());
        }
        if(!(existing.getCompanyAddress().equals(newData.getCompanyAddress()))){
            list.add("Alamat Perusahaan: " + existing.getCompanyAddress()+" -> " + newData.getCompanyAddress());
        }
        if(existing.getEmployeeCount() != newData.getEmployeeCount()){
            list.add("Jumlah Karyawan: " + existing.getEmployeeCount() + " -> " + newData.getEmployeeCount());
        }
        if(!(existing.getPic1Name().equals(newData.getPic1Name()))){
            list.add("Nama PIC1: "+ existing.getPic1Name() + " -> " + newData.getPic1Name());
        }
        if(!(existing.getPic1Email().equals(newData.getPic1Email()))){
            list.add("Email PIC1: " + existing.getPic1Email() + " -> " + newData.getPic1Email());
        }
        if(!(existing.getPic1Telp().equals(newData.getPic1Telp()))){
            list.add("No.Telp PIC1: " + existing.getPic1Telp() + " -> " + newData.getPic1Telp());
        }
        if(!(existing.getPic2Name().equals(newData.getPic2Name()))){
            list.add("Nama PIC2: " + existing.getPic2Name() + " -> " + newData.getPic2Name());
        }
        if(!(existing.getPic2Email().equals(newData.getPic2Email()))){
            list.add("Email PIC2: " + existing.getPic2Email() + " -> " + newData.getPic2Email());
        }
        if(!(existing.getPic2Telp().equals(newData.getPic2Telp()))){
            list.add("No.Telp PIC2: " + existing.getPic2Telp() + " -> " + newData.getPic2Telp());
        }
        if(!(existing.getDocPengajuan().getNoRekPerusahaan().equals(newData.getDocPengajuan().getNoRekPerusahaan()))){
            list.add("No.Rek Perusahaan: " + existing.getDocPengajuan().getNoRekPerusahaan() + " -> " + newData.getDocPengajuan().getNoRekPerusahaan());
        }

        String result = "";
        for (String data : list) {
            result += data +"<br>";
        }
        return result;
    }

}
