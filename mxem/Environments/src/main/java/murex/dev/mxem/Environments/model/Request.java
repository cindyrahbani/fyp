package murex.dev.mxem.Environments.model;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Id;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Document(collection="requests")
public class Request {
    @Id
    public String id;
    public String name;
    public String type;
    public Environment environment;
    public String operation;
    public String status;
    public List<Event> events;
}
