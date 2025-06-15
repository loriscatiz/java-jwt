package com.loriscatiz.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;

public class DBManager {
    private final HikariDataSource dataSource;

    public DBManager() {
        HikariConfig config = new HikariConfig();


        // These values can be pulled from a config class or .env reader
        config.setJdbcUrl(Config.get("DB_URL"));
        config.setUsername(Config.get("DB_USERNAME"));
        config.setPassword(Config.get("DB_PASSWORD"));

        config.setMaximumPoolSize(10); // reasonable default
        config.setMinimumIdle(2);
        config.setIdleTimeout(60000);
        config.setConnectionTimeout(30000);
        config.setMaxLifetime(1800000);


        this.dataSource = new HikariDataSource(config);
    }

    public DataSource getDataSource() {
        return dataSource;
    }

    public void close() {
        if (dataSource != null && !dataSource.isClosed()) {
            dataSource.close();
        }
    }
}