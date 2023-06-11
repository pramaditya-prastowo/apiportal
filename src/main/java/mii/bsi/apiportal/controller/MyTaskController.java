package mii.bsi.apiportal.controller;

import mii.bsi.apiportal.domain.task.TaskApprover;
import mii.bsi.apiportal.dto.task.DetailTaskResponseDTO;
import mii.bsi.apiportal.dto.task.MyTaskResponseDTO;
import mii.bsi.apiportal.dto.task.TaskApproverResponse;
import mii.bsi.apiportal.dto.task.TaskApproverSequenceResponse;
import mii.bsi.apiportal.service.TaskService;
import mii.bsi.apiportal.utils.ResponseHandling;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1.0/my-task")
public class MyTaskController {


    @Autowired
    private TaskService taskService;

    @GetMapping
    public ResponseEntity<ResponseHandling<MyTaskResponseDTO>> getAllTaskAdmin(@RequestHeader(HttpHeaders.AUTHORIZATION) String token){
        return taskService.getAllMyTask(token.substring(7));
    }

    @GetMapping("list-approver")
    public ResponseEntity<ResponseHandling<DetailTaskResponseDTO>> getAllTaskApprover(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String token, @RequestParam String entityId, @RequestParam String entityName){
        return taskService.getAllByEntityIdAndName(token.substring(7), entityId, entityName);
    }
}
