package jmu.shijh.tinymall.common.exception;

import org.springframework.web.bind.annotation.ExceptionHandler;

public class LoginFailureException extends UserStateException{
    public LoginFailureException() {
        super();
    }

    public LoginFailureException(String message) {
        super(message);
    }

    public LoginFailureException(String message, Throwable cause) {
        super(message, cause);
    }

    public LoginFailureException(Throwable cause) {
        super(cause);
    }

}
