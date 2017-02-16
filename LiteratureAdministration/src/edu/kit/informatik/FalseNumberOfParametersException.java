package edu.kit.informatik;

@SuppressWarnings("serial")
public class FalseNumberOfParametersException extends IllegalArgumentException {
    
    /**
     * Creates a new FalseNumberOfParametersException with the provided output message.
     * 
     * @param message the message
     */
    public FalseNumberOfParametersException(String message) {
        super(message);
    }
    /**
     * Creates a new FalseNumberOfParametersException with a default output message.
     */
    public FalseNumberOfParametersException() {
        super("wrong number of parameters.");
    }
}
