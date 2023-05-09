package mii.bsi.apiportal.controller;

import mii.bsi.apiportal.domain.ApprovalMatrix;
import mii.bsi.apiportal.service.ApprovalMatrixService;
import mii.bsi.apiportal.utils.ResponseHandling;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
