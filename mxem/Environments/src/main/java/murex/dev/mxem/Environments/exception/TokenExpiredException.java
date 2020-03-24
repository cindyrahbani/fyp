package murex.dev.mxem.Environments.exception;

public class TokenExpiredException extends Exception {
    private static final long serialVersionUID = 1L;

    public TokenExpiredException()
    {
        super("Error : Token Expired");
    }
}
