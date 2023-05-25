package mii.bsi.apiportal.controller;

import mii.bsi.apiportal.apigw.ApiGatewayService;
import mii.bsi.apiportal.domain.*;
import mii.bsi.apiportal.domain.task.TaskApprover;
import mii.bsi.apiportal.domain.task.TaskMaker;
import mii.bsi.apiportal.dto.task.TaskApproverResponse;
import mii.bsi.apiportal.repository.*;
import mii.bsi.apiportal.service.TaskService;
import mii.bsi.apiportal.utils.DateUtils;
import mii.bsi.apiportal.utils.EncryptUtility;
import mii.bsi.apiportal.utils.ResponseHandling;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/test")
public class TestController {

    @Autowired
    private ApiGatewayService apiGatewayService;
    @Autowired
    private EncryptUtility encryptUtility;
    @Autowired
    private DateUtils dateUtils;

    @GetMapping
    public String testing(){
        String corpId = "";
        String seqNumber = "2";
        corpId = "ABC"+ String.format("%05d", 2);
        return corpId;
    }

    @GetMapping("/string")
    public String testString(){
        String companyName = "PT Mitra Integrasi Informatika";

        int seq = 1;

        String name = companyName.toUpperCase().replaceAll("\\.", "").replaceAll("PT ","");
        String [] names  = name.split(" ");


        String initialName = "";
        if(names.length > 2){
            for (int i = 0; i < names.length; i++) {
                System.out.println(names[i]);
                initialName = initialName + names[i].charAt(0);
            }
        }else if(names.length == 1){
            initialName = names[0].substring(0,3);
        }else{
            initialName = names[0].substring(0,2) + names[1].charAt(0);
        }
        return initialName.substring(0,3) + String.format("%05d", seq);
    }

//    @GetMapping("/sign-auth")
//    public String testApiGw(){
//        return apiGatewayService.signatureAuth();
//    }

//    @GetMapping("/request-token")
//    public String testReqToken(){
//        String privateKey = "MIIEvwIBADANBgkqhkiG9w0BAQEFAASCBKkwggSlAgEAAoIBAQCggUTfA0dsY0Vg5f8Fzhenfxq3E6gbLp19lGLKSoFZYVflRncLJv/WzG8fJdS7AKq8vjzaAHymJoDJjVRUtmAIH1brBSYc1H9kAMkVdyGH+vnVHIVN/tEbbZg9BKgdpXR6LQDkD2rxwLsJ+OpYf10Z8nTWQ0lpew0aWvJIyBrbevo3AQ1shrytSYq5J8+Llek6ZxcpxtmE2uB0yy7/REhuMyTTBE61JXptkcLHpJkAG90DJvPZnAGaA/rK5M1Zb/AgIHI/VxmctcJgL6xGQ35wNXvG1WI6tvIEojVJ8V4QBU+/DEeeAFCbU0LNFpHA8nsKE7HU850FbnBCxiO+GNMVAgMBAAECggEAWrTTZCkpOxLbCYjRV8mByrHlOiOMtFfivy6VqfbzJL0DfFoXOqE+oniEMBkkIM6eru3f29+8kfVegQky6HDs0opLh6QwRsi8eZqMCWp686sdd5eXql1gdVy5VXqFO8PekJFQWJJyAM/HpDoczgikZ96Csvfxy/+zhvpwxDr6GqB+7SYXGY5ff9EgPwRsap7utDxnuJm5pv/Npiq5GUHN84QyNIKa3rXeZb0cZrvEkjKvAdBZ6TaZAN3Mc3m3aXHfgDwm0ZhlBAr2V8+oauepPamO15Phy4xSV5xMdBmsZJ2lMb44ZUrOmTxNkjo9gZ6l+crFUERg1+FK9hstZpKlYQKBgQDTBU6C4oMnR7WD3kbPEGT1UrjCNwD0ODPsUmaSFcVb3+lPBINQ5BQF2z4pHU4AEJA4MyoMPthGv6XlFvgoSLtfEbq2O+G1+ErsWbSnG9qTnGoox70S4BhjVZt016oY+nIjg7LJXojo0qNvT62inRzvoNzFMFTfoQO8+Df3AOXkzQKBgQDCt3xyMo2YYaeg+JUD43ad2zQ9Cic8WeR1iMOhZetkigQKJz+Z7BNAcaZl8aQwgmVuzHCH2T7fahAGeUnnATMh/IhCcKHZlfGYxc4NcwNUnQSZu6ZF6obLbUgnGAcGuN7sOxrT9AjHjKuo1tFpNP0a0z8Zvl6TLAONtv1vD73naQKBgQCq0ufsomtjl/RD7ONLak0gHzf72MUH7ptx2n64EbGznz5iPhgDmq7u0r2uUM+806u8IwcN5K32D9y+6Go5Si+MVXDdZvpf8cDNNg09HhpCVmPF4XOY3RpBB6MR4igLkmplf45y3vlb6HBvgoPgWOl8vq8ZXffHFLRO/G1poit4nQKBgQCEQiOKYeIhpfs5cH+vQ3qXYIRMDbB24sw2NW5EG7lW8hQqXVxrDZpKBKg0yHxw4rFJIB4zeBGnqSA3dX0IJp13sVNQZbbZ12piDcGXCw8xEvJEBdy70sA6PwFqZHypSTtKFB915mVsPZV/umJFZtOgu+o5b0BIEPZc9PWR0Yx5OQKBgQCZG2h96il0o9LTAVlyQEzmncMoQgJsSPbY3HB0NHopl+zIdYVmZeBLzGvG1PIr57AbCjZXqwexhdJ1E2b6HmaKMZ2xvOR+rnjTW4pEYLZ++xwMJUmLHXqQHT2sTOa3JHEY+qnCAzSfEyxzBHqLC3nqfjKoNZUOzN7RbCT6LZzmww==";
//        String clientKey ="5ca21839-0cd6-4094-b55a-2d53994bfbb3";
//        String timeStamp = dateUtils.dateIsoString(new Date());
//        String signature =  encryptUtility.generateSignature(privateKey, clientKey+"|"+timeStamp);
//        return apiGatewayService.requestToken(signature, timeStamp);
//    }

//    @GetMapping("/balance-inquiry")
//    public String balanceInquiry(){
//        String privateKey = "MIIEvwIBADANBgkqhkiG9w0BAQEFAASCBKkwggSlAgEAAoIBAQCggUTfA0dsY0Vg5f8Fzhenfxq3E6gbLp19lGLKSoFZYVflRncLJv/WzG8fJdS7AKq8vjzaAHymJoDJjVRUtmAIH1brBSYc1H9kAMkVdyGH+vnVHIVN/tEbbZg9BKgdpXR6LQDkD2rxwLsJ+OpYf10Z8nTWQ0lpew0aWvJIyBrbevo3AQ1shrytSYq5J8+Llek6ZxcpxtmE2uB0yy7/REhuMyTTBE61JXptkcLHpJkAG90DJvPZnAGaA/rK5M1Zb/AgIHI/VxmctcJgL6xGQ35wNXvG1WI6tvIEojVJ8V4QBU+/DEeeAFCbU0LNFpHA8nsKE7HU850FbnBCxiO+GNMVAgMBAAECggEAWrTTZCkpOxLbCYjRV8mByrHlOiOMtFfivy6VqfbzJL0DfFoXOqE+oniEMBkkIM6eru3f29+8kfVegQky6HDs0opLh6QwRsi8eZqMCWp686sdd5eXql1gdVy5VXqFO8PekJFQWJJyAM/HpDoczgikZ96Csvfxy/+zhvpwxDr6GqB+7SYXGY5ff9EgPwRsap7utDxnuJm5pv/Npiq5GUHN84QyNIKa3rXeZb0cZrvEkjKvAdBZ6TaZAN3Mc3m3aXHfgDwm0ZhlBAr2V8+oauepPamO15Phy4xSV5xMdBmsZJ2lMb44ZUrOmTxNkjo9gZ6l+crFUERg1+FK9hstZpKlYQKBgQDTBU6C4oMnR7WD3kbPEGT1UrjCNwD0ODPsUmaSFcVb3+lPBINQ5BQF2z4pHU4AEJA4MyoMPthGv6XlFvgoSLtfEbq2O+G1+ErsWbSnG9qTnGoox70S4BhjVZt016oY+nIjg7LJXojo0qNvT62inRzvoNzFMFTfoQO8+Df3AOXkzQKBgQDCt3xyMo2YYaeg+JUD43ad2zQ9Cic8WeR1iMOhZetkigQKJz+Z7BNAcaZl8aQwgmVuzHCH2T7fahAGeUnnATMh/IhCcKHZlfGYxc4NcwNUnQSZu6ZF6obLbUgnGAcGuN7sOxrT9AjHjKuo1tFpNP0a0z8Zvl6TLAONtv1vD73naQKBgQCq0ufsomtjl/RD7ONLak0gHzf72MUH7ptx2n64EbGznz5iPhgDmq7u0r2uUM+806u8IwcN5K32D9y+6Go5Si+MVXDdZvpf8cDNNg09HhpCVmPF4XOY3RpBB6MR4igLkmplf45y3vlb6HBvgoPgWOl8vq8ZXffHFLRO/G1poit4nQKBgQCEQiOKYeIhpfs5cH+vQ3qXYIRMDbB24sw2NW5EG7lW8hQqXVxrDZpKBKg0yHxw4rFJIB4zeBGnqSA3dX0IJp13sVNQZbbZ12piDcGXCw8xEvJEBdy70sA6PwFqZHypSTtKFB915mVsPZV/umJFZtOgu+o5b0BIEPZc9PWR0Yx5OQKBgQCZG2h96il0o9LTAVlyQEzmncMoQgJsSPbY3HB0NHopl+zIdYVmZeBLzGvG1PIr57AbCjZXqwexhdJ1E2b6HmaKMZ2xvOR+rnjTW4pEYLZ++xwMJUmLHXqQHT2sTOa3JHEY+qnCAzSfEyxzBHqLC3nqfjKoNZUOzN7RbCT6LZzmww==";
//        String clientKey ="5ca21839-0cd6-4094-b55a-2d53994bfbb3";
//        String timeStamp = dateUtils.dateIsoString(new Date());
//        String signature =  encryptUtility.generateSignature(privateKey, clientKey+"|"+timeStamp);
//        String responseToken = apiGatewayService.requestToken(signature, timeStamp);
//        JSONObject jsonObject = new JSONObject(responseToken);
//        String tokenBearer = jsonObject.getString("accessToken");
//        return apiGatewayService.balanceInquiry(signature, timeStamp, tokenBearer);
//    }

