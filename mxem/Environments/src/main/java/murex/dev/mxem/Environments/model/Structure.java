package murex.dev.mxem.Environments.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;


@Getter
@Setter
public class Structure {
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
