package com.eazybytes.accounts.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RespondDto {

    private String statusCode;
    private String statusMsg;
}
