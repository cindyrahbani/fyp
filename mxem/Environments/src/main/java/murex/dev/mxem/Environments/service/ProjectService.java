package murex.dev.mxem.Environments.service;

import murex.dev.mxem.Environments.model.Project;
import murex.dev.mxem.Environments.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class ProjectService implements IProjectService{
    @Autowired
    ProjectRepository projectRepository;

    public Project add(Project project){
        projectRepository.save(project);
        return project;
    }

    public Project findById(String id){
        Project proj = projectRepository.findById(id).get();
        return proj;
    }

    public List<Project> findByName(String id){
        List<Project> projs = projectRepository.findByName(id);
        return projs;
    }


    public List<Project> findAll(){
        return projectRepository.findAll();
    }

}
