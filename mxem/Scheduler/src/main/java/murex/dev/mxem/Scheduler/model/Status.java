package murex.dev.mxem.Scheduler.model;


import lombok.*;

import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Id;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Document(collection = "statuses")
public class Status {
    @Id
            String id;
    String operationId;
    String createdAt;
    String createdBy;
    String commandName;
    String path;
    Parameters params;
    String version;
    String status;
}
