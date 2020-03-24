package murex.dev.mxem.Authorization.exception;

public class TokenExpiredException extends Exception {
    private static final long serialVersionUID = 1L;

    public TokenExpiredException()
    {
        super("Error : Token Expired");
    }
}
