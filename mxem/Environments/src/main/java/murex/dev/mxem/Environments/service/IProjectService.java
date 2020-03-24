package murex.dev.mxem.Environments.service;

import murex.dev.mxem.Environments.model.Project;

import java.util.List;

public interface IProjectService {

    public Project add(Project project);
    public Project findById(String id);
    public List<Project> findByName(String id);
    public List<Project> findAll();

}
