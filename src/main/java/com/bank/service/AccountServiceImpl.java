package com.bank.service;

import com.bank.api.model.CreateAccountRequest;
import com.google.inject.Inject;
import com.bank.exceptions.AccountNotFoundException;
import com.bank.generated.tables.Account;
import com.bank.generated.tables.records.AccountRecord;
import org.jooq.DSLContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Account Service
 */
public class AccountServiceImpl implements AccountService {

    private final static Logger LOG = LoggerFactory.getLogger(AccountServiceImpl.class.getCanonicalName());

    private DSLContext dslContext;
    @Inject
    public AccountServiceImpl( DSLContext dslContext) {
        this.dslContext = dslContext;
    }

    /**
     *
     * @param id account id
     * @return AccountRecord by id
     */
    @Override
    public AccountRecord getAccount(int id) {
        AccountRecord account = dslContext.selectFrom(Account.ACCOUNT)
                .where(Account.ACCOUNT.ID.eq(id))
                .forUpdate().of(Account.ACCOUNT.BALANCE)
                .fetchOne();
        if (account == null)
            throw new AccountNotFoundException(String.valueOf(id));
        return account;
    }

    /**
     *
     * @param createAccountRequest
     * @return new AccountRecord
     */
    @Override
    public AccountRecord createAccount(CreateAccountRequest createAccountRequest) {
        AccountRecord accountRecord = new AccountRecord();
        accountRecord.setBalance(createAccountRequest.getBalance());
        accountRecord.setOwner(createAccountRequest.getOwner());
        accountRecord = dslContext.newRecord(Account.ACCOUNT, accountRecord);
        accountRecord.store();
        return accountRecord;
    }

    @Override
    public List<AccountRecord> getAll() {
        return dslContext
                .select(Account.ACCOUNT.ID, Account.ACCOUNT.OWNER, Account.ACCOUNT.BALANCE)
                .from(Account.ACCOUNT)
                .fetchInto(AccountRecord.class);
    }

    @Override
    public boolean deleteAccount(int id) {
        return dslContext.delete(Account.ACCOUNT).where(Account.ACCOUNT.ID.eq(id)).execute() == 1;
    }


}
