package jmu.shijh.tinymall.common.exception;

public class CustomException extends RuntimeException {
    private Object errorData;

    public CustomException() {
        super();
    }

    public CustomException(String message) {
        super(message);
    }

    public CustomException(String message, Object errorData) {
        super(message);
        this.errorData = errorData;
    }

    public CustomException(String message, Throwable cause) {
        super(message, cause);
    }

    public CustomException(Throwable cause) {
        super(cause);
    }

    protected CustomException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public Object getErrorData() {
        return errorData;
    }
}
