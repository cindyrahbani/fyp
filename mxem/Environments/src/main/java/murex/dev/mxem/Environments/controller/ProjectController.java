package murex.dev.mxem.Environments.controller;

import lombok.extern.slf4j.Slf4j;
import murex.dev.mxem.Environments.model.Project;
import murex.dev.mxem.Environments.service.AuthorizationService;
import murex.dev.mxem.Environments.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping(value = "/projects")
public class ProjectController {

    @Autowired
    ProjectService projectService;

    @Autowired
    AuthorizationService authorizationService;

    @GetMapping
    public ResponseEntity<List<Project>> getAllProjects() {
        log.info("Calling get all environments");
        List<Project> projs = projectService.findAll();
        log.info(projs.toString());
        return ResponseEntity.ok(projs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<List<Project>> getProjectDetails(@PathVariable String id) {
        List<Project> proj  = projectService.findByName(id);
        return ResponseEntity.ok(proj);
    }

    @PostMapping
    public ResponseEntity<Void> addProject(@Valid @RequestBody Project project, UriComponentsBuilder builder,@RequestHeader("Authorization") String token){
        project.updateOnCreation(authorizationService.getUsernameFromToken(token));
        projectService.add(project);
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(builder.path("/project/{id}").buildAndExpand(project.getId()).toUri());
        return new ResponseEntity<Void>(headers, HttpStatus.CREATED);
    }
}
