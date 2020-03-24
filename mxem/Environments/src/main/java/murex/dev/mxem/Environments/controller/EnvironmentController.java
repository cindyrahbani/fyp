package murex.dev.mxem.Environments.controller;

import lombok.extern.slf4j.Slf4j;
import murex.dev.mxem.Environments.exception.OperationNotSupportedException;
import murex.dev.mxem.Environments.model.Environment;
import murex.dev.mxem.Environments.model.Event;
import murex.dev.mxem.Environments.model.Operation;
import murex.dev.mxem.Environments.model.Request;
import murex.dev.mxem.Environments.service.AuthorizationService;
import murex.dev.mxem.Environments.service.EnvironmentService;
import murex.dev.mxem.Environments.service.RabbitMQService;
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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@CrossOrigin(origins = "*", allowedHeaders = "*", maxAge = 3600)
@Slf4j
@RestController
@Validated
@RefreshScope
@RequestMapping(value = "/environments")
public class EnvironmentController {
    @Autowired
    EnvironmentService environmentService;

    @Autowired
    AuthorizationService authorizationService;

    @Autowired
    RabbitMQService rabbitMQService;

    Boolean isOperationValid(String operationName){
        for(Operation op : Operation.values() ){
            if(op.name().equals(operationName)){
                return true;
            }
        }
        return false;
    }

    @PostMapping(value = "{name}/operations/{operationName}")
    public ResponseEntity<Request> producer(@PathVariable String name,@PathVariable String operationName) {
     try{
        if(!isOperationValid(operationName)){
            throw new OperationNotSupportedException();
        }
        Date date = Calendar.getInstance().getTime();
        DateFormat dateFormat = new SimpleDateFormat("yyyy_MM_dd_hh_mm_ss");
        DateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd");
        String strDate = dateFormat.format(date);

        Request req = new Request();
        req.setEnvironment(environmentService.findByName(name).get(0));
        req.setName("request_"+name+"_"+strDate);
        req.setType("Environment");
        req.setOperation(operationName);
        req.setStatus("Queued");
        ArrayList events = new ArrayList<Event>();
        req.setEvents(events);

        rabbitMQService.send(req);

        return ResponseEntity.ok(req);}
     catch(OperationNotSupportedException e){
         throw new ResponseStatusException(HttpStatus.METHOD_NOT_ALLOWED, e.getMessage());
     }
    }


