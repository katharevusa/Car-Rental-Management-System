///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package beanValidation;
//
//import java.lang.annotation.Documented;
//import java.lang.annotation.ElementType;
//import java.lang.annotation.Retention;
//import java.lang.annotation.RetentionPolicy;
//import java.lang.annotation.Target;
//import javax.validation.Constraint;
//import javax.validation.Payload;
//
//@Target({ElementType.CONSTRUCTOR, ElementType.ANNOTATION_TYPE})
//    @Retention(RetentionPolicy.RUNTIME)
//    @Constraint(validatedBy = DateRangeValidator.class)
//    @Documented
//    public static @interface DateRangeParams {
//
//        String message () default "'start date' must be less than 'end date'. " +
//                            "Found: 'start date'=${validatedValue[0]}, " +
//                            "'end date'=${validatedValue[1]}";
//
//        Class<?>[] groups () default {};
//        Class<? extends Payload>[] payload () default {};
//    }
