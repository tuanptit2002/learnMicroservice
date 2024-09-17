package com.eazybytes.accounts.service.impl;

import com.eazybytes.accounts.constants.AccountConstants;
import com.eazybytes.accounts.dto.AccountDto;
import com.eazybytes.accounts.dto.CustomerDto;
import com.eazybytes.accounts.entity.Account;
import com.eazybytes.accounts.entity.Customer;
import com.eazybytes.accounts.exceptiom.CustomerAlreadyExistsException;
import com.eazybytes.accounts.exceptiom.ResourceNotFoundException;
import com.eazybytes.accounts.mapper.AccountsMapper;
import com.eazybytes.accounts.mapper.CustomerMapper;
import com.eazybytes.accounts.repository.AccountRepository;
import com.eazybytes.accounts.repository.CustomerRepository;
import com.eazybytes.accounts.service.AccountService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@Service
@AllArgsConstructor

public class AccountServiceImpl implements AccountService {


    private AccountRepository accountRepository;
    private CustomerRepository customerRepository;

    /**
     *
     * @param customerDto- customerDto Object
     */

    @Override
    public void createAccount(CustomerDto customerDto) {
        Customer customer = CustomerMapper.mapToCustomer(new Customer(),customerDto);
        Optional<Customer> optionalCustomer = customerRepository.findByMobileNumber(customerDto.getMobileNumber());
        if(optionalCustomer.isPresent()){
            throw new CustomerAlreadyExistsException("Customer already registered with given mobileNumber"+ customerDto.getMobileNumber());
        }
        customer.setCreatedAt(LocalDateTime.now());
        customer.setCreatedBy("tuan phung");
        Customer saveCustomer =  customerRepository.save(customer);
        accountRepository.save(createNewAccount(saveCustomer));
    }

    /**
     *
     * @param mobileNumber
     * @return
     */
    @Override
    public CustomerDto fetchAccount(String mobileNumber) {
       Customer customer =  customerRepository.findByMobileNumber(mobileNumber).orElseThrow(
                () ->new ResourceNotFoundException("Customer","mobileNumber", mobileNumber)
        );
       Account account  = accountRepository.findByCustomerId(customer.getCustomerId()).orElseThrow(
               () -> new ResourceNotFoundException("Account","customerId",customer.getCustomerId().toString())
       );
       CustomerDto customerDto = CustomerMapper.mapToCustomerDto(customer,new CustomerDto());
       customerDto.setAccountDto(AccountsMapper.mapToAccountsDto(account, new AccountDto()));
        return customerDto;
    }

    /**
     *
     * @param customerDto
     * @return
     */
    @Override
    public boolean updateAccount(CustomerDto customerDto) {
        boolean isUpdate = false;
        AccountDto accountDto = customerDto.getAccountDto();
        if(accountDto != null){
            Account account = accountRepository.findById(accountDto.getAccountNumber()).orElseThrow(
                    () -> new ResourceNotFoundException("Account","AccountNumber",accountDto.getAccountNumber().toString())
            );
            AccountsMapper.mapToAccount(accountDto, account);
            accountRepository.save(account);
            Long customerId = account.getCustomerId();
            Customer customer = customerRepository.findById(customerId).orElseThrow(
                    () -> new ResourceNotFoundException("Customer","CustomerID",customerId.toString())
            );
            CustomerMapper.mapToCustomer(customer, customerDto);
            customerRepository.save(customer);
            isUpdate = true;
        }
        return isUpdate;
    }

    @Override
    public boolean deleteAccount(String mobileNumber) {
        Customer customer = customerRepository.findByMobileNumber(mobileNumber).orElseThrow(
                () -> new ResourceNotFoundException("Account", "MobileNumber",mobileNumber.toString())
        );
        Long customerId = customer.getCustomerId();
        accountRepository.deleteByCustomerId(customerId);
        customerRepository.deleteByCustomerId(customerId);
        return true;
    }


    private Account createNewAccount(Customer customer){
        Account newAccount = new Account();
        newAccount.setCustomerId(customer.getCustomerId());
        newAccount.setBranchAddress(AccountConstants.ADDRESS);
        long randomAccNumber = 1000000000L + new Random().nextInt(900000000);
        newAccount.setAccountNumber(randomAccNumber);
        newAccount.setAccountType(AccountConstants.SAVING);
        newAccount.setCreatedBy("Phung Tuan");
        newAccount.setCreatedAt(LocalDateTime.now());
        return newAccount;
    }
}
