package murex.dev.mxem.Environments.model;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Event {
    public String eventid;
    public String title;
    public String description;
    public String date;
}
