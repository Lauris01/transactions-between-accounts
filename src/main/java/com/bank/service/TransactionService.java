package com.bank.service;

import com.bank.api.model.CreateTransactionRequest;
import com.bank.exceptions.TransactionException;
import com.bank.generated.tables.records.AccountTransactionRecord;

import java.util.List;

public interface TransactionService {
    AccountTransactionRecord makeTransaction(CreateTransactionRequest createTransaction) throws TransactionException;

    AccountTransactionRecord createTransactionRecord(CreateTransactionRequest createTransaction);

    List<AccountTransactionRecord> getAll();
}
