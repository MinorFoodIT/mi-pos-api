package com.minor.store.utils.log;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * mark the field that its content need to be masked with 'x' except last 3 characters.
 * Note: need to mark the class with @MaskLog
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface MaskLog3 {
}
