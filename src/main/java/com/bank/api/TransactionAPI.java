package com.bank.api;

import com.bank.api.model.CreateTransactionRequest;
import com.bank.api.model.TransactionResponse;
import com.bank.generated.tables.records.AccountTransactionRecord;
import com.bank.service.TransactionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;
import spark.Route;

import java.util.stream.Collectors;

/**
 * /transaction api routes
 */
public class TransactionAPI {
    @Inject
    private TransactionService transactionService;
    @Inject
    private ObjectMapper objectMapper;

    public TransactionAPI() {
    }

    public Route makeTransaction = (request, response) -> {
        CreateTransactionRequest createTransaction = objectMapper.readValue(request.body(), CreateTransactionRequest.class);
        AccountTransactionRecord result = transactionService.makeTransaction(createTransaction);
        response.status(200);
        return objectMapper.writeValueAsString(new TransactionResponse(result.getSentFrom(), result.getSendTo(), result.getAmount(), result.getStatus()));
    };

    public Route getTransactions = (request, response) -> objectMapper.writeValueAsString(transactionService.getAll().stream().map(result -> new TransactionResponse(result.getSentFrom(), result.getSendTo(), result.getAmount(), result.getStatus())).collect(Collectors.toList()));
}

