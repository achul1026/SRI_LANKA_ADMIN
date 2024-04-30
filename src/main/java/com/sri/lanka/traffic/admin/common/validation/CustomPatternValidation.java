package com.sri.lanka.traffic.admin.common.validation;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.CONSTRUCTOR;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.ElementType.TYPE_USE;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import com.sri.lanka.traffic.admin.common.enums.ValidationPattern;

@Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE })
@Documented
@Constraint(validatedBy = CustomPatternValidator.class)
@Retention(RetentionPolicy.RUNTIME)
public @interface CustomPatternValidation {

	String message() default "{javax.validation.constraints.Pattern.message}";
	
    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    ValidationPattern value();
}
