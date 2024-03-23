package com.bimetri.demo.exception.message;

public class ErrorMessage {
    public static final String RESOURCE_NOT_FOUND_MESSAGE = "Resource with id: %s not found!";
    public static final String NOT_FOUND_MESSAGE = "Resource with %s not found!";
    public static final String RESOURCE_MAX_COUNT = "You have reached the maximum number of %s registrations!";
    public static final String DUPLICATE_NAME = "There is already a record with the name %s.";
    public static final String DUPLICATE_SCHOOL_NUMBER = "There is already a record with the school number %s.";
    public static final String DUPLICATE_COURSE = "Student is already enrolled in this course.";
    public static final String ILLEGAL_EXCEPTION = "The record could not be deleted because it is enrolled in one or more courses.";
}
