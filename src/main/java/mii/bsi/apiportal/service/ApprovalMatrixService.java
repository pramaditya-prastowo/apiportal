package mii.bsi.apiportal.service;

import mii.bsi.apiportal.domain.ApprovalGroup;
import mii.bsi.apiportal.domain.ApprovalMatrix;
import mii.bsi.apiportal.domain.ApprovalMatrixDetail;
import mii.bsi.apiportal.domain.Groups;
import mii.bsi.apiportal.repository.ApprovalGroupRepository;
import mii.bsi.apiportal.repository.ApprovalMatrixDetailRepository;
import mii.bsi.apiportal.repository.ApprovalMatrixRepository;
import mii.bsi.apiportal.utils.RequestData;
import mii.bsi.apiportal.utils.ResponseHandling;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ApprovalMatrixService {

    @Autowired
    private ApprovalMatrixRepository matrixRepository;
    @Autowired
    private ApprovalMatrixDetailRepository detailRepository;
    @Autowired
    private ApprovalGroupRepository approvalGroupRepository;

    public ResponseEntity<ResponseHandling<List<ApprovalMatrix>>> getAll(String token){
        ResponseHandling<List<ApprovalMatrix> > responseData = new ResponseHandling();
        RequestData requestData = new RequestData<>();

        try {

            List<ApprovalMatrix> list = matrixRepository.findAll();
            for (int i = 0; i < list.size(); i ++) {
                List<ApprovalMatrixDetail> listDetail = detailRepository.findByMatrixId(list.get(i).getId());
                for (ApprovalMatrixDetail detail: listDetail) {
                    System.out.println(detail.getId());
                    List<ApprovalGroup> listGroup = approvalGroupRepository.findByMatrixDetailId(detail.getId());
                    detail.setSelectedGroup(listGroup);
                }
                System.out.println(listDetail);
                list.get(i).setDetails(listDetail);
            }
            responseData.success();
            responseData.setPayload(list);
        }catch (Exception e){

        }
        return ResponseEntity.ok(responseData);
    }
}
