package com.bank.service;

import com.bank.api.model.CreateTransactionRequest;
import com.bank.exceptions.TransactionException;
import com.bank.generated.tables.Account;
import com.bank.generated.tables.AccountTransaction;
import com.bank.generated.tables.records.AccountRecord;
import com.bank.generated.tables.records.AccountTransactionRecord;
import com.google.inject.Inject;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.conf.Settings;
import org.jooq.exception.DataAccessException;
import org.jooq.impl.DSL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.List;

/**
 * Transactions service
 */
public class TransactionServiceImpl implements TransactionService {

    private final static Logger LOG = LoggerFactory.getLogger(TransactionServiceImpl.class.getCanonicalName());
    private DSLContext dslContext;


    @Inject
    public TransactionServiceImpl(final DSLContext dslContext) {
        this.dslContext = dslContext;
    }

    /**
     * method to make transactions between accounts
     *
     * @param ctr CreateTransactionRequest object
     * @return
     * @throws TransactionException if balance is too low.
     */
    @Override
    public AccountTransactionRecord makeTransaction(final CreateTransactionRequest ctr) throws TransactionException {
        AccountTransactionRecord ac = createTransactionRecord(ctr);
        try {
            int count = 0;
            int maxTries = 3;
            while (true) {
                try {
                    if (doTransaction(ctr.getFrom(), ctr.getTo(), ctr.getAmount()) == 2) {
                        ac.setStatus(true);
                        break;
                    }
                } catch (DataAccessException e) {
                    try {
                        Thread.sleep(5);
                    } catch (InterruptedException ignored) {
                        throw new TransactionException();
                    }
                    if (++count == maxTries) throw new TransactionException();
                }
            }
        } catch (DataAccessException e) {
            throw new TransactionException();
        } catch (TransactionException e) {
            ac.setStatus(false);
            throw e;
        } finally {
            ac.store();
            return ac;
        }
    }

    /**
     * @param from    account id from
     * @param to      account id to
     * @param ammount ammount
     * @return 2 if transaction succeeded
     */
    private int doTransaction(int from, int to, BigDecimal ammount) {
        return dslContext.transactionResult(outer -> {
            int result = 0;
            DSLContext inner = DSL.using(outer.connectionProvider(), SQLDialect.H2, new Settings().withExecuteWithOptimisticLocking(true));

            AccountRecord fromAR = inner.selectFrom(Account.ACCOUNT)
                    .where(Account.ACCOUNT.ID.eq(from))
                    .forUpdate()
                    .fetchOne();
            AccountRecord toAR = inner.selectFrom(Account.ACCOUNT)
                    .where(Account.ACCOUNT.ID.eq(to))
                    .forUpdate()
                    .fetchOne();
            if (fromAR.getBalance().compareTo(ammount) < 0) {
                LOG.error("Transfer failed. Insufficient funds");
                throw new TransactionException("Insufficient funds for account:" + String.valueOf(from));
            }
            fromAR.setBalance(fromAR.getBalance().subtract(ammount));
            result += fromAR.store();
            toAR.setBalance(toAR.getBalance().add(ammount));
            result += toAR.store();
            return result;
        });

    }

    ;


    /**
     * @param createTransactionRequest
     * @return new Record with status false
     */
    @Override
    public AccountTransactionRecord createTransactionRecord(CreateTransactionRequest createTransactionRequest) {
        AccountTransactionRecord re = new AccountTransactionRecord();
        re.setAmount(createTransactionRequest.getAmount());
        re.setSentFrom(createTransactionRequest.getFrom());
        re.setSendTo(createTransactionRequest.getTo());
        re.setStatus(false);
        return dslContext.newRecord(com.bank.generated.tables.AccountTransaction.ACCOUNT_TRANSACTION, re);
    }

    /**
     * @return all transactions ever made
     */
    @Override
    public List<AccountTransactionRecord> getAll() {
        return dslContext
                .select(AccountTransaction.ACCOUNT_TRANSACTION.fields())
                .from(AccountTransaction.ACCOUNT_TRANSACTION)
                .fetchInto(AccountTransactionRecord.class);
    }
}
