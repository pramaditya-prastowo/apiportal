package mii.bsi.apiportal.service;

import mii.bsi.apiportal.constant.StatusCode;
import mii.bsi.apiportal.domain.*;
import mii.bsi.apiportal.domain.model.ApprovalStatus;
import mii.bsi.apiportal.domain.task.TaskApprover;
import mii.bsi.apiportal.domain.task.TaskMaker;
import mii.bsi.apiportal.domain.task.TaskType;
import mii.bsi.apiportal.dto.task.*;
import mii.bsi.apiportal.repository.TaskApproverRepository;
import mii.bsi.apiportal.repository.TaskMakerRepository;
import mii.bsi.apiportal.utils.RequestData;
import mii.bsi.apiportal.utils.ResponseHandling;
import mii.bsi.apiportal.validation.UserValidation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class TaskService {


    @Autowired
    private TaskMakerRepository taskMakerRepository;
    @Autowired
    private TaskApproverRepository taskApproverRepository;
    @Autowired
    private UserValidation userValidation;
    @Autowired
    private LogService logService;
    @Autowired
    private ApprovalMatrixService matrixService;

    public static final String GET_ALL_TASK = "Get All Task";
    public static final String GET_ALL_TASK_APPROVER = "Get All Task Approver";


    public ResponseEntity<ResponseHandling<MyTaskResponseDTO>> getAllMyTask(String token, String status){
        ResponseHandling<MyTaskResponseDTO> responseData = new ResponseHandling<>();
        RequestData requestData = new RequestData<>();
        System.out.println("status = "+status);

        try {
            User user = userValidation.getUserFromToken(token);
            if(user == null){
                responseData.failed("User Not Found");
                logService.saveLog(requestData, responseData, StatusCode.NOT_FOUND, this.getClass().getName(),
                        GET_ALL_TASK);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseData);
            }

            if(!userValidation.isAdmin(token)){
                responseData.failed("Access denied");
                logService.saveLog(requestData, responseData, StatusCode.FORBIDDEN, this.getClass().getName(),
                        GET_ALL_TASK);
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(responseData);
            }

            List<TaskMaker> taskMakers = getTaskMakerByUserId(user);
            List<TaskApprover> taskApprovers = getTaskApproverByUserId(user);

            List<TaskMakerResponse> taskMakerResponses = new ArrayList<>();
            List<TaskApproverResponse> taskApproverResponses = new ArrayList<>();
            for (TaskMaker task1: taskMakers) {
                taskMakerResponses.add(new TaskMakerResponse(task1));
            }
            for (TaskApprover task2: taskApprovers) {
                taskApproverResponses.add(new TaskApproverResponse(task2));
            }

            responseData.success();
            responseData.setPayload(new MyTaskResponseDTO(taskMakerResponses,taskApproverResponses));

        }catch (Exception e){
            e.printStackTrace();
            responseData.failed(e.getMessage());
            logService.saveLog(new RequestData<>(), responseData, StatusCode.INTERNAL_SERVER_ERROR, this.getClass().getName(),
                    GET_ALL_TASK);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseData);
        }
        return ResponseEntity.ok(responseData);
    }

    public ResponseEntity<ResponseHandling<DetailTaskResponseDTO>> getAllByEntityIdAndName(String token, String entityId, String entityName){
        ResponseHandling<DetailTaskResponseDTO> responseData = new ResponseHandling<>();
        RequestData<Map<String, Object>> requestData = new RequestData<>();
        Map<String, Object> request = logService.setValueRequest("entityId", entityId);
        request.put("entityName", entityName);
        requestData.setPayload(request);

        try {
            User user = userValidation.getUserFromToken(token);
            if(user == null){
                responseData.failed("User Not Found");
                logService.saveLog(requestData, responseData, StatusCode.NOT_FOUND, this.getClass().getName(),
                        GET_ALL_TASK_APPROVER);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseData);
            }

            if(!userValidation.isAdmin(token)){
                responseData.failed("Access denied");
                logService.saveLog(requestData, responseData, StatusCode.FORBIDDEN, this.getClass().getName(),
                        GET_ALL_TASK_APPROVER);
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(responseData);
            }


            List<TaskApprover> taskApprovers = getTaskApproverEntityIdAndName(entityId, entityName);
            List<TaskApproverSequenceResponse> taskApproverSequenceResponses = new ArrayList<>();
            TaskMaker taskMaker = new TaskMaker();


            if(taskApprovers.size() > 0){
                taskMaker =  taskMakerRepository.findById(taskApprovers.get(0).getActivityId()).get();
                int sequence = matrixService.getSequenceApprovalMatrix(taskMaker.getApprovalMatrix().getId());
                for (int i = 1; i <= sequence; i++) {
                    List<TaskApproverResponse> taskApproverResponses = new ArrayList<>();
                    for (TaskApprover task2: taskApprovers) {
                        if(task2.getSequence() == i){
                            taskApproverResponses.add(new TaskApproverResponse(task2));
                        }
                    }
                    taskApproverSequenceResponses.add(new TaskApproverSequenceResponse(i, taskApproverResponses));
                }
            }

            ApprovalMatrix data = matrixService.getApprovalMatrixDetailWithOutGroupById(taskMaker.getApprovalMatrix().getId());
            data.setMenu(null);
            TaskMakerResponse taskMakerResponse = new TaskMakerResponse(taskMaker);
            taskMakerResponse.setApprovalMatrix(data);
            DetailTaskResponseDTO detailResponse = new DetailTaskResponseDTO(taskMakerResponse, taskApproverSequenceResponses);

            responseData.success();
            responseData.setPayload(detailResponse);
        }catch (Exception e){
            e.printStackTrace();
            responseData.failed(e.getMessage());
            logService.saveLog(requestData, responseData, StatusCode.INTERNAL_SERVER_ERROR, this.getClass().getName(),
                    GET_ALL_TASK_APPROVER);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseData);
        }
