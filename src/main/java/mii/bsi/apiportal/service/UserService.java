package mii.bsi.apiportal.service;

import java.text.SimpleDateFormat;
import java.util.*;

import javax.validation.Valid;

import io.jsonwebtoken.Claims;
import mii.bsi.apiportal.constant.Params;
import mii.bsi.apiportal.constant.StatusCode;
import mii.bsi.apiportal.constant.UserAction;
import mii.bsi.apiportal.constant.sql.BuilderUserQuery;
import mii.bsi.apiportal.constant.sql.BuilderUserQueryImpl;
import mii.bsi.apiportal.constant.sql.ConstantQuery;
import mii.bsi.apiportal.constant.sql.MyPagination;
import mii.bsi.apiportal.domain.BsiTokenVerification;
import mii.bsi.apiportal.domain.SystemNotification;
import mii.bsi.apiportal.domain.model.FilterGetData;
import mii.bsi.apiportal.domain.model.Roles;
import mii.bsi.apiportal.domain.model.TokenVerificationType;
import mii.bsi.apiportal.dto.AuthenticationResponseDTO;
import mii.bsi.apiportal.dto.ChangePasswordRequestDTO;
import mii.bsi.apiportal.dto.UserResponseDTO;
import mii.bsi.apiportal.dto.VerificationEmailRequest;
import mii.bsi.apiportal.repository.BsiTokenVerificationRepository;
import mii.bsi.apiportal.utils.*;
import mii.bsi.apiportal.validation.EmailValidation;
import mii.bsi.apiportal.validation.UserValidation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;

import mii.bsi.apiportal.domain.User;
import mii.bsi.apiportal.repository.UserRepository;

@Service
public class UserService {

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private LogService logService;

    public static final String FETCH_ALL_USER = "Fetch All User";
    public static final String FETCH_USER_FILTER = "Fetch User FILTER";
    public static final String FETCH_DETAIL_USER = "Fetch Detail User";
    public static final String REGISTER = "Register";

    public static final String REGISTER_BY_ADMIN = "Register by Admin";
    public static final String EMAIL_VERIFICATION = "Email Verification";
    public static final String RESEND_EMAIL_VERIFICATION = "Resend Email Verification";
    public static final String DELETE_USER = "Delete User";
    public static final String UPDATE_BY_ADMIN = "Update by Admin";
    public static final String UPDATE_BY_MITRA = "Update by Mitra";
    public static final String COUNT_MITRA = "Count Mitra";
    public static final String CHANGE_PASSWORD = "Change Password";

    @Autowired
    private BsiTokenVerificationRepository tokenRepository;

    @Autowired
    private NamedParameterJdbcTemplate jdbc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailUtility emailUtility;

    @Autowired
    private JwtUtility jwtUtility;

    @Autowired
    private EncryptUtility encryptUtility;
    @Autowired
    private EmailValidation emailValidation;

    @Autowired
    private UserValidation userValidation;

    @Autowired
    private QueryUtils queryUtils;

    @Autowired
    private MyPagination<UserResponseDTO> myPagination;
    @Autowired
    private BuilderUserQuery builderUser;

    public ResponseHandling<User> create(@Valid User user, Errors errors) {
        ResponseHandling<User> responseHandling = new ResponseHandling<>();
        System.out.println(user);
        try {
            String sequence = userRepository.getUserSequence();
            String pattern = "yyyyMMddHHmmss";
            SimpleDateFormat timestamp = new SimpleDateFormat(pattern);
            String date = timestamp.format(new Date());
            String idUser = date.concat(StringUtils.leftPad(sequence, 4, "0"));
            user.setId(idUser);
            user.setAccountInactive(false);
            user.setAccountLocked(false);
            user.setRetryPasswordCount(0);
            responseHandling.setPayload(userRepository.save(user));
            responseHandling.setResponseCode("00");
            responseHandling.setResponseMessage("success");
        } catch (Exception e) {
            errors.hasErrors();
            for (ObjectError err : errors.getAllErrors()) {
                responseHandling.getMessageError().add(err.getDefaultMessage());
            }
            responseHandling.setResponseCode("99");
            responseHandling.setResponseMessage("failed");
            responseHandling.setPayload(user);
        }
        return responseHandling;
    }

