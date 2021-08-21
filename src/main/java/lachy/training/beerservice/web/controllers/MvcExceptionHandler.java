package lachy.training.beerservice.web.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
public class MvcExceptionHandler {

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<List<String>> validationErrorsHandler(ConstraintViolationException exception) {
        List<String> errors = new ArrayList<>(exception.getConstraintViolations().size());
        exception.getConstraintViolations().forEach(
                constraintViolation -> errors.add(constraintViolation.getPropertyPath() + ": " + exception.getMessage()));
        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(BindException.class)
    public ResponseEntity<List<ObjectError>> hanldeBindException(BindException exception) {
        return ResponseEntity.badRequest().body(exception.getAllErrors());
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity hanldeNotFoundException(NotFoundException exception) {
        return ResponseEntity.notFound().build();
    }

}