    @GetMapping("/generate-token")
    public String generateToken(){
        String privateKey = "MIIEvwIBADANBgkqhkiG9w0BAQEFAASCBKkwggSlAgEAAoIBAQCggUTfA0dsY0Vg5f8Fzhenfxq3E6gbLp19lGLKSoFZYVflRncLJv/WzG8fJdS7AKq8vjzaAHymJoDJjVRUtmAIH1brBSYc1H9kAMkVdyGH+vnVHIVN/tEbbZg9BKgdpXR6LQDkD2rxwLsJ+OpYf10Z8nTWQ0lpew0aWvJIyBrbevo3AQ1shrytSYq5J8+Llek6ZxcpxtmE2uB0yy7/REhuMyTTBE61JXptkcLHpJkAG90DJvPZnAGaA/rK5M1Zb/AgIHI/VxmctcJgL6xGQ35wNXvG1WI6tvIEojVJ8V4QBU+/DEeeAFCbU0LNFpHA8nsKE7HU850FbnBCxiO+GNMVAgMBAAECggEAWrTTZCkpOxLbCYjRV8mByrHlOiOMtFfivy6VqfbzJL0DfFoXOqE+oniEMBkkIM6eru3f29+8kfVegQky6HDs0opLh6QwRsi8eZqMCWp686sdd5eXql1gdVy5VXqFO8PekJFQWJJyAM/HpDoczgikZ96Csvfxy/+zhvpwxDr6GqB+7SYXGY5ff9EgPwRsap7utDxnuJm5pv/Npiq5GUHN84QyNIKa3rXeZb0cZrvEkjKvAdBZ6TaZAN3Mc3m3aXHfgDwm0ZhlBAr2V8+oauepPamO15Phy4xSV5xMdBmsZJ2lMb44ZUrOmTxNkjo9gZ6l+crFUERg1+FK9hstZpKlYQKBgQDTBU6C4oMnR7WD3kbPEGT1UrjCNwD0ODPsUmaSFcVb3+lPBINQ5BQF2z4pHU4AEJA4MyoMPthGv6XlFvgoSLtfEbq2O+G1+ErsWbSnG9qTnGoox70S4BhjVZt016oY+nIjg7LJXojo0qNvT62inRzvoNzFMFTfoQO8+Df3AOXkzQKBgQDCt3xyMo2YYaeg+JUD43ad2zQ9Cic8WeR1iMOhZetkigQKJz+Z7BNAcaZl8aQwgmVuzHCH2T7fahAGeUnnATMh/IhCcKHZlfGYxc4NcwNUnQSZu6ZF6obLbUgnGAcGuN7sOxrT9AjHjKuo1tFpNP0a0z8Zvl6TLAONtv1vD73naQKBgQCq0ufsomtjl/RD7ONLak0gHzf72MUH7ptx2n64EbGznz5iPhgDmq7u0r2uUM+806u8IwcN5K32D9y+6Go5Si+MVXDdZvpf8cDNNg09HhpCVmPF4XOY3RpBB6MR4igLkmplf45y3vlb6HBvgoPgWOl8vq8ZXffHFLRO/G1poit4nQKBgQCEQiOKYeIhpfs5cH+vQ3qXYIRMDbB24sw2NW5EG7lW8hQqXVxrDZpKBKg0yHxw4rFJIB4zeBGnqSA3dX0IJp13sVNQZbbZ12piDcGXCw8xEvJEBdy70sA6PwFqZHypSTtKFB915mVsPZV/umJFZtOgu+o5b0BIEPZc9PWR0Yx5OQKBgQCZG2h96il0o9LTAVlyQEzmncMoQgJsSPbY3HB0NHopl+zIdYVmZeBLzGvG1PIr57AbCjZXqwexhdJ1E2b6HmaKMZ2xvOR+rnjTW4pEYLZ++xwMJUmLHXqQHT2sTOa3JHEY+qnCAzSfEyxzBHqLC3nqfjKoNZUOzN7RbCT6LZzmww==";
        String clientKey ="5ca21839-0cd6-4094-b55a-2d53994bfbb3";
        String timeStamp = dateUtils.dateIsoString(new Date());
        return encryptUtility.generateSignature(privateKey, clientKey+"|"+timeStamp);


    }