    public ResponseEntity<ResponseHandling> update(User user, String token) {
        ResponseHandling responseData = new ResponseHandling<>();
        RequestData requestData = new RequestData();

        try {

            User newUser = userRepository.findById(user.getId()).orElse(null);
            if(newUser == null){
                responseData.failed("User tidak ditemukan");
                logService.saveLog(new RequestData<>(), responseData, StatusCode.NOT_FOUND, this.getClass().getName(),
                        UPDATE_BY_ADMIN);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseData);
            }

            final String username = jwtUtility.getUsernameFromToken(token);
            User userUpdate = userRepository.findByEmail(username);
            if(userUpdate == null){
                responseData.failed("Akses ditolak");
                logService.saveLog(requestData, responseData, StatusCode.FORBIDDEN, this.getClass().getName(), UPDATE_BY_ADMIN);
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(responseData);
            }
            System.out.println(newUser.getId());
            System.out.println(newUser.getFirstName());
            System.out.println(newUser.getLastName());
            System.out.println(newUser.getAuthPrincipal());
            System.out.println(user.getId());

            newUser.setFirstName(user.getFirstName());
            newUser.setLastName(user.getLastName());
//            newUser.setCorporateName(user.getCorporateName());
//            newUser.setAuthPrincipal(user.getAuthPrincipal());
            newUser.setUpdateDate(new Date());
            newUser.setUpdateBy(user.getId());
            newUser.setMobilePhone(user.getMobilePhone());
            userRepository.save(newUser);

            responseData.success();

        } catch (Exception e) {
            e.printStackTrace();
            responseData.failed(e.getMessage());
            logService.saveLog(new RequestData<>(), responseData, StatusCode.INTERNAL_SERVER_ERROR, this.getClass().getName(),
                    UPDATE_BY_ADMIN);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseData);
        }
        logService.saveLog(new RequestData<>(), responseData, StatusCode.OK, this.getClass().getName(),
                UPDATE_BY_ADMIN);
        return ResponseEntity.ok(responseData);
    }

    public ResponseEntity<ResponseHandling> changePassword(String token, ChangePasswordRequestDTO requestDTO, Errors errors){
        ResponseHandling responseData = new ResponseHandling<>();
        RequestData<ChangePasswordRequestDTO> requestData = new RequestData();
        requestData.setPayload(requestDTO);

        try {

            if (errors.hasErrors()) {
                responseData.failed(CustomError.validRequest(errors), "Bad Request");
                logService.saveLog(requestData, responseData, StatusCode.BAD_REQUEST, this.getClass().getName(),
                        CHANGE_PASSWORD);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseData);
            }
            User user = userValidation.getUserFromToken(token);

            if(user == null){
                responseData.failed("User not found");
                logService.saveLog(requestData, responseData, StatusCode.NOT_FOUND, this.getClass().getName(),
                        CHANGE_PASSWORD);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseData);
            }

            if(passwordEncoder.matches(requestDTO.getOldPassword(), user.getPassword())){
                user.setPassword(passwordEncoder.encode(requestDTO.getNewPassword()));
                userRepository.save(user);
                responseData.success();
//                return ResponseEntity.ok(responseData);
            }else{
                responseData.failed("Password Lama Salah");
                logService.saveLog(requestData, responseData, StatusCode.UNAUTHORIZED, this.getClass().getName(),
                        CHANGE_PASSWORD);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseData);
            }

        } catch (Exception e) {
            e.printStackTrace();
            responseData.failed(e.getMessage());
            logService.saveLog(new RequestData<>(), responseData, StatusCode.INTERNAL_SERVER_ERROR, this.getClass().getName(),
                    UPDATE_BY_ADMIN);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseData);
        }
        logService.saveLog(new RequestData<>(), responseData, StatusCode.OK, this.getClass().getName(),
                UPDATE_BY_ADMIN);
        return ResponseEntity.ok(responseData);
    }

    public ResponseEntity<ResponseHandling> updateByAdmin(User user, String token) {
        ResponseHandling responseData = new ResponseHandling<>();
        RequestData requestData = new RequestData();

        try {

            User newUser = userRepository.findById(user.getId()).orElse(null);
            if(newUser == null){
                responseData.failed("User tidak ditemukan");
                logService.saveLog(new RequestData<>(), responseData, StatusCode.NOT_FOUND, this.getClass().getName(),
                        UPDATE_BY_ADMIN);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseData);
            }

            final Claims claim = jwtUtility.getAllClaimsFromToken(token);
            if(!(claim.get("role").equals(Roles.SUPER_ADMIN.toString()) || claim.get("role").equals(Roles.ADMIN.toString()))){
                responseData.failed("Akses ditolak");
                logService.saveLog(requestData, responseData, StatusCode.FORBIDDEN, this.getClass().getName(), UPDATE_BY_ADMIN);
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(responseData);
            }

            if((user.getAuthPrincipal().equals(Roles.ADMIN) || user.getAuthPrincipal().equals(Roles.SUPER_ADMIN))){

                if(!claim.get("role").equals(Roles.SUPER_ADMIN.toString())){
                    responseData.failed("Akses ditolak");
                    logService.saveLog(requestData, responseData, StatusCode.FORBIDDEN, this.getClass().getName(), UPDATE_BY_ADMIN);
                    return ResponseEntity.status(HttpStatus.FORBIDDEN).body(responseData);
                }

            }

            final String username = jwtUtility.getUsernameFromToken(token);
            User admin = userRepository.findByEmail(username);
            if(admin == null){
                responseData.failed("Akses ditolak");
                logService.saveLog(requestData, responseData, StatusCode.FORBIDDEN, this.getClass().getName(), UPDATE_BY_ADMIN);
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(responseData);
            }

            newUser.setFirstName(user.getFirstName());
            newUser.setLastName(user.getLastName());
            newUser.setMobilePhone(user.getMobilePhone());
            newUser.setCorporateName(user.getCorporateName());
            newUser.setAuthPrincipal(user.getAuthPrincipal());
            newUser.setUpdateDate(new Date());
            newUser.setUpdateBy(admin.getId());
            newUser.setGroupId(user.getGroupId());
            userRepository.save(newUser);

            responseData.success();

        } catch (Exception e) {
            e.printStackTrace();
            responseData.failed(e.getMessage());
            logService.saveLog(new RequestData<>(), responseData, StatusCode.INTERNAL_SERVER_ERROR, this.getClass().getName(),
                    UPDATE_BY_ADMIN);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseData);
        }
        logService.saveLog(new RequestData<>(), responseData, StatusCode.OK, this.getClass().getName(),
                UPDATE_BY_ADMIN);
        return ResponseEntity.ok(responseData);
    }


    public ResponseEntity<ResponseHandling<List<UserResponseDTO>>> getAll(String token, String typeUser) {
        ResponseHandling<List<UserResponseDTO>> responseData = new ResponseHandling<>();
        try {

            final Claims claim = jwtUtility.getAllClaimsFromToken(token);
            claim.get("role");
            if(!(claim.get("role").equals(Roles.SUPER_ADMIN.toString()) || claim.get("role").equals(Roles.ADMIN.toString()))){
                responseData.failed("Access denied");
                logService.saveLog(new RequestData<>(), responseData, StatusCode.FORBIDDEN, this.getClass().getName(),
                        FETCH_ALL_USER);
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(responseData);
            }


            final String username = jwtUtility.getUsernameFromToken(token);
            User user = userRepository.findByEmail(username);

            if(!(user.getAuthPrincipal().equals(Roles.SUPER_ADMIN) || user.getAuthPrincipal().equals(Roles.ADMIN))){
                responseData.failed("Access denied");
                logService.saveLog(new RequestData<>(), responseData, StatusCode.FORBIDDEN, this.getClass().getName(),
                        FETCH_ALL_USER);
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(responseData);
            }

            List<User> userList = userRepository.findByAccountActiveAndType(typeUser);
            List<UserResponseDTO> userListResponse = new ArrayList<>();
            for (User data: userList) {

                userListResponse.add(new UserResponseDTO(
                        data.getId(),
                        data.getFirstName(),
                        data.getLastName(),
                        data.getEmail(),
                        data.getCorporateName(),
                        data.isAccountInactive(),
                        data.isAccountLocked(),
                        data.getMobilePhone(),
                        data.getAuthPrincipal(),
                        data.isEmailVerified(),
                        data.getCreateDate(),
                        data.getGroupId()
                ));
            }
            responseData.setPayload(userListResponse);
            responseData.success("success");
        } catch (Exception e) {
            e.printStackTrace();
            responseData.failed(e.getMessage());
            logService.saveLog(new RequestData<>(), responseData, StatusCode.INTERNAL_SERVER_ERROR, this.getClass().getName(),
                    FETCH_ALL_USER);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseData);
        }
        logService.saveLog(new RequestData<>(), responseData, StatusCode.OK, this.getClass().getName(),
                FETCH_ALL_USER);
        return ResponseEntity.ok(responseData);
    }

    public ResponseEntity<ResponseHandling<RowDataResponse<UserResponseDTO>>> getFilter(String token, FilterGetData filter){
        ResponseHandling<RowDataResponse<UserResponseDTO>> responseData =  new ResponseHandling<>();
        RequestData<Map<String, Object>> requestData = new RequestData<>();
        try {
            User user = userValidation.getUserFromToken(token);
            if(user == null){
                responseData.failed("User not found");
                logService.saveLog(requestData, responseData, StatusCode.NOT_FOUND, this.getClass().getName(),
                        FETCH_USER_FILTER);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseData);
            }
            if(!userValidation.isAdmin(token)){
                responseData.failed("Access denied");
                logService.saveLog(requestData, responseData, StatusCode.FORBIDDEN, this.getClass().getName(),
                        FETCH_USER_FILTER);
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(responseData);
            }
            String sql = builderUser.getWithFilter(ConstantQuery.user, ConstantQuery.userOrder, filter);
            RowDataResponse<UserResponseDTO> rowDataResponse = myPagination.getData(filter, sql, UserResponseDTO.class);

//            MapSqlParameterSource paramSource = new MapSqlParameterSource();
//            for(Map.Entry<String,String> itr : filter.getFilter().entrySet()){
//                paramSource.addValue(itr.getKey(), itr.getValue());
//            }
//
//            paramSource.addValue("pageNumber", filter.getPageNumber());
//            paramSource.addValue("pageSize", filter.getPageSize());
//
//            String sql = BuilderUserQuery.getWithFilter(ConstantQuery.user, ConstantQuery.userOrder, filter);
//            String pagination = queryUtils.generatePagination(sql, filter.getOrderBy(), filter.getSort());
//            logService.logQuery("\r\nsql = \r\n" + pagination);
//
//            String total = queryUtils.countTotalRows(sql);
//            String totalRecord = jdbc.queryForObject(total, paramSource, String.class);
//            List<UserResponseDTO> dataRecord = jdbc.query(pagination, paramSource,
//                    BeanPropertyRowMapper.newInstance(UserResponseDTO.class));
//
//            RowDataResponse<UserResponseDTO> rowDataResponse = new RowDataResponse<>();
//            rowDataResponse.setRowData(dataRecord.size());
//            rowDataResponse.setData(dataRecord);
//            rowDataResponse.setTotalData(Integer.parseInt(totalRecord));

            responseData.success();
            responseData.setPayload(rowDataResponse);
            return ResponseEntity.ok(responseData);

        }catch (Exception e){
            e.printStackTrace();
            responseData.failed(e.getMessage());
            logService.saveLog(requestData, responseData, StatusCode.INTERNAL_SERVER_ERROR, this.getClass().getName(),
                    FETCH_USER_FILTER);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseData);
        }


    }

    public ResponseEntity<ResponseHandling<User>> getById(String id) {
        ResponseHandling<User> responseHandling = new ResponseHandling<>();
        try {
            responseHandling.setPayload(userRepository.findById(id).get());
            responseHandling.setResponseCode("00");
            responseHandling.setResponseMessage("success");
        } catch (Exception e) {
            responseHandling.setResponseCode("99");
            responseHandling.setResponseMessage("failed");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(responseHandling);
    }

    public ResponseEntity<ResponseHandling<AuthenticationResponseDTO>> getDetailUser(String token) {
        ResponseHandling<AuthenticationResponseDTO> responseHandling = new ResponseHandling<>();
        try {
            User user = userValidation.getUserFromToken(token);
            if(user == null){
                responseHandling.failed("User not found");
                logService.saveLog(new RequestData<>(), responseHandling, StatusCode.NOT_FOUND, this.getClass().getName(),
                        FETCH_DETAIL_USER);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseHandling);
            }

            AuthenticationResponseDTO responseDTO = new AuthenticationResponseDTO(user, "", new Date());

            responseHandling.setPayload(responseDTO);
            responseHandling.setResponseCode("00");
            responseHandling.setResponseMessage("success");
        } catch (Exception e) {
            responseHandling.setResponseCode("99");
            responseHandling.setResponseMessage("failed");
        }
        return ResponseEntity.status(HttpStatus.OK).body(responseHandling);
    }

    public ResponseEntity<ResponseHandling<User>> register(User user, Errors errors) {
        ResponseHandling<User> responseData = new ResponseHandling<>();
        RequestData<User> requestData = new RequestData<>();
        requestData.setPayload(user);

        try {

            if (errors.hasErrors()) {
                responseData.failed(CustomError.validRequest(errors), "Bad Request");
                requestData.getPayload().setPassword(passwordEncoder.encode(user.getPassword()));
                logService.saveLog(requestData, responseData, StatusCode.BAD_REQUEST, this.getClass().getName(),
                        REGISTER);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseData);
            }

            if(!emailValidation.validEmail(user.getEmail())){
                responseData.failed("Silahkan gunakan Email perusahaan anda");
                requestData.getPayload().setPassword(passwordEncoder.encode(user.getPassword()));
                logService.saveLog(requestData, responseData, StatusCode.BAD_REQUEST, this.getClass().getName(),
                        REGISTER);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseData);
            }

            User userExist = userRepository.findByEmail(requestData.getPayload().getEmail());
            if (userExist != null) {
                responseData.failed("Email sudah terdaftar");
                requestData.getPayload().setPassword(passwordEncoder.encode(user.getPassword()));
                logService.saveLog(requestData, responseData, StatusCode.CONFLICT, this.getClass().getName(), REGISTER);
                return ResponseEntity.status(HttpStatus.CONFLICT).body(responseData);
            }

            String sequence = userRepository.getUserSequence();
            user.generateCreated(sequence);
            user.setPassword(passwordEncoder.encode(user.getPassword()));

            BsiTokenVerification tokenVerification = new BsiTokenVerification();
            tokenVerification.generateToken();
            tokenVerification.setUser(user);
            tokenVerification.setIdToken(null);
            tokenVerification.setTokenType(TokenVerificationType.EMAIL_VERIFICATION);

            final String encToken = encryptUtility.encryptAES(tokenVerification.getToken(), Params.PASS_KEY);

            SystemNotification notification = new SystemNotification();
