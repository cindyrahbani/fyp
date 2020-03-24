package murex.dev.mxem.Scheduler.model;


import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Host {

    public String name;
    public Credentials credentials;
}
