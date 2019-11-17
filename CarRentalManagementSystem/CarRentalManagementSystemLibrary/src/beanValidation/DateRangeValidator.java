package beanValidation;

import entity.ReservationRecordEntity;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Date;

public class DateRangeValidator implements ConstraintValidator<DateRange, ReservationRecordEntity> {

    @Override
    public void initialize(DateRange constraintAnnotation) {
    }

    @Override
    public boolean isValid(ReservationRecordEntity reservation, ConstraintValidatorContext context) {

        // null values are valid
        if (reservation == null ) {
            return true;
        }
        
        return reservation.getPickUpDateTime().isBefore(reservation.getReturnDateTime());
    }

}
