package murex.dev.mxem.Environments.model;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Database {
    public String name;
    public String type;
    public DbServer dbserver;
}
