package com.bank.db;


import org.apache.commons.dbcp.BasicDataSource;
import org.jooq.Configuration;
import org.jooq.ConnectionProvider;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.conf.Settings;
import org.jooq.impl.*;

import java.io.IOException;
import java.util.Properties;

/**
 * DSLContext
 */
public class ConnectionUtil {

    public static DSLContext getDBConnection() {
        final BasicDataSource ds = new BasicDataSource();
        final Properties properties = new Properties();
        try {
            properties.load(ConnectionUtil.class.getResourceAsStream("/config.properties"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        ds.setDriverClassName(properties.getProperty("db.driver"));
        ds.setUrl(properties.getProperty("db.url"));
        ds.setUsername(properties.getProperty("db.username"));
        ds.setPassword(properties.getProperty("db.password"));

        final ConnectionProvider cp = new DataSourceConnectionProvider(ds);
        final Configuration configuration = new DefaultConfiguration()
                .set(cp)
                .set(SQLDialect.H2)
                .set(new ThreadLocalTransactionProvider(cp, true));
        return DSL.using(configuration);
    }
}
