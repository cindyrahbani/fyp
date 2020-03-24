package murex.dev.mxem.Scheduler.repository;

import murex.dev.mxem.Scheduler.model.Status;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StatusRepository extends MongoRepository<Status, String> {
}
