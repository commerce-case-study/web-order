package com.commerce.web.order.config.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * <p>
 * empty value means method is accessible for login user
 * <p/>
 * <ul>
 *   <li>@NeedLogin</li>
 * </ul>
 */
@Retention(RUNTIME)
@Target(METHOD)
public @interface NeedLogin {
    String[] value() default {};
}
