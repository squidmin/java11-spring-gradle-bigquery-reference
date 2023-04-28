package org.squidmin.java.spring.gradle.bigquery.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class FieldAValidator implements ConstraintValidator<FieldAConstraint, String> {

    @Override
    public void initialize(FieldAConstraint fieldA) {
    }

    @Override
    public boolean isValid(String fieldA, ConstraintValidatorContext ctx) {
        return false;
    }

}
