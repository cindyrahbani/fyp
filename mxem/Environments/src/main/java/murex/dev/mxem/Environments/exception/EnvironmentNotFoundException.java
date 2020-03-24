package murex.dev.mxem.Environments.exception;

public class EnvironmentNotFoundException extends Exception {
    private static final long serialVersionUID = 1L;

    public EnvironmentNotFoundException()
    {
        super("Error : Environment not found");
    }
}