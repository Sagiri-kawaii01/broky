package cn.cimoc.broky.core;

import java.lang.annotation.*;

/**
 * @author LGZ
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface BrokyResponse {
}
