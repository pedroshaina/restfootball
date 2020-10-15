package de.planerio.developertest.rest;


import de.planerio.developertest.dto.ApiError;
import de.planerio.developertest.dto.ApiFieldValidationError;
import de.planerio.developertest.error.InvalidInputException;
import de.planerio.developertest.error.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(value = InvalidInputException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public ApiError handleInvalidInputException(final InvalidInputException e) {
        return new ApiError(
                HttpStatus.UNPROCESSABLE_ENTITY.value(),
                e.getMessage()
        );
    }

    @ExceptionHandler(value = ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError handleResourceNotFoundException(final ResourceNotFoundException e) {
        return new ApiError(
                HttpStatus.NOT_FOUND.value(),
                e.getMessage()
        );
    }

    @ExceptionHandler(value = HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleHttpMessageNotReadableException(final HttpMessageNotReadableException e) {

        final String exceptionErrorDetails = e.getCause().getMessage();

        return new ApiError(
                HttpStatus.BAD_REQUEST.value(),
                String.format("Error parsing the JSON object: %s", exceptionErrorDetails)
        );

    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public ApiFieldValidationError handleMethodArgumentNotValidException(final MethodArgumentNotValidException e) {

        final ApiFieldValidationError validationError = new ApiFieldValidationError(
                HttpStatus.UNPROCESSABLE_ENTITY.value(),
                "One or more fields have an error"
        );

        e.getBindingResult().getFieldErrors().forEach(error -> {
            validationError.addInvalidField(error.getField(), error.getDefaultMessage());
        });

        return validationError;
    }

}