//            notification.set
            emailUtility.sendEmailVerification(user, encToken);

            userRepository.save(user);
            tokenRepository.save(tokenVerification);
            responseData.success();
            requestData.getPayload().setPassword(passwordEncoder.encode(user.getPassword()));

        } catch (Exception e) {
            responseData.failed(e.getMessage());
            e.printStackTrace();
            requestData.getPayload().setPassword(passwordEncoder.encode(user.getPassword()));
            logService.saveLog(requestData, responseData, StatusCode.INTERNAL_SERVER_ERROR, this.getClass().getName(),
                    REGISTER);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseData);
        }
        logService.saveLog(requestData, responseData, StatusCode.CREATED, this.getClass().getName(), REGISTER);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseData);
    }

    public ResponseEntity<ResponseHandling<User>> registerByAdmin(User user,String token, Errors errors) {
        ResponseHandling<User> responseData = new ResponseHandling<>();
        RequestData<User> requestData = new RequestData<>();
        requestData.setPayload(user);

        System.out.println("bad");
        try {

            if (errors.hasErrors()) {
                responseData.failed(CustomError.validRequest(errors), "Bad Request");

                requestData.getPayload().setPassword(passwordEncoder.encode(user.getPassword()));
                logService.saveLog(requestData, responseData, StatusCode.BAD_REQUEST, this.getClass().getName(),
                        REGISTER_BY_ADMIN);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseData);
            }

            if(!emailValidation.validEmail(user.getEmail())){
                responseData.failed("Silahkan gunakan Email perusahaan anda");
                requestData.getPayload().setPassword(passwordEncoder.encode(user.getPassword()));
                logService.saveLog(requestData, responseData, StatusCode.BAD_REQUEST, this.getClass().getName(),
                        REGISTER);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseData);
            }

            User userExist = userRepository.findByEmail(requestData.getPayload().getEmail());
            if (userExist != null) {
                responseData.failed("Email is already register");
                requestData.getPayload().setPassword(passwordEncoder.encode(user.getPassword()));
                logService.saveLog(requestData, responseData, StatusCode.CONFLICT, this.getClass().getName(), REGISTER_BY_ADMIN);
                return ResponseEntity.status(HttpStatus.CONFLICT).body(responseData);
            }


            final Claims claim = jwtUtility.getAllClaimsFromToken(token);
            if(!(claim.get("role").equals(Roles.SUPER_ADMIN.toString()) || claim.get("role").equals(Roles.ADMIN.toString()))){
                System.out.println("Di atas");
                responseData.failed("Access denied");
                requestData.getPayload().setPassword(passwordEncoder.encode(user.getPassword()));
                logService.saveLog(requestData, responseData, StatusCode.FORBIDDEN, this.getClass().getName(), REGISTER_BY_ADMIN);
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(responseData);
            }

            if((user.getAuthPrincipal().equals(Roles.ADMIN) || user.getAuthPrincipal().equals(Roles.SUPER_ADMIN))){

                if(!claim.get("role").equals(Roles.SUPER_ADMIN.toString())){
                    System.out.println("Di bawah");
                    responseData.failed("Access denied");
                    requestData.getPayload().setPassword(passwordEncoder.encode(user.getPassword()));
                    logService.saveLog(requestData, responseData, StatusCode.FORBIDDEN, this.getClass().getName(), REGISTER_BY_ADMIN);
                    return ResponseEntity.status(HttpStatus.FORBIDDEN).body(responseData);
                }

            }

            final String username = jwtUtility.getUsernameFromToken(token);
            User admin = userRepository.findByEmail(username);

            String sequence = userRepository.getUserSequence();
            user.generateCreated(sequence);
            user.setCreateBy(admin.getId());
            user.setPassword(passwordEncoder.encode(user.getPassword()));

            System.out.println(user);

            BsiTokenVerification tokenVerification = new BsiTokenVerification();
            tokenVerification.generateToken();
            tokenVerification.setUser(user);
            tokenVerification.setIdToken(null);
            tokenVerification.setTokenType(TokenVerificationType.EMAIL_VERIFICATION);

            final String encToken = encryptUtility.encryptAES(tokenVerification.getToken(), Params.PASS_KEY);

            //send email verification
            emailUtility.sendEmailVerification(user, encToken);

            userRepository.save(user);
            tokenRepository.save(tokenVerification);
            responseData.success();
            requestData.getPayload().setPassword(passwordEncoder.encode(user.getPassword()));

        } catch (Exception e) {
            responseData.failed(e.getMessage());
            e.printStackTrace();
            requestData.getPayload().setPassword(passwordEncoder.encode(user.getPassword()));
            logService.saveLog(requestData, responseData, StatusCode.INTERNAL_SERVER_ERROR, this.getClass().getName(),
                    REGISTER_BY_ADMIN);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseData);
        }
        logService.saveLog(requestData, responseData, StatusCode.CREATED, this.getClass().getName(), REGISTER_BY_ADMIN);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseData);
    }

    public ResponseEntity<ResponseHandling> confirmEmailVerification(VerificationEmailRequest request, Errors errors) {
        ResponseHandling responseData = new ResponseHandling();
        RequestData<VerificationEmailRequest> requestData = new RequestData<>();
        requestData.setPayload(request);

        try {

            if(errors.hasErrors()){
                responseData.failed(CustomError.validRequest(errors), "Bad request");
                logService.saveLog(requestData, responseData, StatusCode.BAD_REQUEST, this.getClass().getName(),
                        EMAIL_VERIFICATION);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseData);
            }

            final String decToken = encryptUtility.decryptAES(request.getToken(), Params.PASS_KEY);
            final String decUid = encryptUtility.decryptAES(request.getId(), Params.PASS_KEY);

            BsiTokenVerification resultToken = tokenRepository.findByToken(decToken);
            if(resultToken == null){
                responseData.failed("Token is not valid");
                logService.saveLog(requestData, responseData, StatusCode.BAD_REQUEST, this.getClass().getName(),
                        EMAIL_VERIFICATION);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseData);
            }

            User user = userRepository.findByEmail(resultToken.getValidEmail());
            if(user.isEmailVerified()){
                responseData.success("Email already verified");
                logService.saveLog(requestData, responseData, StatusCode.OK, this.getClass().getName(), EMAIL_VERIFICATION);
                return ResponseEntity.ok(responseData);
            }

            if(!resultToken.getUserId().equals(decUid)){
                responseData.failed("ID is not valid");
                logService.saveLog(requestData, responseData, StatusCode.BAD_REQUEST, this.getClass().getName(), EMAIL_VERIFICATION);
                return ResponseEntity.badRequest().body(responseData);
            }

            if (resultToken.isTokenExpired()) {
                responseData.failed("Token is expired");
                logService.saveLog(requestData, responseData, StatusCode.GONE, this.getClass().getName(),
                        EMAIL_VERIFICATION);
                return ResponseEntity.status(HttpStatus.GONE).body(responseData);
            }

