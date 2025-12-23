package com.example.notesApp.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(NoteNotFoundException.class)
    public ResponseEntity<ErrorObject> handleNoteNotFoundException(
            NoteNotFoundException exception, WebRequest request){
        ErrorObject errorObject = new ErrorObject();
        errorObject.setStatusCode(HttpStatus.NOT_FOUND.value());
        errorObject.setMessage(exception.getMessage());
        errorObject.setPath(request.getDescription(false).replace("uri=", ""));
        errorObject.setErrorTime(LocalDateTime.now());

        return new ResponseEntity<>(errorObject, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)// exception for validation errors
    public ResponseEntity<ErrorObject> handleValidationException(
            MethodArgumentNotValidException exception, WebRequest request){
       String message = exception.getBindingResult()
               .getFieldErrors()
               .stream().map(error -> error.getField() + ": " + error.getDefaultMessage())
               .collect(Collectors.joining(", "));

       ErrorObject errorObject = new ErrorObject();
       errorObject.setStatusCode(HttpStatus.BAD_REQUEST.value());
       errorObject.setMessage(message);
       errorObject.setErrorTime(LocalDateTime.now());
       errorObject.setPath(request.getDescription(false).replace("uri=", ""));

       return new ResponseEntity<>(errorObject, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorObject> handleGeneralExceptions(
            Exception e, WebRequest request){
        ErrorObject errorObject = new ErrorObject();
        errorObject.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
        errorObject.setMessage("Internal server error occurred");
        errorObject.setErrorTime(LocalDateTime.now());
        errorObject.setPath(request.getDescription(false).replace("uri=", ""));

        return new ResponseEntity<>(errorObject, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
