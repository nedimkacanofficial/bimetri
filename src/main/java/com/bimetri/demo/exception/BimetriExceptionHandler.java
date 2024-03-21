package com.bimetri.demo.exception;

import com.bimetri.demo.exception.message.ApiResponseError;
import jakarta.annotation.Nonnull;
import org.springframework.beans.ConversionNotSupportedException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.naming.AuthenticationException;
import java.nio.file.AccessDeniedException;
import java.util.List;

@ControllerAdvice
public class BimetriExceptionHandler extends ResponseEntityExceptionHandler {
    private ResponseEntity<Object> buildResponseEntity(ApiResponseError error) {
        return new ResponseEntity<>(error, error.getStatus());
    }

    /**
     * Handles ResourceNotFoundException and generates a custom ResponseEntity for the response.
     * <p>
     * This method is designed to handle exceptions of type ResourceNotFoundException.
     * It creates a custom error response in the form of an ApiResponseError containing
     * information about the not found resource and returns a ResponseEntity with the appropriate HTTP status.
     *
     * @param ex      The ResourceNotFoundException instance that triggered the exception.
     * @param request The WebRequest associated with the request that resulted in the exception.
     * @return ResponseEntity<Object> A ResponseEntity containing an ApiResponseError indicating
     * that the requested resource was not found. HttpStatus.NOT_FOUND is returned.
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    protected ResponseEntity<Object> handleResourceNotFoundException(ResourceNotFoundException ex, WebRequest request) {
        ApiResponseError error = new ApiResponseError(HttpStatus.NOT_FOUND, ex.getMessage(), request.getDescription(false));
        return buildResponseEntity(error);
    }

    /**
     * Handles ConflictException and generates a custom ResponseEntity for the response.
     * <p>
     * This method is designed to handle exceptions of type ConflictException.
     * It creates a custom error response in the form of an ApiResponseError containing
     * information about the conflict and returns a ResponseEntity with the appropriate HTTP status.
     *
     * @param ex      The ConflictException instance that triggered the exception.
     * @param request The WebRequest associated with the request that resulted in the exception.
     * @return ResponseEntity<Object> A ResponseEntity containing an ApiResponseError indicating
     * that a conflict occurred. HttpStatus.CONFLICT is returned.
     */
    @ExceptionHandler(ConflictException.class)
    protected ResponseEntity<Object> handleConflictException(ConflictException ex, WebRequest request) {
        ApiResponseError error = new ApiResponseError(HttpStatus.CONFLICT, ex.getMessage(), request.getDescription(false));
        return buildResponseEntity(error);
    }

    /**
     * Handles BadRequestException and generates a custom ResponseEntity for the response.
     * <p>
     * This method is designed to handle exceptions of type BadRequestException.
     * It creates a custom error response in the form of an ApiResponseError containing
     * information about the bad request and returns a ResponseEntity with the appropriate HTTP status.
     *
     * @param ex      The BadRequestException instance that triggered the exception.
     * @param request The WebRequest associated with the request that resulted in the exception.
     * @return ResponseEntity<Object> A ResponseEntity containing an ApiResponseError indicating
     * that a bad request occurred. HttpStatus.BAD_REQUEST is returned.
     */
    @ExceptionHandler(BadRequestException.class)
    protected ResponseEntity<Object> handleBadRequestException(BadRequestException ex, WebRequest request) {
        ApiResponseError error = new ApiResponseError(HttpStatus.BAD_REQUEST, ex.getMessage(), request.getDescription(false));
        return buildResponseEntity(error);
    }

