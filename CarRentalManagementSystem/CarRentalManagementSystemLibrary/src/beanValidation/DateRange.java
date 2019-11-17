package beanValidation;


import static java.lang.annotation.ElementType.CONSTRUCTOR;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.time.LocalDateTime;
import java.util.List;
import javax.validation.Constraint;
import javax.validation.Payload;
@Target({CONSTRUCTOR})
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = {DateRangeValidator.class})


public @interface DateRange {
    
    String message() default "Pickup datetime must be before return datetime!";
    Class<?>[] groups() default { };
    Class<? extends Payload>[] payload() default { };
    
}