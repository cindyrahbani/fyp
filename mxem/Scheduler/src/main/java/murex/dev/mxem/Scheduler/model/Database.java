package murex.dev.mxem.Scheduler.model;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Database {
    public String dbid;
    public String name;
    public String type;
    public DbServer dbserver;
}
