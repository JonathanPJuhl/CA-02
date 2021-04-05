
package errorhandling;

public class IllegalPhoneException extends Exception{
    int errorCode;

    public IllegalPhoneException(int errorCode, String string) {
        super(string);
        this.errorCode = errorCode;
    }

    public int getErrorCode() {
        return errorCode;
    }
    
    
}
