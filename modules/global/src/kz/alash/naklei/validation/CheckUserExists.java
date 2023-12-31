package kz.alash.naklei.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({ ElementType.PARAMETER})
@Retention(RUNTIME)
@Constraint(validatedBy = UserExistsValidator.class)
public @interface CheckUserExists {
    String message() default "{msg://kz.alash.naklei.service/UserExistsValidator.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
