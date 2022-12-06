package cn.cimoc.broky.log;

import java.lang.annotation.*;

/**
 * @author LGZ
 * <p>
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface BrokyLog {
    String runTime() default "-1";

    String module() default "默认模块";
    String optType() default "默认类型";
    String description() default "默认说明";
}