    /**
     * Handles MethodArgumentNotValidException and generates a custom ResponseEntity for the response.
     * <p>
     * This method is designed to handle exceptions of type MethodArgumentNotValidException,
     * which occurs when the validation of method arguments annotated with @Valid fails.
     * It extracts error messages from the exception and creates a custom error response
     * in the form of an ApiResponseError containing information about the validation errors.
     *
     * @param ex      The MethodArgumentNotValidException instance that triggered the exception.
     * @param headers The HttpHeaders to be included in the error response.
     * @param status  The HttpStatusCode to be included in the error response.
     * @param request The WebRequest associated with the request that resulted in the exception.
     * @return ResponseEntity<Object> A ResponseEntity containing an ApiResponseError indicating
     * that a validation error occurred. HttpStatus.BAD_REQUEST is returned.
     */
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, @Nonnull HttpHeaders headers, @Nonnull HttpStatusCode status, WebRequest request) {
        List<String> errors = ex.getBindingResult().getFieldErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).toList();
        ApiResponseError error = new ApiResponseError(HttpStatus.BAD_REQUEST, errors.get(0), request.getDescription(false));
        return buildResponseEntity(error);
    }

    /**
     * Handles TypeMismatchException and generates a custom ResponseEntity for the response.
     * <p>
     * This method is designed to handle exceptions of type TypeMismatchException,
     * which occurs when a method argument is not the expected type.
     * It creates a custom error response in the form of an ApiResponseError containing
     * information about the type mismatch and returns a ResponseEntity with the appropriate HTTP status.
     *
     * @param ex      The TypeMismatchException instance that triggered the exception.
     * @param headers The HttpHeaders to be included in the error response.
     * @param status  The HttpStatusCode to be included in the error response.
     * @param request The WebRequest associated with the request that resulted in the exception.
     * @return ResponseEntity<Object> A ResponseEntity containing an ApiResponseError indicating
     * that a type mismatch error occurred. HttpStatus.BAD_REQUEST is returned.
     */
    @Override
    protected ResponseEntity<Object> handleTypeMismatch(TypeMismatchException ex, @Nonnull HttpHeaders headers, @Nonnull HttpStatusCode status, WebRequest request) {
        ApiResponseError error = new ApiResponseError(HttpStatus.BAD_REQUEST, ex.getMessage(), request.getDescription(false));
        return buildResponseEntity(error);
    }

    /**
     * Handles ConversionNotSupportedException and generates a custom ResponseEntity for the response.
     * <p>
     * This method is designed to handle exceptions of type ConversionNotSupportedException,
     * which occurs when a conversion is not supported.
     * It creates a custom error response in the form of an ApiResponseError containing
     * information about the unsupported conversion and returns a ResponseEntity with the appropriate HTTP status.
     *
     * @param ex      The ConversionNotSupportedException instance that triggered the exception.
     * @param headers The HttpHeaders to be included in the error response.
     * @param status  The HttpStatusCode to be included in the error response.
     * @param request The WebRequest associated with the request that resulted in the exception.
     * @return ResponseEntity<Object> A ResponseEntity containing an ApiResponseError indicating
     * that a conversion not supported error occurred. HttpStatus.INTERNAL_SERVER_ERROR is returned.
     */
    @Override
    protected ResponseEntity<Object> handleConversionNotSupported(ConversionNotSupportedException ex, @Nonnull HttpHeaders headers, @Nonnull HttpStatusCode status, WebRequest request) {
        ApiResponseError error = new ApiResponseError(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage(), request.getDescription(false));
        return buildResponseEntity(error);
    }

    /**
     * Handles HttpMessageNotReadableException and generates a custom ResponseEntity for the response.
     * <p>
     * This method is designed to handle exceptions of type HttpMessageNotReadableException,
     * which occurs when there are issues with the readability of the HTTP message.
     * It creates a custom error response in the form of an ApiResponseError containing
     * information about the not readable HTTP message and returns a ResponseEntity with the appropriate HTTP status.
     *
     * @param ex      The HttpMessageNotReadableException instance that triggered the exception.
     * @param headers The HttpHeaders to be included in the error response.
     * @param status  The HttpStatusCode to be included in the error response.
     * @param request The WebRequest associated with the request that resulted in the exception.
     * @return ResponseEntity<Object> A ResponseEntity containing an ApiResponseError indicating
     * that a not readable HTTP message error occurred. HttpStatus.BAD_REQUEST is returned.
     */
    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, @Nonnull HttpHeaders headers, @Nonnull HttpStatusCode status, WebRequest request) {
        ApiResponseError error = new ApiResponseError(HttpStatus.BAD_REQUEST, ex.getMessage(), request.getDescription(false));
        return buildResponseEntity(error);
    }

    /**
     * Handles AccessDeniedException and generates a ResponseEntity with an ApiResponseError.
     *
     * @param ex      The AccessDeniedException that was thrown.
     * @param request The WebRequest associated with the exception.
     * @return A ResponseEntity containing an ApiResponseError with details about the exception.
     */
    @ExceptionHandler(AccessDeniedException.class)
    protected ResponseEntity<Object> handleAccessDeniedException(AccessDeniedException ex, WebRequest request) {
        ApiResponseError error = new ApiResponseError(HttpStatus.FORBIDDEN, ex.getMessage(), request.getDescription(false));
        return buildResponseEntity(error);
    }

    /**
     * Handles exceptions related to authentication errors.
     * <p>
     * This method is designed to handle exceptions of type AuthenticationException,
     * which may occur during authentication processes. It returns a ResponseEntity
     * containing an ApiResponseError object with details about the error, including
     * the HTTP status code, error message, and a description of the request.
     * </p>
     *
     * @param ex      The AuthenticationException that was thrown.
     * @param request The WebRequest containing information about the current request.
     * @return ResponseEntity<Object> A ResponseEntity containing an ApiResponseError
     * object with details about the authentication error.
     */
    @ExceptionHandler(AuthenticationException.class)
    protected ResponseEntity<Object> handleAuthenticationException(AuthenticationException ex, WebRequest request) {
        ApiResponseError error = new ApiResponseError(HttpStatus.BAD_REQUEST, ex.getMessage(), request.getDescription(false));
        return buildResponseEntity(error);
    }


    /**
     * Handles RuntimeException and generates a custom ResponseEntity for the response.
     * <p>
     * This method is designed to handle general runtime exceptions (instances of RuntimeException).
     * It creates a custom error response in the form of an ApiResponseError containing
     * information about the runtime exception and returns a ResponseEntity with the appropriate HTTP status.
     *
     * @param ex      The RuntimeException instance that triggered the exception.
     * @param request The WebRequest associated with the request that resulted in the exception.
     * @return ResponseEntity<Object> A ResponseEntity containing an ApiResponseError indicating
     * that a general runtime exception occurred. HttpStatus.INTERNAL_SERVER_ERROR is returned.
     */
    @ExceptionHandler(RuntimeException.class)
    protected ResponseEntity<Object> handleGeneralException(RuntimeException ex, WebRequest request) {
        ApiResponseError error = new ApiResponseError(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage(), request.getDescription(false));
        return buildResponseEntity(error);
    }
}

