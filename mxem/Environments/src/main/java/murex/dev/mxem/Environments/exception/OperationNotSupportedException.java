package murex.dev.mxem.Environments.exception;

public class OperationNotSupportedException extends Exception {
    private static final long serialVersionUID = 1L;

    public OperationNotSupportedException()
    {
        super("Error : This operation is not supported ");
    }
}