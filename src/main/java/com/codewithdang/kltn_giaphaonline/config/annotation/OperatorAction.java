package com.codewithdang.kltn_giaphaonline.config.annotation;

import com.codewithdang.kltn_giaphaonline.enums.CommonEnums;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.FIELD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface OperatorAction {
    CommonEnums.Operator value();
}
