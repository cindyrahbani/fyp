package murex.dev.mxem.Scheduler.controller;


import lombok.extern.slf4j.Slf4j;

import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.util.List;

@CrossOrigin(origins = "*", allowedHeaders = "*", maxAge = 3600)
@Slf4j
@RestController
@Validated
@RefreshScope
@RequestMapping(value = "/requests")
public class SchedulerController {

//    @Autowired
//    RequestService requestService;
//
//    @GetMapping
//    public ResponseEntity<List<Request>> getAllEnvironments() {
//        log.info("Calling get all environments");
//        List<Request> reqs = requestService.findAll();
//        log.info(reqs.toString());
//        return ResponseEntity.ok(reqs);
//    }
//
//    @GetMapping("/{id}")
//    public ResponseEntity<List<Request>> getEnvironmentDetails(@PathVariable String id) {
//        List<Request>req = requestService.findByName(id);
//        return ResponseEntity.ok(req);
//    }
//
//    @PostMapping
//    public ResponseEntity<Void> addEnvironment(@Valid @RequestBody Request request, UriComponentsBuilder builder){
//        requestService.add(request);
//        HttpHeaders headers = new HttpHeaders();
//        headers.setLocation(builder.path("/request/{id}").buildAndExpand(request.getId()).toUri());
//        return new ResponseEntity<Void>(headers, HttpStatus.CREATED);
//    }


}
