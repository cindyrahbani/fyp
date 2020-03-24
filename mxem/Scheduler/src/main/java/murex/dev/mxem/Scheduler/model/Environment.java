package murex.dev.mxem.Scheduler.model;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Id;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Environment {

    @Id
    public String id;
    public String name;
    public String path;
    public String version;
    public String owner;
    public Long projectId;
    public Host host;
    public List<String> tags;
    public List<Database> databases;
    public Boolean isDeleted;
    public String createdOn;
    public String createdBy;
    public String modifiedOn;
    public String modifiedBy;
}
