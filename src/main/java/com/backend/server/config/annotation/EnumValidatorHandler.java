package com.backend.server.config.annotation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public class EnumValidatorHandler implements ConstraintValidator<EnumValidator, String> {

    private Set<String> acceptedValues;

    @Override
    public void initialize(EnumValidator annotation) {
        Class<? extends Enum<?>> enumSelected = annotation.enumClass();
        acceptedValues = Arrays.stream(enumSelected.getEnumConstants())
                .map(Enum::name)
                .collect(Collectors.toSet());
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return value == null || acceptedValues.contains(value);
    }
}