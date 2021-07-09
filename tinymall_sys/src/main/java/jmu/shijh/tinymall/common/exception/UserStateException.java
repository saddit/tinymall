package jmu.shijh.tinymall.common.exception;

import org.apache.shiro.authc.AuthenticationException;

public class UserStateException extends AuthenticationException {
    public UserStateException() {
        super();
    }

    public UserStateException(String message) {
        super(message);
    }

    public UserStateException(String message, Throwable cause) {
        super(message, cause);
    }

    public UserStateException(Throwable cause) {
        super(cause);
    }

}
