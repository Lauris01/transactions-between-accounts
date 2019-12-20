package com.bank.api;

import com.bank.api.model.AccountResponse;
import com.bank.api.model.CreateAccountRequest;
import com.bank.generated.tables.records.AccountRecord;
import com.bank.service.AccountService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;
import spark.Route;

import java.util.stream.Collectors;

/**
 * /account api routes
 */
public class AccountAPI {
    @Inject
    private AccountService accountService;
    @Inject
    private ObjectMapper objectMapper;

    public Route getAccounts = (request, response) -> objectMapper.writeValueAsString(accountService.getAll().stream().map(account -> new AccountResponse(account.getId(), account.getOwner(), account.getBalance())).collect(Collectors.toList()));

    public Route createAccount = (request, response) -> {
        CreateAccountRequest createAccountRequest = objectMapper.readValue(request.body(), CreateAccountRequest.class);
        AccountRecord accountRecord = accountService.createAccount(createAccountRequest);
        response.status(201);
        return objectMapper.writeValueAsString(new AccountResponse(accountRecord.getId(), accountRecord.getOwner(), accountRecord.getBalance()));
    };


}
