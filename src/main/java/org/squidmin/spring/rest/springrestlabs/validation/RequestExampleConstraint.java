package org.squidmin.spring.rest.springrestlabs.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = RequestExampleValidator.class)
@Target({ ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface RequestExampleConstraint {

    String message() default "Invalid request";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

}
