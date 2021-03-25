
package errorhandling;


public class ExceptionDTO {

    private int httpCode;
    private String description;
    
    public ExceptionDTO(int httpcode, String description) {
        this.httpCode = httpcode;
        this.description = description;
    }

    public int getHttpCode() {
        return httpCode;
    }

    public String getDescription() {
        return description;
    }
    
    
    
}
