package errorhandling;


public class InvaldPhoneNumberException extends Exception{ //RuntimeException?
    int errorCode;

    public InvaldPhoneNumberException(int errorCode, String string) {
        super(string);
        this.errorCode = errorCode;
    }

    public int getErrorCode() {
        return errorCode;
    }

}