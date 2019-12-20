package com.bank.api.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * transaction api response
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public
class TransactionResponse {
    private int from;
    private int to;
    private BigDecimal amount;
    private boolean status;
}
