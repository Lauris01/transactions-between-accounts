package com.bank.api.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * api account requests
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class CreateAccountRequest {
    private String owner;
    private BigDecimal balance;
}
