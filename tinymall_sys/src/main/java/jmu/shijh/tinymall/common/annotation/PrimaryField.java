package jmu.shijh.tinymall.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface PrimaryField {
    String value() default "";
    boolean auto() default true;

    /**
     * 使用雪花算法生成 id - 优先级高于 auto()
     */
    boolean snowFlake() default false;
}
