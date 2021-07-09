package jmu.shijh.tinymall.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 'exclude'、'include'只能使用其中一个,如果两个都用，那么只使用’include‘ <br/>
 * @author shijh
 */
@Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ParamCheck {
    String value() default "参数不能为空或格式有误";
    String regexp() default "";
    String split() default ",";
    int lengthLT() default -1;
    int lengthLE() default -1;
    int lengthEQ() default -1;
    boolean isArray() default false;
    boolean split2Array() default false;
    boolean required() default true;
    String target() default "";
    String[] include() default {};
    String[] exclude() default {};
}
