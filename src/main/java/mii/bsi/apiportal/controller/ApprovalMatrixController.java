package mii.bsi.apiportal.controller;

import mii.bsi.apiportal.domain.ApprovalMatrix;
import mii.bsi.apiportal.dto.AddMatrixRequestDTO;
import mii.bsi.apiportal.service.ApprovalMatrixService;
import mii.bsi.apiportal.utils.ResponseHandling;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1.0/approval-matrix")
public class ApprovalMatrixController {

    @Autowired
    private ApprovalMatrixService matrixService;

    @GetMapping
    public ResponseEntity<ResponseHandling<List<ApprovalMatrix>>> getAllApprovalMatrix(@RequestHeader(HttpHeaders.AUTHORIZATION) String token){
        return matrixService.getAll(token.substring(7));
    }

    @GetMapping("{id}")
    public ResponseEntity<ResponseHandling<ApprovalMatrix>> getDetail(@RequestHeader(HttpHeaders.AUTHORIZATION) String token,
                                                                            @PathVariable("id") Long id){
        return matrixService.getDetail(token.substring(7),id);
    }

    @PostMapping
    public ResponseEntity<ResponseHandling> createMatrix(@RequestHeader(HttpHeaders.AUTHORIZATION) String token,
                                                         @RequestBody AddMatrixRequestDTO request){
        return matrixService.createMatrix(token.substring(7), request);
//        ResponseHandling<AddMatrixRequestDTO> resonseData = new ResponseHandling<>();
//        resonseData.setPayload(request);
//        return ResponseEntity.ok(resonseData);
    }

    @PatchMapping("/status/{id}")
    public ResponseEntity<ResponseHandling> updateStatus(@RequestHeader(HttpHeaders.AUTHORIZATION) String token,
                                                         @PathVariable("id") Long id){
        return matrixService.disableMatrix(token.substring(7), id);

    }

    @PatchMapping("/{id}")
    public ResponseEntity<ResponseHandling> updateData(@RequestHeader(HttpHeaders.AUTHORIZATION) String token,
                                                         @PathVariable("id") Long id, @RequestBody AddMatrixRequestDTO request){
        return matrixService.updateMatrix(token.substring(7), id, request);
    }
}
