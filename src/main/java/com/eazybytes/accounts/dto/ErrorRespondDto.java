package com.eazybytes.accounts.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ErrorRespondDto {

    private String apiPath;
    private HttpStatus code;
    private String errorMessage;
    private LocalDateTime errorTime;
}
