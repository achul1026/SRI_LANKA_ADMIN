package com.sri.lanka.traffic.admin.common.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.sri.lanka.traffic.admin.common.enums.ValidationPattern;

public class CustomPatternValidator  implements ConstraintValidator<CustomPatternValidation, String> {
	private ValidationPattern patternEnum;

    @Override
    public void initialize(CustomPatternValidation constraintAnnotation) {
        this.patternEnum = constraintAnnotation.value();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return true; // null은 @NotBlank로 처리
        }
        boolean isValid =  value.matches(patternEnum.getPattern());
        
        return isValid;
    }
}
