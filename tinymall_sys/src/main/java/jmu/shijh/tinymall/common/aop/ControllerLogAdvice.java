package jmu.shijh.tinymall.common.aop;


import jmu.shijh.tinymall.common.util.Str;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;

@Aspect
@Component
@Slf4j
@Order(1)
public class ControllerLogAdvice {

    @Pointcut("@within(org.springframework.web.bind.annotation.RestController)")
    public void controller() {
    }

    @Before(value = "controller()")
    public void before(JoinPoint jp) {
        log.debug(Str.f("begin requesting-> [{}.{}]: [args({})]:{}",
                jp.getTarget().getClass().getSimpleName(), jp.getSignature().getName(), jp.getArgs().length, Arrays.toString(jp.getArgs())));
    }

    @AfterThrowing(value = "controller()", throwing = "e")
    public void throwing(JoinPoint joinPoint, Exception e) {
        StringBuilder s = new StringBuilder(
                Str.f("[{}.{}] throwing exception[{}], msg: {}",
                        joinPoint.getTarget().getClass().getSimpleName(),
                        joinPoint.getSignature().getName(),
                        e.getClass().getSimpleName(),
                        e.getMessage())
        );
        Throwable throwable = e.getCause();
        while (throwable != null) {
            s.append(Str.f(",cause by {} msg:{}",
                    throwable.getClass().getSimpleName(),
                    throwable.getMessage()));
            throwable = throwable.getCause();
        }
        log.debug(s.toString());
    }

}
