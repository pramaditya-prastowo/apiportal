package mii.bsi.apiportal.controller;

import javax.validation.Valid;

import mii.bsi.apiportal.constant.UserAction;
import mii.bsi.apiportal.dto.ChangePasswordRequestDTO;
import mii.bsi.apiportal.dto.UpdatePasswordRequestDTO;
import mii.bsi.apiportal.dto.UserResponseDTO;
import mii.bsi.apiportal.dto.VerificationEmailRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import mii.bsi.apiportal.domain.User;
import mii.bsi.apiportal.service.UserService;
import mii.bsi.apiportal.utils.ResponseHandling;

import java.util.List;

@RestController
@RequestMapping("/api/v1.0/user")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity<ResponseHandling<User>> registerByMitra(@Valid @RequestBody User user, Errors errors) {
        return userService.register(user, errors);
    }

    @PostMapping(value = "/by-admin")
    public ResponseEntity<ResponseHandling<User>> registerByAdmin(
            @Valid @RequestBody User user,
            @RequestHeader(HttpHeaders.AUTHORIZATION) String token,
            Errors errors) {
        return userService.registerByAdmin(user,token.substring(7), errors);
    }

    @PostMapping("/change-password")
    public ResponseEntity<ResponseHandling> changePassword(@Valid
            @RequestHeader(HttpHeaders.AUTHORIZATION) String token,
            @RequestBody ChangePasswordRequestDTO requestDTO, Errors errors
            ){
        return userService.changePassword(token.substring(7), requestDTO, errors);
    }

    @GetMapping
    public ResponseEntity<ResponseHandling<List<UserResponseDTO>>> getAll(@RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        return userService.getAll(token.substring(7));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseHandling<User>> getById(@PathVariable("id") String id) {
        return userService.getById(id);
    }

    @PatchMapping
    public ResponseEntity<ResponseHandling> update(@RequestBody User user,@RequestHeader(HttpHeaders.AUTHORIZATION) String token ) {
        return userService.update(user, token.substring(7));
    }

    @PatchMapping("/by-admin")
    public ResponseEntity<ResponseHandling> updateByAdmin(@RequestBody User user, @RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        return userService.updateByAdmin(user, token.substring(7));
    }

    @PatchMapping(value = "/verification")
    public ResponseEntity<ResponseHandling> confirmEmailVerification(@Valid @RequestBody VerificationEmailRequest request, Errors errors) {
        return userService.confirmEmailVerification(request, errors);
    }

    @PostMapping(value = "/verification")
    public ResponseEntity<ResponseHandling> resendEmailVerification(@RequestParam String email){
        return userService.resendEmailVerification(email);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseHandling> deleteUser(@RequestHeader(HttpHeaders.AUTHORIZATION) String token,
                                                       @PathVariable("id") String idUser) {
        return userService.deleteUser(idUser, token.substring(7));
    }

    @GetMapping("/count")
    public ResponseEntity<ResponseHandling<Integer>> getCountMitra(@RequestHeader(HttpHeaders.AUTHORIZATION) String token){
        return userService.countMitra(token.substring(7));
    }

    @PostMapping("/unlock")
    public ResponseEntity<ResponseHandling> unlockUser(@RequestHeader(HttpHeaders.AUTHORIZATION) String token,@RequestParam String idUser ){
        return userService.updateStatusMitra(token.substring(7), UserAction.UNLOCK, idUser);
    }

    @PostMapping("/lock")
    public ResponseEntity<ResponseHandling> lockUser(@RequestHeader(HttpHeaders.AUTHORIZATION) String token,@RequestParam String idUser ){
        return userService.updateStatusMitra(token.substring(7), UserAction.LOCK, idUser);
    }

    @PostMapping("/activate")
    public ResponseEntity<ResponseHandling> activateUser(@RequestHeader(HttpHeaders.AUTHORIZATION) String token,@RequestParam String idUser ){
        return userService.updateStatusMitra(token.substring(7), UserAction.ACTIVATE, idUser);
    }
    @PostMapping("/inactivate")
    public ResponseEntity<ResponseHandling> inactivateUser(@RequestHeader(HttpHeaders.AUTHORIZATION) String token,@RequestParam String idUser ){
        return userService.updateStatusMitra(token.substring(7), UserAction.INACTIVATE, idUser);
    }

}
