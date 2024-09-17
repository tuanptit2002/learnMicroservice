package com.eazybytes.accounts.exceptiom;

import com.eazybytes.accounts.dto.ErrorRespondDto;
import com.eazybytes.accounts.entity.Customer;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalExceptionHandler {

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
