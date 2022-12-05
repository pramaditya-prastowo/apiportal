package mii.bsi.apiportal.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import mii.bsi.apiportal.domain.User;
import mii.bsi.apiportal.service.UserService;
import mii.bsi.apiportal.utils.ResponseHandling;

@RestController
@RequestMapping("/api/v1.0/user")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping(value = "/create")
    public ResponseHandling<User> create(@Valid @RequestBody User user, Errors errors) {
        return userService.create(user, errors);
    }

    @GetMapping(value = "/all")
    public ResponseHandling<Iterable<User>> getAll(User user) {
        return userService.getAll();
    }

    @GetMapping
    public ResponseEntity<ResponseHandling<User>> getById(@RequestParam String id) {
        return userService.getById(id);
    }

    @PostMapping(value = "/update")
    public ResponseHandling<User> update(@RequestBody User user) {
        return userService.update(user);
    }

    @PostMapping(value = "/register")
    public ResponseEntity<ResponseHandling<User>> register(@Valid @RequestBody User user, Errors errors) {
        System.out.println(user);
        return userService.register(user, errors);
    }

    @GetMapping(value = "/verification")
    public ResponseEntity<ResponseHandling> confirmEmailVerification(@RequestParam String token) {
        return userService.confirmEmailVerification(token);
    }

    @PostMapping(value = "/verification")
    public ResponseEntity<ResponseHandling> resendEmailVerification(@RequestParam String email){
        return userService.resendEmailVerification(email);
    }

    @PostMapping(value = "/delete/{id}")
    public ResponseEntity<ResponseHandling> deleteUser(@PathVariable("id") String idUser) {
        return userService.deleteUser(idUser);
    }

}
