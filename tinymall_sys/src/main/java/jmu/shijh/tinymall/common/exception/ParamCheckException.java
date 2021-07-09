package jmu.shijh.tinymall.common.exception;

public class ParamCheckException extends RuntimeException {
    public ParamCheckException() {
        super();
    }

    public ParamCheckException(String message) {
        super(message);
    }

    public ParamCheckException(String message, Throwable cause) {
        super(message, cause);
    }

    public ParamCheckException(Throwable cause) {
        super(cause);
    }

    protected ParamCheckException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
