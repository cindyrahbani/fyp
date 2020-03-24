package murex.dev.mxem.Scheduler.model;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Event {
    public String eventId;
    public String createdAt;
    public String log;
    public String operationId;
}
