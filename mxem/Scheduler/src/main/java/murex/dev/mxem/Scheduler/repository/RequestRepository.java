package murex.dev.mxem.Scheduler.repository;

        import murex.dev.mxem.Scheduler.model.Request;
        import org.springframework.data.mongodb.repository.MongoRepository;
        import org.springframework.stereotype.Repository;

        import java.util.List;

@Repository
public interface RequestRepository extends MongoRepository<Request, String> {
    List<Request> findByOperationId(String name);
}
