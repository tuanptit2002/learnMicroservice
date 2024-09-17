package com.eazybytes.accounts.exceptiom;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException{

    public ResourceNotFoundException(String resourceName, String fileName, String fileValue){
        super(String.format("%s not found with the given input data %s %s", resourceName,fileName,fileValue));
    }
}
