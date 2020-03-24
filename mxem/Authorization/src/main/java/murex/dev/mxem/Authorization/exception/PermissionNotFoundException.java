package murex.dev.mxem.Authorization.exception;

public class PermissionNotFoundException extends Exception {
    private static final long serialVersionUID = 1L;

    public PermissionNotFoundException(){
        super("Error : Permission Not Found in Persmisison repository");
    }
}