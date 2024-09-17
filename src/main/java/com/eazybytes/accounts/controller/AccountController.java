package com.eazybytes.accounts.controller;

import com.eazybytes.accounts.constants.AccountConstants;
import com.eazybytes.accounts.dto.CustomerDto;
import com.eazybytes.accounts.dto.RespondDto;
import com.eazybytes.accounts.service.AccountService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/account")
public class AccountController {
    private AccountService accountService;

    @PostMapping("/create")
    public ResponseEntity<RespondDto> createAccount(@RequestBody CustomerDto customerDto) {
        accountService.createAccount(customerDto);
        return ResponseEntity.
                status(HttpStatus.CREATED).body(new RespondDto(AccountConstants.STATUS_201, AccountConstants.MESSAGE_201));
    }

    @GetMapping("/fetch")
    public ResponseEntity<CustomerDto> fetchAccountDetails(@RequestParam String numberMobile) {
        CustomerDto customerDto = accountService.fetchAccount(numberMobile);
        return ResponseEntity.status(HttpStatus.OK).body(customerDto);
    }

    @PutMapping("/update")
    public ResponseEntity<RespondDto> updateAccountDetail(@RequestBody CustomerDto customerDto) {
        boolean isUpdate = accountService.updateAccount(customerDto);
        if (isUpdate) {
            return ResponseEntity.status(HttpStatus.OK).
                    body(new RespondDto(AccountConstants.STATUS_200, AccountConstants.MESSAGE_200));
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).
                    body(new RespondDto(AccountConstants.STATUS_500, AccountConstants.MESSAGE_500));
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<RespondDto> deleteAccountDetails(@RequestParam String mobileNumber) {
        boolean isDelete = accountService.deleteAccount(mobileNumber);
        if (isDelete) {
            return ResponseEntity.status(HttpStatus.OK).body(new RespondDto(AccountConstants.STATUS_200, AccountConstants.MESSAGE_200));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body((new RespondDto(AccountConstants.STATUS_500, AccountConstants.MESSAGE_500)));
        }
    }
}
