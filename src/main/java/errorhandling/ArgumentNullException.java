
package errorhandling;


public class ArgumentNullException extends Exception{ //Runtime?
    int errorCode;
    
    public ArgumentNullException(String string, int errorCode) {
        super(string);
        this. errorCode = errorCode;
    }

    public int getErrorCode() {
        return errorCode;
    }
    
    
    
}
