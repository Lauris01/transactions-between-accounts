package com.bank.service;


import com.bank.api.model.CreateAccountRequest;
import com.bank.exceptions.AccountNotFoundException;
import com.bank.generated.tables.records.AccountRecord;

import java.util.List;

public interface AccountService {
    AccountRecord getAccount(int id) throws AccountNotFoundException;

    AccountRecord createAccount(CreateAccountRequest createAccountRequest);

    List<AccountRecord> getAll();

    boolean deleteAccount(int id);
}
