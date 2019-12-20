package com.bank.api.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * api transaction request
 */
public @Data @AllArgsConstructor @NoArgsConstructor
class CreateTransactionRequest {
    private int from;
    private int to;
    private BigDecimal amount;
}
