package murex.dev.mxem.Environments.model;


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
