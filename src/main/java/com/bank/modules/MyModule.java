package com.bank.modules;

import com.bank.Application;
import com.bank.db.ConnectionUtil;
import com.bank.mapper.ExtendedObjectMapper;
import com.bank.service.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import org.jooq.DSLContext;


public class MyModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(DSLContext.class).toInstance(ConnectionUtil.getDBConnection());
        bind(AccountService.class).to(AccountServiceImpl.class);
        bind(TransactionService.class).to(TransactionServiceImpl.class);
        bind(Application.class).in(Singleton.class);
        bind(ObjectMapper.class).toInstance(new ExtendedObjectMapper());
    }

}
