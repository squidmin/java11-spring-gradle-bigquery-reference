package org.squidmin.java.spring.gradle.bigquery.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = ExampleRequestBodyValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ExampleRequestBodyConstraint {

    String message() default "Invalid request.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
