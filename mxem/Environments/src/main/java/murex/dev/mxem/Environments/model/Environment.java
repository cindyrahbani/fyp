package murex.dev.mxem.Environments.model;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Id;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Document(collection="environments")
public class Environment extends Structure {

    @Id
    public String id;
    public String name;
    public String path;
    public String version;
    public String owner;
    public Long projectId;
    public Host host;
    public List<String> tags=new ArrayList<String>();
    public List<Database> databases=new ArrayList<Database>();
}
