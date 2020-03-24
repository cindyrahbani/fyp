package murex.dev.mxem.Environments.repository;

import murex.dev.mxem.Environments.model.Environment;

import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;


public interface EnvironmentRepository extends MongoRepository<Environment, String> {
    List<Environment> findByName(String name);
    List<Environment> findByProjectId(Long id);
}