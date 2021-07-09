package jmu.shijh.tinymall.common.aop;


import jmu.shijh.tinymall.common.annotation.ArrayParamChecks;
import jmu.shijh.tinymall.common.annotation.MultiRequestBody;
import jmu.shijh.tinymall.common.annotation.ParamCheck;
import jmu.shijh.tinymall.common.annotation.ParamChecks;
import jmu.shijh.tinymall.common.exception.ParamCheckException;
import jmu.shijh.tinymall.common.util.Cl;
import jmu.shijh.tinymall.common.util.Str;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ClassUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpSession;
import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

@Aspect
@Component
@Slf4j
@Order(2)
public class ParamCheckAdvice {
    private final int ERROR1 = 1;
    private final int ERROR2 = 2;
    private final int ERROR3 = 3;
    private final int OK = -1;

    @Pointcut("@annotation(jmu.shijh.tinymall.common.annotation.ParamCheck)")
    public void pointCut() {
    }

    @Pointcut("@annotation(jmu.shijh.tinymall.common.annotation.ParamChecks)")
    public void multiParamCheck() {
    }

    @Pointcut("@annotation(jmu.shijh.tinymall.common.annotation.ArrayParamChecks)")
    public void arrayParamChecks() {
    }

    private <T extends Annotation> T getAnnotation(JoinPoint jp, Class<T> targetClass) {
        return ((MethodSignature) jp.getSignature()).getMethod().getAnnotation(targetClass);
    }

    private boolean isTarget(ParamCheck rule, String argName) {
        return rule.target().isEmpty() || rule.target().equals(argName);
    }

    private boolean isAllowCheck(ParamCheck anno, String paramName) {
        if (anno.include().length > 0) {
            return Arrays.asList(anno.include()).contains(paramName);
        } else if (anno.exclude().length > 0) {
            return !Arrays.asList(anno.exclude()).contains(paramName);
        }
        return true;
    }

    private boolean isSpringObject(Object arg) {
        return arg instanceof HttpSession || arg instanceof ModelAndView
                || arg instanceof ModelMap || arg instanceof ServletResponse
                || arg instanceof ServletRequest || arg instanceof Model;
    }

    private void throwException(ParamCheck anno) throws ParamCheckException {
        throw new ParamCheckException(anno == null ? "Unknown Error!" : anno.value());
    }

    private void throwException(String msg) throws ParamCheckException {
        throw new ParamCheckException(msg);
    }

    private boolean isWrongParam(Object param, ParamCheck anno) {
        boolean regex = true;
        boolean length = true;
        //必要性检查
        boolean require = Str.notEmpty(param) || !anno.required();
        if(require && Str.notEmpty(param)) {
            //正则检查
            String regexp = anno.regexp();
            if (Str.notEmpty(regexp) && param instanceof String) {
                regex = param.toString().matches(regexp);
            }
            //长度检查
            if (anno.lengthLE() != -1) {
                length = lengthCheck(anno,param,anno.lengthLE(),0,1);
            } else if (anno.lengthLT() != -1) {
                length = lengthCheck(anno,param,anno.lengthLT(),1);
            } else if (anno.lengthEQ() != -1) {
                length = lengthCheck(anno,param,anno.lengthEQ(),0);
            }
        }
        return !(require && regex && length);
    }

    /**
     * rule 最多三种 -1 0 1
     */
    private boolean lengthCheck(ParamCheck anno, Object o, Integer val, Integer... rule) {
        List<Integer> rules = Cl.list(rule);
        if (anno.isArray()) {
            int l = 0;
            if (o instanceof Collection) {
                l = ((Collection) o).size();
            } else if (o.getClass().isArray()){
                l = Array.getLength(o);
            }
            return (l != 0 || !anno.required()) && rules.contains(val.compareTo(l));
        } else if (anno.split2Array()) {
            String[] array = o.toString().split(anno.split());
            return rules.contains(val.compareTo(array.length));
        } else {
            return rules.contains(val.compareTo(o.toString().length()));
        }
    }

    private boolean hasAllowedField(Class<?> clazz, ParamCheck anno) {
        for (Field field : clazz.getDeclaredFields()) {
            if (isAllowCheck(anno, field.getName())) {
                return true;
            }
        }
        return false;
    }

