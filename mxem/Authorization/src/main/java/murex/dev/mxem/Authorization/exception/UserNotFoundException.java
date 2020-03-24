package murex.dev.mxem.Authorization.exception;

public class UserNotFoundException extends Exception {
    private static final long serialVersionUID = 1L;

    public UserNotFoundException()
    {
        super("Error : User Not Found in User repository");
    }
}
