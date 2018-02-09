package ergot.anthony.com.ergot.exception;

import android.support.annotation.StringRes;

import ergot.anthony.com.ergot.MyApplication;

/**
 * Created by Anthony on 24/10/2017.
 */

public class TechnicalException extends Exception {

    private String userMessage;

    public TechnicalException(String message, @StringRes int userMessageId) {
        super(message);
        this.userMessage = MyApplication.getMyApplication().getString(userMessageId);
    }

    public TechnicalException(String message, String userMessage) {
        super(message);
        this.userMessage = userMessage;
    }

    public TechnicalException(String message, Throwable cause, String userMessage) {
        super(message, cause);
        this.userMessage = userMessage;
    }

    public TechnicalException(String message, Throwable cause, @StringRes int userMessageId) {
        super(message, cause);
        this.userMessage = MyApplication.getMyApplication().getString(userMessageId);
    }

    public TechnicalException(Throwable cause, String userMessage) {
        super(cause);
        this.userMessage = userMessage;
    }

    public String getUserMessage() {
        return userMessage;
    }

    public void setUserMessage(String userMessage) {
        this.userMessage = userMessage;
    }
}
