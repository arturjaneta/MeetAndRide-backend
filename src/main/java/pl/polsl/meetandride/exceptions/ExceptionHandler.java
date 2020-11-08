package pl.polsl.meetandride.exceptions;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import pl.polsl.meetandride.enums.ErrorCode;
import pl.polsl.meetandride.exceptions.DTOs.FieldErrorDescDTO;
import pl.polsl.meetandride.exceptions.DTOs.GenericErrorDescDTO;
import pl.polsl.meetandride.exceptions.DTOs.ObjectErrorDescDTO;
import pl.polsl.meetandride.exceptions.DTOs.ValidationErrorDescDTO;

import java.util.List;

@ControllerAdvice
public class ExceptionHandler {

    @org.springframework.web.bind.annotation.ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationErrorDescDTO> handleValidationException(MethodArgumentNotValidException e) {
        List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();
        List<ObjectError> objectErrors = e.getBindingResult().getGlobalErrors();

        ValidationErrorDescDTO errorDescDTO = new ValidationErrorDescDTO(
                ErrorCode.VALIDATION_FAILED, "Request data validation failed!", HttpStatus.BAD_REQUEST
        );

        objectErrors.forEach(element -> {
            errorDescDTO.getObjectErrors().add(new ObjectErrorDescDTO(element.getObjectName(), element.getDefaultMessage()));
        });

        fieldErrors.forEach(fieldError -> {
            errorDescDTO.getFieldErrors().add(
                    new FieldErrorDescDTO(
                            fieldError.getField(),
                            fieldError.getRejectedValue() != null ? fieldError.getRejectedValue() : "null",
                            fieldError.getCode()
                    )
            );
        });

        return new ResponseEntity<>(errorDescDTO, HttpStatus.BAD_REQUEST);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(value = EmailAlreadyTakenException.class)
    public ResponseEntity<GenericErrorDescDTO> handleEmailAlreadyTakenException(EmailAlreadyTakenException e) {
        return new ResponseEntity<>(
                new GenericErrorDescDTO(ErrorCode.EMAIL_ALREADY_TAKEN, e.getMessage(), HttpStatus.CONFLICT),
                HttpStatus.CONFLICT
        );
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(value = ResourceNotFoundException.class)
    public ResponseEntity<GenericErrorDescDTO> handleResourceNotFoundException(ResourceNotFoundException e) {
        return new ResponseEntity<>(
                new GenericErrorDescDTO(ErrorCode.RESOURCE_NOT_FOUND, e.getMessage(), HttpStatus.BAD_REQUEST),
                HttpStatus.BAD_REQUEST
        );
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(value = NoPermissionsException.class)
    public ResponseEntity<GenericErrorDescDTO> handleNoPermissionsException(NoPermissionsException e) {
        return new ResponseEntity<>(
                new GenericErrorDescDTO(ErrorCode.NO_PERMISSIONS, e.getMessage(), HttpStatus.FORBIDDEN),
                HttpStatus.FORBIDDEN
        );
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(value = IllegalArgumentException.class)
    public ResponseEntity<GenericErrorDescDTO> handleIllegalArgumentException(IllegalArgumentException e) {
        return new ResponseEntity<>(
                new GenericErrorDescDTO(ErrorCode.ILLEGAL_ARGUMENT_EXCEPTION, e.getMessage(), HttpStatus.BAD_REQUEST),
                HttpStatus.BAD_REQUEST
        );
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(value = EmptyResultDataAccessException.class)
    public ResponseEntity<GenericErrorDescDTO> handleIllegalArgumentException(EmptyResultDataAccessException e) {
        return new ResponseEntity<>(
                new GenericErrorDescDTO(ErrorCode.EMPTY_RESULT_DATA_ACCESS_EXCEPTION, e.getMessage(), HttpStatus.BAD_REQUEST),
                HttpStatus.BAD_REQUEST
        );
    }
}
