package murex.dev.mxem.Authorization.model;


import lombok.*;

import java.util.Set;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class RolesPermissions {
    private Set<String> roles;
    private Set<String> permissions;
}
