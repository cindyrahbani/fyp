package murex.dev.mxem.Authorization.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
@Table(name="roles")
@javax.persistence.Entity
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotEmpty(message="name is mandatory. Please provide username")
    @Size(min=2, message="Name should be at least 2 characters")
    private String name;

    @ManyToMany
    @JsonIgnore
    @JoinTable(
            name="user_role",
            joinColumns= @JoinColumn(name="roleId", referencedColumnName="id"),
            inverseJoinColumns = @JoinColumn(name="userId", referencedColumnName="id"))
    Set<User> users;

    @ManyToMany
    @JsonIgnore
    @JoinTable(
            name="role_perm",
            joinColumns= @JoinColumn(name="roleId", referencedColumnName="id"),
            inverseJoinColumns = @JoinColumn(name="permissionId", referencedColumnName="id"))
    Set<Permission> permissions;

    public Boolean isDeleted=false;
    public Date createdOn=new Date();
    public String createdBy="user";
    public Date modifiedOn=new Date();
    public String modifiedBy="user";

    public void updateOnCreation(String username){
        this.setCreatedBy(username);
        this.setModifiedBy(username);
        this.setCreatedOn(new Date());
        this.setModifiedOn(new Date());
    }

    public void updateOnModification(String username){
        this.setModifiedBy(username);
        this.setModifiedOn(new Date());
    }
}
