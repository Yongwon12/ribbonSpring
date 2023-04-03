package com.project.ribbon.customvaild;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class DigitLengthValidator implements ConstraintValidator<DigitLength, Integer> {
    private int min;
    private int max;

    @Override
    public void initialize(DigitLength constraintAnnotation) {
        this.min = constraintAnnotation.min();
        this.max = constraintAnnotation.max();
    }

    @Override
    public boolean isValid(Integer value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        int length = String.valueOf(value).length();
        return length >= min && length <= max;
    }
}
