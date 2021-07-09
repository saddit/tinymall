package jmu.shijh.tinymall.shiro;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationException;
import org.springframework.http.HttpStatus;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

/**
 * 通过转发并携带异常信息到异常处理器中抛出异常
 */
@Slf4j
public class FilterExceptionResolver {

    public static final String EXCEPTION_CONTROLLER = "/oauth/error/token/";

    public static void doThrow(HttpServletRequest request, HttpServletResponse response, String msg) throws IOException {
        doThrow(request,response, AuthenticationException.class,msg);
    }

    public static void doThrow(HttpServletRequest request, HttpServletResponse response, Class<? extends AuthenticationException> clazz, String msg) {
        try {
            Object exception = clazz.getConstructor(String.class).newInstance(msg);
            request.setAttribute("exception", exception);
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException classEx) {
            request.setAttribute("exception", new Exception("unknown cast exception"));
        }
        try {
            request.getRequestDispatcher(EXCEPTION_CONTROLLER).forward(request,response);
        } catch (ServletException | IOException e) {
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            log.error(e.toString());
        }
    }
}
