package jmu.shijh.tinymall.common;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import jmu.shijh.tinymall.common.annotation.MultiRequestBody;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.security.auth.kerberos.KerberosKey;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.List;

/**
 * 多RequestBody解析器
 *
 * @author 明明如月，施嘉宏
 * @create 2018/08/27
 * @modify 2021/05/12
 */
@Component
public class MultiRequestBodyArgumentResolver implements HandlerMethodArgumentResolver {

    private static final String JSONBODY_ATTRIBUTE = "JSON_REQUEST_BODY";

    /**
     * 设置支持的方法参数类型
     *
     * @param parameter 方法参数
     * @return 支持的类型
     */
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        // 支持带@MultiRequestBody注解的参数
        boolean res = parameter.hasParameterAnnotation(MultiRequestBody.class);
        return res;
    }

    /**
     * 参数解析，利用fastjson
     * 注意：非基本类型返回null会报空指针异常，要通过反射或者JSON工具类创建一个空对象
     */
    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {

        String jsonBody = getRequestBody(webRequest);
        // 根据@MultiRequestBody注解value作为json解析的key
        MultiRequestBody mAnno = parameter.getParameterAnnotation(MultiRequestBody.class);

        //如果@MultiRequestBody注解没有设置value，则取参数名FrameworkServlet作为json解析的key
        String key = mAnno.value().isEmpty() ? parameter.getParameterName() : mAnno.value();

        //如果请求体为空
        if (StringUtils.isEmpty(jsonBody)) {
            if (mAnno.required()) {
                throw new IllegalArgumentException(String.format("required param %s is not present", key));
            } else {
                return null;
            }
        }

        JSONObject jsonObject = JSON.parseObject(jsonBody);
        Object value = null;

        if (StringUtils.isNotEmpty(key)) {
            value = jsonObject.get(key);
            // 如果设置了value但是解析不到，报错
            if (value == null && mAnno.required()) {
                throw new IllegalArgumentException(String.format("required param %s is not present", key));
            }
        }

        // 获取的注解后的类型
        Class<?> parameterType = parameter.getParameterType();
        // 通过注解的value或者参数名解析，能拿到value进行解析
        if (value != null) {
            //基本类型
            if (parameterType.isPrimitive()) {
                return parsePrimitive(parameterType.getName(), value);
            } else if (parameterType.isArray()) {
                JSONArray jsonArray = jsonObject.getJSONArray(key);
                return jsonArray.toArray();
            }
            // 基本类型包装类
            if (isPrimitiveWrapper(parameterType)) {
                return parsePrimitiveWrapper(parameterType, value);
                // 字符串类型
            } else if (parameterType == String.class) {
                return value.toString();
            }
            //集合
            //if(Collection.class.isAssignableFrom(parameterType)) {
            //    JSONArray array = jsonObject.getJSONArray(key);
            //    if (array.size() == 0 && mAnno.required()) {
            //        throw new IllegalArgumentException(key + " is required, but get empty array");
            //    }
            //    Object o = array.get(0);
            //    return array.toJavaList(o.getClass());
            //}
            // 其他复杂对象
            Object res = JSON.parseObject(value.toString(), parameterType);
            return res;
        }

        // 以下代码在无法通过 key 获取到请求参数时执行

        // 包装类型则结束
        if (isPrimitiveWrapper(parameterType)) {
            if (mAnno.required()) {
                throw new IllegalArgumentException(String.format("required param %s is not present", key));
            } else {
                return null;
            }
        }

        // 不允许整体解析则结束
        if (!mAnno.parseFromBody()) {
            // 如果是必传参数抛异常
            if (mAnno.required()) {
                throw new IllegalArgumentException(String.format("required param %s is not present", key));
            }
            // 否则返回null
            return null;
        }

        // 非基本类型，允许解析，将外层属性解析
        Object result;
        try {
            result = JSON.parseObject(jsonObject.toString(), parameterType);
        } catch (JSONException jsonException) {
            result = null;
        }

        // 如果非必要参数直接返回，否则如果没有一个属性有值则报错
        if (mAnno.required()) {
            boolean haveValue = false;
            Field[] declaredFields = parameterType.getDeclaredFields();
            for (Field field : declaredFields) {
                field.setAccessible(true);
                if (field.get(result) != null) {
                    haveValue = true;
                    break;
                }
            }
            if (!haveValue) {
                throw new IllegalArgumentException(String.format("required param %s is not present", key));
            }
        }
        return result;
    }

    /**
     * 基本类型解析
     */
    private Object parsePrimitive(String parameterTypeName, Object value) {
        final String booleanTypeName = "boolean";
        if (booleanTypeName.equals(parameterTypeName)) {
            return Boolean.valueOf(value.toString());
        }
        final String intTypeName = "int";
        if (intTypeName.equals(parameterTypeName)) {
            return Integer.valueOf(value.toString());
        }
        final String charTypeName = "char";
        if (charTypeName.equals(parameterTypeName)) {
            return value.toString().charAt(0);
        }
        final String shortTypeName = "short";
        if (shortTypeName.equals(parameterTypeName)) {
            return Short.valueOf(value.toString());
        }
        final String longTypeName = "long";
        if (longTypeName.equals(parameterTypeName)) {
            return Long.valueOf(value.toString());
        }
        final String floatTypeName = "float";
        if (floatTypeName.equals(parameterTypeName)) {
            return Float.valueOf(value.toString());
        }
        final String doubleTypeName = "double";
        if (doubleTypeName.equals(parameterTypeName)) {
            return Double.valueOf(value.toString());
        }
        final String byteTypeName = "byte";
        if (byteTypeName.equals(parameterTypeName)) {
            return Byte.valueOf(value.toString());
        }
        return null;
    }

    /**
     * 基本类型包装类解析
     */
    private Object parsePrimitiveWrapper(Class<?> parameterType, Object value) {
        if (Number.class.isAssignableFrom(parameterType)) {
            Number number = (Number) value;
            if (parameterType == Integer.class) {
                return number.intValue();
            } else if (parameterType == Short.class) {
                return number.shortValue();
            } else if (parameterType == Long.class) {
                return number.longValue();
            } else if (parameterType == Float.class) {
                return number.floatValue();
            } else if (parameterType == Double.class) {
                return number.doubleValue();
            } else if (parameterType == Byte.class) {
                return number.byteValue();
            }
        } else if (parameterType == Boolean.class) {
            return value.toString();
        } else if (parameterType == Character.class) {
            return value.toString().charAt(0);
        }
        return null;
    }

    /**
     * 判断是否为基本数据类型包装类
     */
    private boolean isPrimitiveWrapper(Class<?> clazz) {
        return ClassUtils.isPrimitiveWrapper(clazz);
    }

    /**
     * 获取请求体JSON字符串
     */
    private String getRequestBody(NativeWebRequest webRequest) {
        HttpServletRequest servletRequest = webRequest.getNativeRequest(HttpServletRequest.class);

        // 有就直接获取
        String jsonBody = (String) webRequest.getAttribute(JSONBODY_ATTRIBUTE, NativeWebRequest.SCOPE_REQUEST);
        // 没有就从请求中读取
        if (jsonBody == null) {
            try {
                jsonBody = IOUtils.toString(servletRequest.getReader());
                webRequest.setAttribute(JSONBODY_ATTRIBUTE, jsonBody, NativeWebRequest.SCOPE_REQUEST);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return jsonBody;
    }
}
