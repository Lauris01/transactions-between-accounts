//import com.bank.Application;
//import com.bank.api.model.AccountResponse;
//import com.bank.api.model.CreateAccountRequest;
//import com.bank.api.model.CreateTransactionRequest;
//import com.bank.api.model.TransactionResponse;
//import com.bank.modules.MyModule;
//import com.bank.mapper.ExtendedObjectMapper;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.google.inject.Guice;
//import org.apache.http.HttpResponse;
//import org.apache.http.client.methods.HttpGet;
//import org.apache.http.client.methods.HttpPost;
//import org.apache.http.client.methods.HttpPut;
//import org.apache.http.client.utils.HttpClientUtils;
//import org.apache.http.client.utils.URIBuilder;
//import org.apache.http.entity.StringEntity;
//import org.apache.http.impl.client.CloseableHttpClient;
//import org.apache.http.impl.client.HttpClients;
//import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
//import org.apache.http.util.EntityUtils;
//import org.junit.AfterClass;
//import org.junit.BeforeClass;
//import org.junit.Test;
//
//import java.io.IOException;
//import java.math.BigDecimal;
//import java.net.URI;
//import java.net.URISyntaxException;
//
//import static junit.framework.TestCase.assertEquals;
//import static junit.framework.TestCase.assertNotNull;
//
///**
// * testing api
// */
//public class HttpServiceTest {
//
//    private static final int PORT = 4049;
//    protected static CloseableHttpClient client;
//    protected static PoolingHttpClientConnectionManager connManager = new PoolingHttpClientConnectionManager();
//    protected URIBuilder builder = new URIBuilder().setScheme("http").setHost("localhost:" + PORT);
//
//    protected ObjectMapper mapper = new ExtendedObjectMapper();
//
//    @BeforeClass
//    public static void setup() {
//        Guice.createInjector(new MyModule()).getInstance(Application.class).run(PORT);
//        client = HttpClients.custom()
//                .setConnectionManager(connManager)
//                .setConnectionManagerShared(true)
//                .build();
//    }
//
//    @AfterClass
//    public static void closeClient() {
//        HttpClientUtils.closeQuietly(client);
//    }
//
//    @Test
//    public void getAllAccountsAPI() throws IOException, URISyntaxException {
//        System.out.println("AAA");
//        URI uri = builder.setPath("/accounts").build();
//        HttpGet request = new HttpGet(uri);
//        HttpResponse response = client.execute(request);
//        int statusCode = response.getStatusLine().getStatusCode();
//        assertEquals(200, statusCode);
//        System.out.println("AAA");
//
//    }
//
//    @Test
//    public void testCreateAccountAPI() throws IOException, URISyntaxException {
//        System.out.println("BBB");
//
//        String owner = "creatTestName1";
//        BigDecimal balance = new BigDecimal(789);
//        URI uri = builder.setPath("/accounts").build();
//        HttpPut request = new HttpPut(uri);
//        request.setHeader("Content-type", "application/json");
//        CreateAccountRequest createAccountRequest = new CreateAccountRequest(owner, balance);
//        String json = mapper.writeValueAsString(createAccountRequest);
//        request.setEntity(new StringEntity(json));
//        HttpResponse response = client.execute(request);
//        int statusCode = response.getStatusLine().getStatusCode();
//        String s = EntityUtils.toString(response.getEntity());
//        AccountResponse accountResponse = mapper.readValue(s, AccountResponse.class);
//        assertEquals(201, statusCode);
//        assertEquals(accountResponse.getOwner(), owner);
//        assertEquals(accountResponse.getBalance(), balance);
//        assertNotNull(accountResponse.getId());
//        System.out.println("BBB");
//
//    }
//
//    @Test
//    public void makeTransactionAPI() throws IOException, URISyntaxException {
//        System.out.println("CCC");
//        int i = 0;
//        System.out.println(i++);
//        int from = 1;
//        int to = 2;
//        BigDecimal amount = BigDecimal.valueOf(32);
//        CreateTransactionRequest ctr = new CreateTransactionRequest(from, to, amount);
//        BigDecimal balance = new BigDecimal(11);
//        System.out.println(i++);
//
//        URI uri = builder.setPath("/transactions").build();
//        HttpPost request = new HttpPost(uri);
//        request.setHeader("Content-type", "application/json");
//        String json = mapper.writeValueAsString(ctr);
//        System.out.println(i++);
//
//        request.setEntity(new StringEntity(json));
//        System.out.println(i++);
//
//        HttpResponse response = client.execute(request);
//        System.out.println(i++);
//
//        System.out.println(i++);
//
//        String s = EntityUtils.toString(response.getEntity());
//        System.out.println(i++);
//
//        TransactionResponse transactionResponse = mapper.readValue(s, TransactionResponse.class);
//        System.out.println(i++);
//
////        assertEquals(200, statusCode);
//        assertEquals(transactionResponse.getFrom(), from);
//        assertEquals(transactionResponse.getTo(), to);
//        assertEquals(transactionResponse.getAmount().compareTo(amount), 0);
//        System.out.println("CCC");
//
//    }
//
//    @Test
//    public void getAllTransactionsAPI() throws IOException, URISyntaxException {
//        System.out.println("DDD");
//
//        URI uri = builder.setPath("/transactions").build();
//        HttpGet request = new HttpGet(uri);
//        HttpResponse response = client.execute(request);
//        int statusCode = response.getStatusLine().getStatusCode();
//        assertEquals(200, statusCode);
//        System.out.println("DDD");
//
//    }
//
//
//}