    private int doSingleCheck(Object arg, Parameter parameter, Class<?> argType,
                              String argName, ParamCheck rule) throws IllegalAccessException{
        if (isSpringObject(arg) || !isTarget(rule, argName)) {
            return OK;
        }
        if (isAllowCheck(rule, argName) && isWrongParam(arg, rule)) {
            return ERROR1;
        } else if (arg != null) {
            Field[] fields = arg.getClass().getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
                if (isAllowCheck(rule, field.getName()) && isWrongParam(field.get(arg), rule)) {
                    return ERROR2;
                }
            }
        } else if (parameter != null && hasAllowedField(argType, rule)) {
            RequestBody reqBody = parameter.getAnnotation(RequestBody.class);
            MultiRequestBody mulReqBody = parameter.getAnnotation(MultiRequestBody.class);
            if (reqBody != null && !reqBody.required() && !rule.regexp().isEmpty() && !rule.required()) {
                return OK;
            }
            if (mulReqBody != null && !mulReqBody.required() && !rule.regexp().isEmpty() && !rule.required()) {
                return OK;
            }
            return ERROR3;
        }
        return OK;
    }

    private int doParamCheck(Object[] args, Class<?>[] argTypes,
                             String[] argNames, Parameter[] parameters,
                             ParamCheck rule) throws ParamCheckException, IllegalAccessException {
        for (int i = 0; i < args.length; i++) {
            Object arg = args[i];
            String argName = argNames[i];
            Class<?> argType = argTypes[i];
            int res = doSingleCheck(arg, parameters[i], argType, argName, rule);
            if (res != OK) {
                return res;
            }
        }
        return OK;
    }

    @Before(value = "ParamCheckAdvice.pointCut()")
    public void paramCheck(JoinPoint jp) throws ParamCheckException, IllegalAccessException {
        Object[] args = jp.getArgs();
        MethodSignature signature = (MethodSignature) jp.getSignature();
        Class<?>[] argTypes = signature.getParameterTypes();
        String[] argNames = signature.getParameterNames();
        Parameter[] parameters = signature.getMethod().getParameters();

        ParamCheck rule = getAnnotation(jp, ParamCheck.class);
        int errorCode = doParamCheck(args, argTypes, argNames, parameters, rule);
        if (errorCode != OK) {
            throwException(rule);
        }
    }

    @Before(value = "multiParamCheck()")
    public void paramChecks(JoinPoint jp) throws ParamCheckException, IllegalAccessException {
        Object[] args = jp.getArgs();
        MethodSignature signature = (MethodSignature) jp.getSignature();
        Class<?>[] argTypes = signature.getParameterTypes();
        String[] argNames = signature.getParameterNames();
        Parameter[] parameters = signature.getMethod().getParameters();
        ParamChecks rules = getAnnotation(jp, ParamChecks.class);
        for (ParamCheck rule : rules.value()) {
            int errorCode = doParamCheck(args, argTypes, argNames, parameters, rule);
            if (errorCode != OK) {
                throwException(rule);
            }
        }
    }

    @Before(value = "arrayParamChecks()")
    public void arrayParamChecks(JoinPoint jp) throws ParamCheckException, IllegalAccessException{
        Object[] args = jp.getArgs();
        MethodSignature signature = (MethodSignature) jp.getSignature();
        String[] argNames = signature.getParameterNames();
        ArrayParamChecks rules = getAnnotation(jp, ArrayParamChecks.class);
        for (int i = 0; i<args.length; i++) {
            Object arg = args[i];
            String argName = argNames[i];
            if (arg instanceof Collection) {
                if(rules.targets().length > 0 && !Cl.list(rules.targets()).contains(argName)) {
                    continue;
                }
                if(rules.required() && ((Collection) arg).isEmpty()) {
                    throwException("参数为空");
                }
                for (Object item : (Collection<?>) arg) {
                    for (ParamCheck rule : rules.value()) {
                        int code = doSingleCheck(item, null, null, null, rule);
                        if (code != OK) {
                            throwException(rule);
                        }
                    }
                }
            }
        }
    }

}
