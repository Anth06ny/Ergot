package ergot.anthony.com.ergot.model.bean;

/**
 * Created by Anthony on 24/10/2017.
 */

public class TechnicalException extends Exception {

    private String userMessage;

    public TechnicalException(String userMessage) {
        this.userMessage = userMessage;
    }

    public TechnicalException(String message, String userMessage) {
        super(message);
        this.userMessage = userMessage;
    }

    public TechnicalException(String message, Throwable cause, String userMessage) {
        super(message, cause);
        this.userMessage = userMessage;
    }

    public TechnicalException(Throwable cause, String userMessage) {
        super(cause);
        this.userMessage = userMessage;
    }

    public TechnicalException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, String userMessage) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.userMessage = userMessage;
    }

    public String getUserMessage() {
        return userMessage;
    }

    public void setUserMessage(String userMessage) {
        this.userMessage = userMessage;
    }
}
