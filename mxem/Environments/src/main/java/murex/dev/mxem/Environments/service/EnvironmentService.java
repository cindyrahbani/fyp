package murex.dev.mxem.Environments.service;

import murex.dev.mxem.Environments.model.Environment;
import murex.dev.mxem.Environments.repository.EnvironmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EnvironmentService implements IEnvironmentService {

    @Autowired
    EnvironmentRepository environmentRepository;

    @Autowired
    AuthorizationService authorizationService;

    public Environment add(Environment environment){
        environmentRepository.save(environment);
        return environment;
    }

    public Environment findById(String id){
        Environment env = environmentRepository.findById(id).get();
        return env;
    }

    public List<Environment> findByName(String id){
        List<Environment> env = environmentRepository.findByName(id);
        return env;
    }


    public List<Environment> findAll(){
        return environmentRepository.findAll();
    }
}
