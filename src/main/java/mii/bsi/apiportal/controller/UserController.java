package mii.bsi.apiportal.controller;

import javax.validation.Valid;

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

    @PostMapping(value = "/admin")
    public ResponseEntity<ResponseHandling<User>> registerByAdmin(
            @Valid @RequestBody User user,
            @RequestHeader(HttpHeaders.AUTHORIZATION) String token,
            Errors errors) {
        return userService.registerByAdmin(user,token.substring(7), errors);
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
    public ResponseHandling<User> update(@RequestBody User user) {
        return userService.update(user);
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

}
