package jmu.shijh.tinymall.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 带有此注解的 Field 即允许更新（包括插入），与数据库不同名可指定value值，默认同名不用赋值
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface UpdateField {
    String value() default "";
}
