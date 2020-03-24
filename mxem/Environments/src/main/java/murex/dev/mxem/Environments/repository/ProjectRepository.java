package murex.dev.mxem.Environments.repository;

import murex.dev.mxem.Environments.model.Project;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ProjectRepository extends MongoRepository<Project, String> {
    List<Project> findByName(String name);
}