//        logService.saveLog(requestData, responseData, StatusCode.OK, this.getClass().getName(),
//                GET_ALL_TASK_APPROVER);
        return ResponseEntity.ok(responseData);

    }

    public List<TaskMaker> getTaskMakerByUserId(User user){
        List<TaskMaker> taskMakers = taskMakerRepository.findByMaker(user);
        if(taskMakers.size() > 0 ){
            return taskMakers;
        }
        return new ArrayList<>();
    }

    public List<TaskApprover> getTaskApproverByUserId(User user){
        List<TaskApprover> taskApprovers =   taskApproverRepository.findByApprover(user);
        if(taskApprovers.size() > 0){
            return taskApprovers;
        }
        return new ArrayList<>();
    }

    public List<TaskApprover> getTaskApproverEntityIdAndName(String entityId, String entityName){
        List<TaskApprover> taskApprovers =   taskApproverRepository.findByEntityIdAndEntityName(entityId, entityName);
        if(taskApprovers.size() > 0){
            return taskApprovers;
        }
        return new ArrayList<>();
    }

    public TaskMaker createTaskMaker(TaskType taskType, String approvalToken, String description,
                                String entityId, String entityName, String serviceName, int currentApprovalLevel, Menu menu, ApprovalMatrix approvalMatrix,
                                User maker, String label, String newData, String oldData){

        TaskMaker task = new TaskMaker();
        task.setActivityId(generateActivityId(taskType));
        task.setActivityName(taskType.name());
        task.setTaskType(taskType);
        task.setApprovalStatus(ApprovalStatus.MENUNGGU_PERSETUJUAN);
        task.setApprovalToken(UUID.randomUUID().toString());
        task.setApprovalToken(approvalToken);
        task.setDescription(description);
        task.setEntityId(entityId);
        task.setEntityName(entityName);
        task.setServiceName(serviceName);
        task.setCurrentApprovalLevel(currentApprovalLevel);
        task.setMenu(menu);
        task.setApprovalMatrix(approvalMatrix);
        task.setMaker(maker);
        task.setTaskLabel(label);
        task.setNewData(newData);
        task.setOldData(oldData);
        task.setCreatedDate(new Date());
        task.setCreatedDateEmail(maker.getEmail());

        return taskMakerRepository.save(task);

    }

    public TaskApprover createTaskApprover(TaskMaker taskMaker, User approver, int levelApprover, String levelStatus, int sequence, Menu menu){
        TaskApprover taskApprover = new TaskApprover(taskMaker);
        taskApprover.setCreatedDate(new Date());
        taskApprover.setApprover(approver);
        taskApprover.setLevelApprover(levelApprover);
        taskApprover.setLevelStatus(levelStatus);
        taskApprover.setSequence(sequence);
        taskApprover.setMenu(menu);
        taskApprover.setTaskMaker(taskMaker);

        return taskApproverRepository.save(taskApprover);
    }




    private String generateActivityId(TaskType taskType){
        List<TaskMaker> maker = taskMakerRepository.findByActivityIdContaining(taskType.getCode());
        int sequence = 1;
        String activityId = "";
        if(maker.size() > 0){
            String last = maker.get(0).getActivityId().substring(2);
            sequence = Integer.parseInt(last) + 1;
        }
        activityId = taskType.getCode()+ StringUtils.leftPad(String.valueOf(sequence), 10, "0");
        return activityId;
    }
}
