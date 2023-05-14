package org.squidmin.java.spring.gradle.bigquery.validation;

import org.squidmin.java.spring.gradle.bigquery.dto.ExampleRequestItem;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;

public class ExampleRequestBodyValidator implements ConstraintValidator<ExampleRequestBodyConstraint, List<ExampleRequestItem>> {

    @Override
    public void initialize(ExampleRequestBodyConstraint constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(List<ExampleRequestItem> value, ConstraintValidatorContext context) {
        return true;
    }

}
