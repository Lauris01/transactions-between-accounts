package com.bank;

import com.bank.exceptions.TransactionException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.bank.api.AccountAPI;
import com.bank.api.TransactionAPI;
import com.bank.exceptions.AccountNotFoundException;
import com.bank.modules.MyModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static spark.Spark.*;

/**
 * start app class
 */
public class Application {

    private final static Logger LOG = LoggerFactory.getLogger(Application.class.getCanonicalName());

    public static void main(String[] args) {
        int port = Integer.parseInt(System.getProperty("port", "4567"));
        Guice.createInjector(new MyModule()).getInstance(Application.class).run(port);
    }

    @Inject
    private AccountAPI accountApi;
    @Inject
    private TransactionAPI transactionAPI;

    public Application() {
    }

    public void run(int port) {
        port(port);
        path("", () -> {
            before("*",(request,response)->LOG.info("Request:{}",request.uri()));
            path("/accounts", () -> {
                get("", accountApi.getAccounts);
                put("", accountApi.createAccount);
            });
            path("/transactions", () -> {
                post("", transactionAPI.makeTransaction);
                get("", transactionAPI.getTransactions);
            });
        });
        //error handling
        exception(TransactionException.class, (e, request, response) -> {
                    response.status(400);
                    response.body(e.getMessage());
                }
        );
        exception(AccountNotFoundException.class, (e, request, response) -> {
            response.status(400);
            response.body(e.getMessage());
        });
        exception(InvalidFormatException.class, (e, request, response) -> {
            response.status(400);
            response.body("Bad request body. Please check API documentation");
        });
        exception(Exception.class, (e, request, response) -> {
            response.status(400);
            response.body("Invalid request body");
        });
        LOG.info("Application started");
        awaitInitialization();

    }

}
