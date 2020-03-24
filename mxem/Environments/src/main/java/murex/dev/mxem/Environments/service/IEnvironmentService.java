package murex.dev.mxem.Environments.service;

import murex.dev.mxem.Environments.model.Environment;

import java.util.List;

public interface IEnvironmentService {

    public Environment add(Environment environment);
    public Environment findById(String id);
    public List<Environment> findByName(String id);
    public List<Environment> findAll();

}
