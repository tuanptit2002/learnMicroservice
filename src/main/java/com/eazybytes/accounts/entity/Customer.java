package com.eazybytes.accounts.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Customer extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @Column(name = "customer_id")
    private Long customerId;
    private String name;
    private String email;
    private String mobileNumber;

}
