package murex.dev.mxem.Authorization.repository;


import murex.dev.mxem.Authorization.model.Permission;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PermissionRepository extends CrudRepository<Permission, Long> {
}
