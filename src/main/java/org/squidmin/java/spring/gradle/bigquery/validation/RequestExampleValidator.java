package org.squidmin.java.spring.gradle.bigquery.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class RequestExampleValidator implements ConstraintValidator<RequestExampleConstraint, String> {

    @Override
    public void initialize(RequestExampleConstraint constrainedField) {
    }

    @Override
    public boolean isValid(String constrainedField, ConstraintValidatorContext ctx) {
        return false;
    }

}