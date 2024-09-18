package com.eazybytes.accounts.exceptiom;

import com.eazybytes.accounts.dto.ErrorRespondDto;
import com.eazybytes.accounts.entity.Customer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        Map<String, String> validationErrors = new HashMap<>();
        List<ObjectError> validationErrorList = ex.getBindingResult().getAllErrors();
        validationErrorList.forEach((error) ->{
            String fileName = ((FieldError)error).getField();
            String validationMsg = error.getDefaultMessage();
            validationErrors.put(fileName, validationMsg);
        });
        return new ResponseEntity<>(validationErrors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorRespondDto> handleGlobalException(Exception exception,
                                                                           WebRequest webRequest){
        ErrorRespondDto errorRespondDto = new ErrorRespondDto(
                webRequest.getDescription(false),
                HttpStatus.INTERNAL_SERVER_ERROR,
                exception.getMessage(),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(errorRespondDto, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorRespondDto> handleResourceNotFoundException(CustomerAlreadyExistsException exception,
                                                                               WebRequest webRequest){
        ErrorRespondDto errorRespondDto = new ErrorRespondDto(
                webRequest.getDescription(false),
                HttpStatus.NOT_FOUND,
                exception.getMessage(),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(errorRespondDto, HttpStatus.NOT_FOUND);
    }

    public ResponseEntity<ErrorRespondDto> handleCustomerAlreadyExitsException(CustomerAlreadyExistsException exception,
                                                                               WebRequest webRequest){
        ErrorRespondDto errorRespondDto = new ErrorRespondDto(
                webRequest.getDescription(false),
                HttpStatus.BAD_REQUEST,
                exception.getMessage(),
                LocalDateTime.now()
                );
        return new ResponseEntity<>(errorRespondDto, HttpStatus.BAD_REQUEST);
    }
}
