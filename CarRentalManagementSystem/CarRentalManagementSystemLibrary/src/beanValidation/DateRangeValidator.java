///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package beanValidation;
//
//import java.time.LocalDate;
//import javax.validation.ConstraintValidator;
//import javax.validation.ConstraintValidatorContext;
//
//@SupportedValidationTarget(ValidationTarget.PARAMETERS)
//public static class DateRangeValidator implements
//        ConstraintValidator<DateRangeParams, Object[]> {
//
//    @Override
//    public void initialize(DateRangeParams constraintAnnotation) {
//    }
//
//    @Override
//    public boolean isValid(Object[] value, ConstraintValidatorContext context) {
//        if (value == null || value.length != 2
//                || !(value[0] instanceof LocalDate)
//                || !(value[1] instanceof LocalDate)) {
//            return false;
//        }
//
//        return ((LocalDate) value[0]).isBefore((LocalDate) value[1]);
//    }
//}
