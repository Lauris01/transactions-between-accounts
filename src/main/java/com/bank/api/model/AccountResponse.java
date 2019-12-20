package com.bank.api.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * schema for account api responses
 */
public @AllArgsConstructor @Data @NoArgsConstructor
class AccountResponse {
    private int id;
    private String owner;
    private BigDecimal balance;


}
