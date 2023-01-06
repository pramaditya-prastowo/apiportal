package mii.bsi.apiportal.controller;

import mii.bsi.apiportal.domain.Groups;
import mii.bsi.apiportal.domain.Menu;
import mii.bsi.apiportal.dto.AuthGuardPageRequestDTO;
import mii.bsi.apiportal.service.GroupsService;
import mii.bsi.apiportal.utils.ResponseHandling;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1.0/groups")
public class GroupsController {

    @Autowired
    private GroupsService groupsService;

    @GetMapping
    public ResponseEntity<ResponseHandling<List<Groups>>> getAll(@RequestHeader(HttpHeaders.AUTHORIZATION) String token){
        return groupsService.getAll(token.substring(7));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseHandling<Groups>> getById(@PathVariable("id") Long id,
                                                            @RequestHeader(HttpHeaders.AUTHORIZATION) String token){
        return groupsService.getById(id,token.substring(7));
    }

    @PostMapping
    public ResponseEntity<ResponseHandling> create(@Valid @RequestBody Groups groups, Errors errors,
                                                   @RequestHeader(HttpHeaders.AUTHORIZATION) String token){
        return groupsService.createGroups(groups, errors, token.substring(7));
    }

    @PostMapping("/validate")
    public ResponseEntity<ResponseHandling<Boolean>> validatePage(@Valid @RequestBody AuthGuardPageRequestDTO authGuard,
                                                                  @RequestHeader(HttpHeaders.AUTHORIZATION) String token,
                                                                  Errors errors){
        return groupsService.validatePage(token.substring(7), authGuard, errors);
    }

    @GetMapping("/menu")
    public ResponseEntity<ResponseHandling<List<Menu>>> getMenu(@RequestHeader(HttpHeaders.AUTHORIZATION) String token, @RequestParam Long groupId){
        return groupsService.getMenuByGroupId(token.substring(7), groupId);
    }


}
