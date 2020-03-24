package murex.dev.mxem.Authorization.controller;

import lombok.extern.slf4j.Slf4j;
import murex.dev.mxem.Authorization.exception.PermissionNotFoundException;
import murex.dev.mxem.Authorization.exception.RoleNotFoundException;
import murex.dev.mxem.Authorization.model.Permission;
import murex.dev.mxem.Authorization.model.Role;
import murex.dev.mxem.Authorization.service.AuthorizationService;
import murex.dev.mxem.Authorization.service.PermissionService;

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
@Slf4j
@RefreshScope
@RequestMapping(value = "/permissions")
public class PermissionController {
    @Autowired
    PermissionService permissionService;

    @Autowired
    AuthorizationService authorizationService;

    @GetMapping
    public ResponseEntity<List<Permission>> getAllPermissions(){
        List<Permission> perms=permissionService.findAllPermissions();
        return ResponseEntity.ok(perms);

    }
    @GetMapping("/{id}")
    public ResponseEntity<Permission> getPermissionDetails(@PathVariable Long id){
        try{
            Optional<Permission> perm = permissionService.findPermissionById(id);
            return ResponseEntity.ok(perm.get());
        }
        catch (PermissionNotFoundException e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @GetMapping("/{id}/roles")
    public ResponseEntity<Set<Role>> getRolesForPermissions(@PathVariable Long id) {
        try{
            log.info("Getting Roles for Permission # "+id);
            return ResponseEntity.ok(permissionService.findRolesForPermission(id));}
        catch(PermissionNotFoundException e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @DeleteMapping
    public ResponseEntity deleteAllPermissions(){
        permissionService.deleteAllPermissions();
        log.info("Delete all permissions");
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @DeleteMapping(path="/{permId}")
    public ResponseEntity deletePermissionById(@PathVariable Long permId)  {
        try{
            permissionService.deletePermissionById(permId);
            log.info("Delete permission of ID: {"+permId+"}");
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        catch (PermissionNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }

    }

    @PostMapping
    public ResponseEntity<Void> addPermission(@Valid @RequestBody Permission permission, UriComponentsBuilder builder,@RequestHeader("Authorization") String token){
        permission.updateOnCreation(authorizationService.getUsernameFromToken(token));
        permissionService.addPermission(permission);
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(builder.path("/permissions/{id}").buildAndExpand(permission.getId()).toUri());
        return new ResponseEntity<Void>(headers,HttpStatus.CREATED);
    }

    @PutMapping(path="/{permissionId}")
    public ResponseEntity<Permission> updatePermission(@PathVariable String permissionId, @RequestBody Permission permission,@RequestHeader("Authorization") String token) throws PermissionNotFoundException {
        try {
            permission.updateOnModification(authorizationService.getUsernameFromToken(token));
            permissionService.updatePermission(Long.parseLong(permissionId), permission);
            return ResponseEntity.ok(permission);
        }catch(PermissionNotFoundException e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,e.getMessage());
        }
    }

    @PatchMapping(path="/{permissionId}")
    public ResponseEntity<Permission>updateNameOfPermission(@PathVariable Long permissionId, @RequestBody String name,@RequestHeader("Authorization") String token) throws PermissionNotFoundException {
        try {

            permissionService.updateNameofPermission(permissionId,name);  //it saved the new name in the database
            Optional<Permission>permission=permissionService.findPermissionById(permissionId);
            return ResponseEntity.ok(permission.get());
        }catch(PermissionNotFoundException e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,e.getMessage());
        }
    }

    @PostMapping("/{permission_id}/roles/{role_id}")
    public ResponseEntity<Set<Role>> addRoleForPermission(@PathVariable String permission_id, @PathVariable String role_id) throws PermissionNotFoundException, RoleNotFoundException {
        try {

            permissionService.addRoleForPermission(Long.parseLong(permission_id), Long.parseLong(role_id));
            log.info("Adding a role for permission # "+permission_id);
            return getRolesForPermissions(Long.parseLong(permission_id));
        }
        catch(PermissionNotFoundException e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
        catch(RoleNotFoundException e1){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e1.getMessage());
        }
    }

}
