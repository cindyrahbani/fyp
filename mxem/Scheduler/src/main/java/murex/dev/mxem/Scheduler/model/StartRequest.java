package murex.dev.mxem.Scheduler.model;


import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class StartRequest extends Parameters{
    String applicative_hostname;
    String applicative_password;
    String start_services_script;
    String applicative_username;
    String app_dir;
}
