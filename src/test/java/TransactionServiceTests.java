import com.bank.api.model.CreateTransactionRequest;
import com.bank.db.ConnectionUtil;
import com.bank.generated.tables.records.AccountRecord;
import com.bank.generated.tables.records.AccountTransactionRecord;
import com.bank.service.AccountService;
import com.bank.service.AccountServiceImpl;
import com.bank.service.TransactionServiceImpl;
import org.jooq.DSLContext;
import org.junit.AfterClass;
import org.junit.BeforeClass;

import java.math.BigDecimal;
import java.util.concurrent.CountDownLatch;

import static org.junit.Assert.assertEquals;


public class TransactionServiceTests {

    private static AccountService accountService;
    private static TransactionServiceImpl transactionService;
    private static DSLContext dslContext;

    @BeforeClass
    public static void setUp() {
        dslContext = ConnectionUtil.getDBConnection();
        accountService = new AccountServiceImpl(dslContext);
        transactionService = new TransactionServiceImpl(dslContext);

    }

    @AfterClass
    public static void tearDown() {
        dslContext.close();
    }

    @org.junit.Test
    public void test001_testCreateTranscationRecord() {
        CreateTransactionRequest ctr = createDummyCTR(1, 2, BigDecimal.valueOf(133));
        AccountTransactionRecord res = transactionService.createTransactionRecord(ctr);
        assertEquals(res.getAmount().compareTo( ctr.getAmount()), 0);
        assertEquals((int) res.getSentFrom(), ctr.getFrom());
        assertEquals((int) res.getSendTo(), ctr.getTo());
    }

    @org.junit.Test
    public void test002_testIfTransactionRecordReturnedIsCorrect() {
        AccountRecord fromBefore = accountService.getAccount(3);
        AccountRecord toBefore = accountService.getAccount(4);
        BigDecimal amount = BigDecimal.valueOf(10);
        AccountTransactionRecord res = transactionService.makeTransaction(createDummyCTR(fromBefore.getId(), toBefore.getId(), amount));
        AccountRecord fromAfter = accountService.getAccount(3);
        AccountRecord toAfter = accountService.getAccount(4);
        assertEquals(fromAfter.getBalance(), fromBefore.getBalance().subtract(res.getAmount()));
        assertEquals(toAfter.getBalance(), toBefore.getBalance().add(res.getAmount()));
    }

    @org.junit.Test
    public void test003_testIfMoneyIsNotLost() {
        AccountRecord fromBefore = accountService.getAccount(5);
        AccountRecord toBefore = accountService.getAccount(6);
        BigDecimal amount = new BigDecimal(10);
        transactionService.makeTransaction(createDummyCTR(fromBefore.getId(), toBefore.getId(), amount));

        AccountRecord fromAfter = accountService.getAccount(fromBefore.getId());
        AccountRecord toAfter = accountService.getAccount(toBefore.getId());
        assertEquals(fromAfter.getBalance(), fromBefore.getBalance().subtract(amount));
        assertEquals(toAfter.getBalance(), toBefore.getBalance().add(amount));
    }

    @org.junit.Test
    public void test004_testIfMoneyIsNotLostInMultiThreadedWay() throws InterruptedException {
        AccountRecord fromBefore = accountService.getAccount(7);
        AccountRecord toBefore = accountService.getAccount(8);

        int nrOfThreds = 20;
        BigDecimal amount = new BigDecimal(1);

        final CountDownLatch latch = new CountDownLatch(nrOfThreds);
        for (int i = 0; i < nrOfThreds; i++) {
            int multiply = i;
            new Thread(() -> {
                try {
                    transactionService.makeTransaction(createDummyCTR(fromBefore.getId(), toBefore.getId(), amount.multiply(BigDecimal.valueOf(multiply))));
                } catch (Exception e) {
                } finally {
                    latch.countDown();
                }
            }).start();
        }
        latch.await();

        AccountRecord fromAfter = accountService.getAccount(fromBefore.getId());
        AccountRecord toAfter = accountService.getAccount(toBefore.getId());
        int diff1 = fromBefore.getBalance().subtract(fromAfter.getBalance()).intValue();
        int diff2 = toAfter.getBalance().subtract(toBefore.getBalance()).intValue();
        assertEquals(diff1,diff2);

    }


    static CreateTransactionRequest createDummyCTR(int from, int to, BigDecimal ammount) {
        CreateTransactionRequest ctr = new CreateTransactionRequest(from,to,ammount);
        return ctr;
    }


}