    @Autowired
    private ApprovalMatrixRepository approvalMatrixRepository;
    @Autowired
    private MenuRepository menuRepository;
    @Autowired
    private ApprovalMatrixDetailRepository approvalMatrixDetailRepository;
    @Autowired
    private ApprovalGroupRepository approvalGroupRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TaskMakerRepository taskMakerRepository;
    @Autowired
    private TaskService taskService;

    @PostMapping("/kerjasama")
    public ResponseEntity<ResponseHandling<Object>> testingKerjasama(){
        ResponseHandling<Object> responseData = new ResponseHandling<>();

        try {
            List<TaskMaker> maker = taskMakerRepository.findByActivityIdContaining("PK");
//            List<TaskMaker> maker = new ArrayList<>();
            int sequence = 1;
            String activityId = "";
            if(maker.size() > 0){
                String last = maker.get(0).getActivityId().substring(2);
                sequence = Integer.parseInt(last) + 1;
            }
            activityId = "PK"+ StringUtils.leftPad(String.valueOf(sequence), 10, "0");

//            Menu menu = menuRepository.findByPermissionName("MAINTAIN_KERJASAMA");
//            ApprovalMatrix approvalMatrix = approvalMatrixRepository.findByMenuId(menu.getId());
//            List<ApprovalMatrixDetail> approvalMatrixDetails = approvalMatrixDetailRepository.findByMatrixId(approvalMatrix.getId());
//            for (ApprovalMatrixDetail detail: approvalMatrixDetails) {
//                List<ApprovalGroup> groups = approvalGroupRepository.findByMatrixDetailId(detail.getId());
//                detail.setSelectedGroup(groups);
//                for (ApprovalGroup group: groups) {
//                    List<User> userList = userRepository.findByGroupId(group.getGroup().getId());
//                    for (User user: userList) {
//                        System.out.println(user.getId()+" : " + user.getFirstName() + " "+ user.getLastName() );
//                    }
//                }
//            }
//            approvalMatrix.setDetails(approvalMatrixDetails);
            responseData.setPayload(activityId);
            return ResponseEntity.ok(responseData);
        }catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }


    }

    @GetMapping("/task")
    public ResponseEntity<ResponseHandling<List<TaskApproverResponse>>> getTask(@RequestHeader(HttpHeaders.AUTHORIZATION) String token, @RequestParam String entityId, @RequestParam String entityName){
        return taskService.getAllByEntityIdAndName(token.substring(7), entityId, entityName);
    }
}
