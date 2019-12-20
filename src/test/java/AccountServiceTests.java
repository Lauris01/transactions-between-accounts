import com.bank.api.model.CreateAccountRequest;
import com.bank.db.ConnectionUtil;
import com.bank.generated.tables.records.AccountRecord;
import com.bank.service.AccountService;
import com.bank.service.AccountServiceImpl;
import org.jooq.DSLContext;
import org.junit.AfterClass;
import org.junit.BeforeClass;

import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class AccountServiceTests {

    private static AccountService accountService;
    private static DSLContext dslContext;

    @BeforeClass
    public static void setUp() {
        dslContext = ConnectionUtil.getDBConnection();
        accountService = new AccountServiceImpl(dslContext);
    }

    @AfterClass
    public static void tearDown() {
        dslContext.close();
    }


    @org.junit.Test
    public void createAccountTest() {
        CreateAccountRequest car = createAccountRequestObj("OwnerName1", BigDecimal.valueOf(400));
        AccountRecord response = accountService.createAccount(car);
        assertEquals(car.getOwner(), response.getOwner());
        assertEquals(car.getBalance().compareTo(response.getBalance()), 0);
        assertNotNull(response.getId());
    }

    private CreateAccountRequest createAccountRequestObj(String ownerName1, BigDecimal balance) {
        return new CreateAccountRequest(ownerName1, balance);
    }


}
