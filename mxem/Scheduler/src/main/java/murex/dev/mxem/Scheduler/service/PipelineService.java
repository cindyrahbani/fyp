package murex.dev.mxem.Scheduler.service;

import murex.dev.mxem.Scheduler.model.*;
import org.apache.commons.codec.binary.Base64;
import org.apache.tomcat.util.json.JSONParser;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;

@Service
public class PipelineService {


    private static final String username="admin";
    private static final String password="admin";
    private static final String pipelineURI="http://mxpipeline-blue:8080/";
    private static final String commandsPath="commands/";

    public String getBasicAuth(){
        String auth = username + ":" + password;
        byte[] encodedAuth = Base64.encodeBase64(
                auth.getBytes(Charset.forName("US-ASCII")) );
        String authHeader = "Basic " + new String( encodedAuth );
        return authHeader;
    }

    public ResponseEntity<String> getCommands(){
        String uri=pipelineURI+commandsPath;
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", getBasicAuth());
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);

        ResponseEntity<String> result = restTemplate.exchange(uri, HttpMethod.GET, entity, String.class);

        return result;
    }

    public Status sendCommandRequest(Request request){
        String uri=pipelineURI+commandsPath+request.getOperation()+"/operations";
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();

        headers.set("Authorization", getBasicAuth());
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        StartRequest startRequest=new StartRequest();
        startRequest.setApp_dir(request.getEnvironment().getPath());
        startRequest.setApplicative_hostname(request.getEnvironment().getHost().getName());
        startRequest.setApplicative_password(request.getEnvironment().getHost().getCredentials().getPassword());
        startRequest.setApplicative_username(request.getEnvironment().getHost().getCredentials().getUsername());
        startRequest.setApplicative_username("script.sh");
        HttpEntity<StartRequest> entity = new HttpEntity<StartRequest>(startRequest, headers);
        ResponseEntity<Status> result = restTemplate.exchange(uri, HttpMethod.POST, entity, Status.class);


        return( result.getBody());
    }

    public Events updateEvents(String commandName, String operationId){
        String uri=pipelineURI+commandsPath+commandName+"/operations/"+operationId+"/events";
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", getBasicAuth());
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        HttpEntity<String> entity = new HttpEntity<String>("message", headers);
        ResponseEntity<Events> result = restTemplate.exchange(uri, HttpMethod.GET, entity, Events.class);
        return result.getBody();

    }

    public String getNewStatus(String commandName, String operationId){
        String uri=pipelineURI+commandsPath+commandName+"/operations/"+operationId;
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", getBasicAuth());
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        HttpEntity<String> entity = new HttpEntity<String>("message", headers);
        ResponseEntity<Status> result = restTemplate.exchange(uri, HttpMethod.GET, entity, Status.class);
       // System.out.println(result.getBody());
        return result.getBody().getStatus();
    }
}
