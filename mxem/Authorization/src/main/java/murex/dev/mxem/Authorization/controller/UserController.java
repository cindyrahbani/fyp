package murex.dev.mxem.Authorization.controller;

import lombok.extern.slf4j.Slf4j;
import murex.dev.mxem.Authorization.exception.RoleNotFoundException;
import murex.dev.mxem.Authorization.exception.UserNotFoundException;
import murex.dev.mxem.Authorization.model.Role;
import murex.dev.mxem.Authorization.model.User;
import murex.dev.mxem.Authorization.service.AuthorizationService;
import murex.dev.mxem.Authorization.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@Validated
@RefreshScope
@Slf4j
@RequestMapping(value = "/users")
public class UserController {
    @Autowired
    UserService userService;

    @Autowired
    AuthorizationService authorizationService;


    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.findAllUsers();
        return ResponseEntity.ok(users);
    }
//all methods should return a response entity
    @GetMapping("/{name}")
    public ResponseEntity<User> getUserDetails(@PathVariable String name) {
        try {
            User user = userService.findUserByName(name);
            return ResponseEntity.ok(user);
        } catch (UserNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @GetMapping("/{id}/roles")
    public ResponseEntity<Set<Role>> getRolesForUser(@PathVariable String id) {
        try{
        return ResponseEntity.ok(userService.findRolesForUser(id));}
        catch(UserNotFoundException e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }


    @DeleteMapping
    public ResponseEntity deleteAllUsers() {
        userService.deleteAllUsers();
        log.info("Delete all users");
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @DeleteMapping(path = "/{userId}")
    public ResponseEntity deleteUserById(@PathVariable Long userId) {
        try {
            userService.deleteUserById(userId);
            log.info("Delete user of ID: {"+userId+"}");
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (UserNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }

    }

    @DeleteMapping(path = "/{userId}/roles")
    public ResponseEntity deleteRolesForUser(@PathVariable Long userId) {
        try {
            userService.deleteRolesForUser(userId);
            log.info("Delete user' roles of ID : {"+userId+"}");
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (UserNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }


    @PostMapping
    public ResponseEntity<Void> addUser(@Valid @RequestBody User user, UriComponentsBuilder builder, @RequestHeader("Authorization") String token){
        user.updateOnCreation(authorizationService.getUsernameFromToken(token));
        userService.addUser(user);
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(builder.path("/users/{id}").buildAndExpand(user.getId()).toUri());
        return new ResponseEntity<Void>(headers,HttpStatus.CREATED);
    }

    @PutMapping(path="/{userId}")
    public ResponseEntity<User> updateUser(@PathVariable String userId, @RequestBody User user) throws UserNotFoundException {
        try {
            userService.updateUser(Long.parseLong(userId), user);
            return ResponseEntity.ok(user);
        }catch(UserNotFoundException e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,e.getMessage());
        }
    }


    @PostMapping("/{User_id}/roles/{Role_id}")
    public ResponseEntity<Set<Role>> addRoleForUser(@PathVariable String User_id, @PathVariable String Role_id) throws UserNotFoundException, RoleNotFoundException {
        try { userService.addRoleForUser(Long.parseLong(User_id), Long.parseLong(Role_id));
            log.info("Adding a role for user # "+User_id);
            return getRolesForUser(User_id);
        }
        catch(UserNotFoundException e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
        catch(RoleNotFoundException e1){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e1.getMessage());
        }
    }


    @PatchMapping(path="/{userId}")
    public ResponseEntity<User>updateNameOfUser(@PathVariable Long userId, @RequestBody String name) throws UserNotFoundException {
        try {
            userService.updateNameofUser(userId,name);  //it saved the new name in the database
            Optional<User>user=userService.findUserById(userId);
            return ResponseEntity.ok(user.get());
        }catch(UserNotFoundException e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,e.getMessage());
        }
    }

    }

