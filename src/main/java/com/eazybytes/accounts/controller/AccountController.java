package com.eazybytes.accounts.controller;

import com.eazybytes.accounts.constants.AccountConstants;
import com.eazybytes.accounts.dto.AccountsContactInfoDto;
import com.eazybytes.accounts.dto.CustomerDto;
import com.eazybytes.accounts.dto.RespondDto;
import com.eazybytes.accounts.service.AccountService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Tag(
        name = "CRUD APIs for Accounts in eazyBank",
        description = "CRUD REST APIs in EazyBank to CREATE,UPDATE,FETCH AND DELETE account details"
)
@RestController
@RequestMapping("/api/v1/account")
@Validated
@EnableConfigurationProperties(AccountsContactInfoDto.class)
public class AccountController {
    @Autowired
    private AccountService accountService;

    @Value("${build.version}")
    private String buildVersion;

    @Autowired
    AccountsContactInfoDto accountsContactInfoDto;

    @PostMapping("/create")
    public ResponseEntity<RespondDto> createAccount(@Valid @RequestBody CustomerDto customerDto) {
        accountService.createAccount(customerDto);
        return ResponseEntity.
                status(HttpStatus.CREATED).body(new RespondDto(AccountConstants.STATUS_201, AccountConstants.MESSAGE_201));
    }

    @GetMapping("/fetch")
    public ResponseEntity<CustomerDto> fetchAccountDetails(@RequestParam
                                                           @Pattern(regexp = "(^$|[0-9]{10})", message = "Account number must be 10 digits")
                                                           String numberMobile) {
        CustomerDto customerDto = accountService.fetchAccount(numberMobile);
        return ResponseEntity.status(HttpStatus.OK).body(customerDto);
    }

    @PutMapping("/update")
    public ResponseEntity<RespondDto> updateAccountDetail(@Valid @RequestBody CustomerDto customerDto) {
        boolean isUpdate = accountService.updateAccount(customerDto);
        if (isUpdate) {
            return ResponseEntity.status(HttpStatus.OK).
                    body(new RespondDto(AccountConstants.STATUS_200, AccountConstants.MESSAGE_200));
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).
                    body(new RespondDto(AccountConstants.STATUS_417, AccountConstants.MESSAGE_417_UPDATE));
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<RespondDto> deleteAccountDetails(@RequestParam
                                                           @Pattern(regexp = "(^$|[0-9]{10})", message = "Account number must be 10 digits")
                                                           String mobileNumber) {
        boolean isDelete = accountService.deleteAccount(mobileNumber);
        if (isDelete) {
            return ResponseEntity.status(HttpStatus.OK).body(new RespondDto(AccountConstants.STATUS_200, AccountConstants.MESSAGE_200));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body((new RespondDto(AccountConstants.STATUS_417, AccountConstants.MESSAGE_417_DELETE)));
        }
    }

    @GetMapping("/build-info")
    public ResponseEntity<AccountsContactInfoDto> getBuildInfo() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(accountsContactInfoDto);
    }

    @GetMapping("/build-version")
    public ResponseEntity<String> getBuild() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(buildVersion);
    }
}