//            User user = userRepository.findByEmail(resultToken.getValidEmail());
            user.setEmailVerified(true);
            user.setEmailVerifiedDate(new Date());
            userRepository.save(user);
            responseData.success("Verifikasi Email Berhasil");
        } catch (Exception e) {
            responseData.failed(e.getMessage());
            e.printStackTrace();
            logService.saveLog(requestData, responseData, StatusCode.INTERNAL_SERVER_ERROR, this.getClass().getName(), EMAIL_VERIFICATION);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseData);
        }
        logService.saveLog(requestData, responseData, StatusCode.OK, this.getClass().getName(), EMAIL_VERIFICATION);
        return ResponseEntity.ok(responseData);

    }

    public ResponseEntity<ResponseHandling> resendEmailVerification(String email) {
        ResponseHandling responseData = new ResponseHandling();
        RequestData<Map<String, Object>> requestData = new RequestData<>();
        Map<String, Object> request = new HashMap<>();
        request.put("email", email);
        requestData.setPayload(request);

        try {
            if (email.equals(null) || email.equals("")) {
                responseData.failed("Email is required");
                logService.saveLog(requestData, responseData, StatusCode.BAD_REQUEST, this.getClass().getName(),
                        RESEND_EMAIL_VERIFICATION);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseData);
            }

            User user = userRepository.findByEmail(email);
            if (user == null) {
                responseData.failed("Email is not register in system");
                logService.saveLog(requestData, responseData, StatusCode.NOT_FOUND, this.getClass().getName(),
                        RESEND_EMAIL_VERIFICATION);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseData);
            }

            BsiTokenVerification tokenVerification = new BsiTokenVerification();
            tokenVerification.generateToken();
            tokenVerification.setUser(user);
            tokenVerification.setIdToken(null);
            tokenVerification.setTokenType(TokenVerificationType.EMAIL_VERIFICATION);

            final String encToken = encryptUtility.encryptAES(tokenVerification.getToken(), Params.PASS_KEY);

            emailUtility.sendEmailVerification(user, encToken);

            tokenRepository.save(tokenVerification);
            responseData.success();
        } catch (Exception e) {
            responseData.failed(e.getMessage());
            e.printStackTrace();
            logService.saveLog(requestData, responseData, StatusCode.INTERNAL_SERVER_ERROR, this.getClass().getName(), RESEND_EMAIL_VERIFICATION);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseData);
        }
        logService.saveLog(requestData, responseData, StatusCode.OK, this.getClass().getName(), RESEND_EMAIL_VERIFICATION);
        return ResponseEntity.status(HttpStatus.OK).body(responseData);
    }

    public ResponseEntity<ResponseHandling> deleteUser(String idUser, String token) {
        ResponseHandling responseData = new ResponseHandling();
        RequestData<Map<String, Object>> requestData = new RequestData<>();
        Map<String, Object> request = new HashMap<>();
        request.put("id", idUser);
        requestData.setPayload(request);

        try {
            User userAccess = userValidation.getUserFromToken(token);
            if(userAccess == null){
                responseData.failed("User Access Not Found");
                logService.saveLog(requestData, responseData, StatusCode.NOT_FOUND, this.getClass().getName(),
                        DELETE_USER);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseData);
            }
            if(!userValidation.isAdmin(token)){
                responseData.failed("Access denied");
                logService.saveLog(requestData, responseData, StatusCode.FORBIDDEN, this.getClass().getName(),
                        DELETE_USER);
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(responseData);
            }

            if (idUser.equals(null) || idUser.equals("")) {
                responseData.failed("ID is required");
                logService.saveLog(requestData, responseData, StatusCode.BAD_REQUEST, this.getClass().getName(),
                        DELETE_USER);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseData);
            }

            User user = userRepository.findById(idUser).orElse(null);
            if (user == null) {
                responseData.failed("User not found");
                logService.saveLog(requestData, responseData, StatusCode.NOT_FOUND, this.getClass().getName(),
                        DELETE_USER);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseData);
            }

            user.setAccountInactive(true);
            userRepository.save(user);
            responseData.success("User has been deleted");

        } catch (Exception e) {
            responseData.failed(e.getMessage());
            e.printStackTrace();
            logService.saveLog(requestData, responseData, StatusCode.INTERNAL_SERVER_ERROR, this.getClass().getName(),
                    DELETE_USER);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseData);
        }
        logService.saveLog(requestData, responseData, StatusCode.OK, this.getClass().getName(), DELETE_USER);
        return ResponseEntity.ok(responseData);
    }

    public ResponseEntity<ResponseHandling<Integer>> countMitra(String token){
        ResponseHandling responseData= new ResponseHandling();
        try {
            final Claims claim = jwtUtility.getAllClaimsFromToken(token);
            claim.get("role");
            if(!(claim.get("role").equals(Roles.SUPER_ADMIN.toString()) || claim.get("role").equals(Roles.ADMIN.toString()))){
                responseData.failed("Access denied");
                logService.saveLog(new RequestData<>(), responseData, StatusCode.FORBIDDEN, this.getClass().getName(),
                        COUNT_MITRA);
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(responseData);
            }
            long count = userRepository.countUserByAuthPrincipal(Roles.MITRA.toString());
            responseData.setPayload(count);
            responseData.success();


        }catch (Exception e){
            e.printStackTrace();
            responseData.failed(e.getMessage());
            logService.saveLog(new RequestData<>(), responseData, StatusCode.INTERNAL_SERVER_ERROR, this.getClass().getName(),
                    COUNT_MITRA);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseData);
        }
        logService.saveLog(new RequestData<>(), responseData, StatusCode.OK, this.getClass().getName(),
                COUNT_MITRA);
        return ResponseEntity.ok(responseData);
    }

    public ResponseEntity<ResponseHandling> updateStatusMitra(String token, UserAction action, String idUser){
        ResponseHandling responseData = new ResponseHandling<>();
        RequestData<Map<String, Object>> requestData = new RequestData<>();
        requestData.setPayload(logService.setValueRequest("idUser",idUser));

        try {
            User user = userValidation.getUserFromToken(token);
            if(user == null){
                responseData.failed("User Not Found");
                logService.saveLog(requestData, responseData, StatusCode.NOT_FOUND, this.getClass().getName(),
                        action.toString());
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseData);
            }
            if(!userValidation.isAdmin(token)){
                responseData.failed("Access denied");
                logService.saveLog(requestData, responseData, StatusCode.FORBIDDEN, this.getClass().getName(),
                        action.toString());
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(responseData);
            }

            User userData = userRepository.findById(idUser).orElse(null);
            if(userData == null){
                responseData.failed("User not found");
                logService.saveLog(requestData, responseData, StatusCode.NOT_FOUND, this.getClass().getName(),
                        action.toString());
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseData);
            }

            switch (action){
                case LOCK:
                    userData.setAccountLocked(true);
                    break;
                case UNLOCK:
                    userData.setAccountLocked(false);
                    break;
                case ACTIVATE:
                    userData.setAccountInactive(false);
                    break;
                case INACTIVATE:
                    userData.setAccountInactive(true);
                    break;

            }
            userData.setUpdateBy(user.getId());
            userData.setUpdateDate(new Date());
            userRepository.save(userData);

            responseData.success();

        }catch (Exception e){
            e.printStackTrace();
            responseData.failed("Internal Server Error");
            logService.saveLog(requestData, responseData, StatusCode.INTERNAL_SERVER_ERROR, this.getClass().getName(),
                    action.toString());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseData);
        }
        logService.saveLog(requestData, responseData, StatusCode.OK, this.getClass().getName(),
                action.toString());
        return ResponseEntity.ok(responseData);

    }
}
