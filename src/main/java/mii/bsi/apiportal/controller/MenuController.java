package mii.bsi.apiportal.controller;

import mii.bsi.apiportal.domain.Groups;
import mii.bsi.apiportal.domain.Menu;
import mii.bsi.apiportal.service.GroupsService;
import mii.bsi.apiportal.service.MenuService;
import mii.bsi.apiportal.utils.ResponseHandling;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1.0/menu")
public class MenuController {

    @Autowired
    private MenuService menuService;

    @GetMapping
    public ResponseEntity<ResponseHandling<List<Menu>>> getAll(@RequestHeader(HttpHeaders.AUTHORIZATION) String token){
        return menuService.getAll(token.substring(7));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseHandling<Menu>> getById(@PathVariable("id") Long id,
                                                            @RequestHeader(HttpHeaders.AUTHORIZATION) String token){
        return menuService.getById(id,token.substring(7));
    }

    @GetMapping("/show-approval")
    public ResponseEntity<ResponseHandling<List<Menu>>> getMenuShowApprovalMatrix(@RequestHeader(HttpHeaders.AUTHORIZATION) String token){
        return menuService.getAllShowApproval(token.substring(7));
    }
}
