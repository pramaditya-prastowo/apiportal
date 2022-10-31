package mii.bsi.apiportal.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import mii.bsi.apiportal.domain.User;
import mii.bsi.apiportal.service.UserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/api/v1.0/user")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping
    public User create(@RequestBody User user) {
        return userService.create(user);
    }

    @GetMapping(value = "/all")
    public Iterable<User> getAll(User user) {
        return userService.getAll();
    }

    @GetMapping
    public User getById(@RequestParam String id) {
        return userService.getById(id);
    }

}
