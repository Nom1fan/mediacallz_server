package com.mediacallz.server.database.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Created by Mor on 1/17/2017.
 */
@Component
@Data
public class MySqlDbConfig implements DbConfig {

    @Value("${db.host}")
    private String host;

    @Value("${db.port}")
    private int port;

    @Value("${db.name}")
    private String dbName;

    @Value("${db.username}")
    private String username;

    @Value("${db.password}")
    private String password;

    @Value("${db.maxPoolSize}")
    private int maxPoolSize;

    @Value("${db.acquireIncrement}")
    private int acquireIncrement;

    @Value("${db.testConnectionOnCheckIn}")
    private Boolean testConnectionOnCheckIn;

    @Value("${db.idleConnectionTestPeriod}")
    private int idleConnectionTestPeriod;

    @Value("${db.maxIdleTimeExcessConnections}")
    private int maxIdleTimeExcessConnections;
}