    @GetMapping
    public ResponseEntity<List<Environment>> getAllEnvironments() {
        log.info("Calling get all environments");
        List<Environment> envs = environmentService.findAll();
        log.info(envs.toString());
        return ResponseEntity.ok(envs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<List<Environment>> getEnvironmentDetails(@PathVariable String id) {
        List<Environment> env = environmentService.findByName(id);
        return ResponseEntity.ok(env);
    }

    @PostMapping
    public ResponseEntity<Void> addEnvironment(@Valid @RequestBody Environment environment, UriComponentsBuilder builder,@RequestHeader("Authorization") String token){
        environment.updateOnCreation(authorizationService.getUsernameFromToken(token));
        environmentService.add(environment);
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(builder.path("/environments/{id}").buildAndExpand(environment.getId()).toUri());
        return new ResponseEntity<Void>(headers,HttpStatus.CREATED);
    }


    @GetMapping("/{name}/tags")
    public ResponseEntity<List<String>> getTagsForEnvironments(@PathVariable String name) {
            log.info("Getting the tags for the environment "+name);
            Environment env = environmentService.findByName(name).get(0);
            return ResponseEntity.ok(env.getTags());
    }
//
//    @DeleteMapping
//    public ResponseEntity deleteAllEnvironments(){
//        environmentService.deleteAllEnvironments();
//        log.info("Delete all environments");
//        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
//    }
//
////Delete the environment by id
////    @DeleteMapping(path="/{envId}")
////    public ResponseEntity deleteEnvironmentById(@PathVariable Long envId)  {
////        try{
////            environmentService.deleteEnvironmentById(envId);
////            log.info("Delete environment of ID: {"+envId+"}");
////            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
////        }
////        catch (EnvironmentNotFoundException e) {
////            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
////        }
////    }
//
//    @DeleteMapping(path="/{envId}/users")
//    public ResponseEntity deleteUsersForEnvironment(@PathVariable Long envId)  {
//        try{
//            environmentService.deleteUsersForEnvironment(envId);
//            log.info("Delete users' environment of ID: {"+envId+"}");
//            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
//        }
//        catch (EnvironmentNotFoundException e) {
//            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
//        }
//    }
//    @DeleteMapping(path="/{envId}/tags")
//    public ResponseEntity deleteTagsForEnvironment(@PathVariable Long envId)  {
//        try{
//            environmentService.deleteTagsForEnvironment(envId);
//            log.info("Delete tags' environment of ID: {"+envId+"}");
//            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
//        }
//        catch (EnvironmentNotFoundException e) {
//            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
//        }
//    }
//    @DeleteMapping(path="/{envId}/projects")
//    public ResponseEntity deleteProjectForEnvironment(@PathVariable Long envId)  {
//        try{
//            environmentService.deleteProjectForEnvironment(envId);
//            log.info("Delete projects' environment of ID: {"+envId+"}");
//            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
//        }
//        catch (EnvironmentNotFoundException e) {
//            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
//        }
//    }
//



//    @PutMapping(path="/{environmentId}")
//    public ResponseEntity<Environment> updateEnvironment(@PathVariable String environmentId, @RequestBody Environment environment) throws EnvironmentNotFoundException {
//        try {
//            environmentService.updateEnvironment(Long.parseLong(environmentId), environment);
//            return ResponseEntity.ok(environment);
//        }catch(EnvironmentNotFoundException e){
//            throw new ResponseStatusException(HttpStatus.NOT_FOUND,e.getMessage());
//        }
//    }
//
//    @PatchMapping(path="/{environmentId}")
//    public ResponseEntity<Environment>updateNameOfEnvironment(@PathVariable Long environmentId, @RequestBody String name) throws EnvironmentNotFoundException{
//        try {
//            environmentService.updateNameofEnvironment(environmentId,name);  //it saved the new name in the database
//            Optional<Environment>env=environmentService.findEnvironmentById(environmentId);
//            return ResponseEntity.ok(env.get());
//        }catch(EnvironmentNotFoundException e){
//            throw new ResponseStatusException(HttpStatus.NOT_FOUND,e.getMessage());
//        }
//    }
/////////////////////////////////
//
//    //Change the env_deleted of the environment to true
//    @DeleteMapping(path="/{envId}")
//    public ResponseEntity<Environment> updateDeleteOfEnv(@PathVariable Long envId) throws EnvironmentNotFoundException {
//        try {
//            environmentService.updateDeleteOfEnvironment(envId,true);  //it saved the new name in the database
//            Optional<Environment>env=environmentService.findEnvironmentById(envId);
//            return ResponseEntity.ok(env.get());
//        }catch(EnvironmentNotFoundException e){
//            throw new ResponseStatusException(HttpStatus.NOT_FOUND,e.getMessage());
//        }
//    }
//
//    ///////////////////////////////
//
//
//    @PostMapping("/{Environment_id}/users/{user_id}")
//    public ResponseEntity<Set<User>> addUserForEnvironment(@PathVariable String Environment_id, @PathVariable String user_id) throws EnvironmentNotFoundException, UserNotFoundException {
//        try {
//            environmentService.addUserForEnvironment(Long.parseLong(Environment_id), Long.parseLong(user_id));
//            log.info("Adding a user for the environment # "+Environment_id);
//            return getUsersForEnvironments(Long.parseLong(Environment_id));
//        }
//        catch(EnvironmentNotFoundException e){
//            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
//        }
//        catch(UserNotFoundException e1){
//            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e1.getMessage());
//        }
//    }
//    @PostMapping("/{Environment_id}/tags/{tag_id}")
//    public ResponseEntity<Set<Tag>> addTagForEnvironment(@PathVariable String Environment_id, @PathVariable String tag_id) throws EnvironmentNotFoundException, TagNotFoundException {
//        try {
//            environmentService.addTagForEnvironment(Long.parseLong(Environment_id), Long.parseLong(tag_id));
//            log.info("Adding a tag for the environment # "+Environment_id);
//            return getTagsForEnvironments(Long.parseLong(Environment_id));
//        }
//        catch(EnvironmentNotFoundException e){
//            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
//        }
//        catch(TagNotFoundException e1){
//            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e1.getMessage());
//        }
//    }
}