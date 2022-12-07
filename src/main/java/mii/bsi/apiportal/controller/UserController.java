package mii.bsi.apiportal.controller;

import javax.validation.Valid;

import mii.bsi.apiportal.dto.UserResponseDTO;
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
    public ResponseEntity<ResponseHandling<User>> create(@Valid @RequestBody User user, Errors errors) {
        return userService.register(user, errors);
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

    @GetMapping(value = "/verification")
    public ResponseEntity<ResponseHandling> confirmEmailVerification(@RequestParam String token) {
        return userService.confirmEmailVerification(token);
    }

    @PostMapping(value = "/verification")
    public ResponseEntity<ResponseHandling> resendEmailVerification(@RequestParam String email){
        return userService.resendEmailVerification(email);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseHandling> deleteUser(@PathVariable("id") String idUser) {
        return userService.deleteUser(idUser);
    }

}
