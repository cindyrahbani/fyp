package murex.dev.mxem.Environments.model;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Id;


@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Document(collection="projects")
public class Project extends Structure {
    @Id
    public String id;
    public String name;
    public String owner;
}
