package org.squidmin.java.spring.gradle.bigquery.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ColumnAValidator implements ConstraintValidator<ColumnAConstraint, String> {

    @Override
    public void initialize(ColumnAConstraint columnA) {
    }

    @Override
    public boolean isValid(String columnA, ConstraintValidatorContext ctx) {
        return false;
    }

}
