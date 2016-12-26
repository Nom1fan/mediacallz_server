package com.mediacallz.server;

import com.google.gson.Gson;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.mediacallz.server.logs.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

@SpringBootApplication
public class Application {

    @Value("${server.port}")
    private int serverPort;

    @Value("${db.host}")
    private String dbHost;

    @Value("${db.port}")
    private int dbPort;

    @Value("${db.name}")
    private String dbName;

    @Value("${db.username}")
    private String dbUsername;

    @Value("${db.password}")
    private String dbPassword;

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

    @PostConstruct
    public void init() {
        logger().info("Server running on port:" + serverPort + "...");
    }

    private static Map<String,Level> logLevelsMap = new HashMap<String,Level>() {{
        put("DEBUG", Level.CONFIG);
        put("CONFIG", Level.CONFIG);
        put("INFO", Level.INFO);
    }};
    private static Logger logger;
    private static Level logLevel = Level.INFO;

    public static void main(String[] args) {
        if(args.length > 0)
            setLogLevel(args[0]);
        SpringApplication.run(Application.class, args);
        logger.info("Log level:" + logger.getLevel());
    }

    @Bean
    public MultipartResolver getMultiPMultipartResolver() {
        return new StandardServletMultipartResolver();
    }

    @Bean
    public Gson getGson() {
        return new Gson();
    }

    @Bean
    public ComboPooledDataSource getDataSource() {
        ComboPooledDataSource dataSource = new ComboPooledDataSource();
        String jdbcUrl = "jdbc:mysql://" + dbHost + ":" + dbPort + "/" + dbName;
        dataSource.setJdbcUrl(jdbcUrl);
        dataSource.setUser(dbUsername);
        dataSource.setPassword(dbPassword);
        dataSource.setMaxPoolSize(maxPoolSize);
        dataSource.setAcquireIncrement(acquireIncrement);
        dataSource.setTestConnectionOnCheckin(testConnectionOnCheckIn);
        dataSource.setIdleConnectionTestPeriod(idleConnectionTestPeriod);
        dataSource.setMaxIdleTimeExcessConnections(maxIdleTimeExcessConnections);
        return dataSource;
    }

    @Bean
    public Logger logger() {
        try {
            LogFactory.createServerLogsDir();
        } catch (IOException e) {
            e.printStackTrace();
        }

        logger = LogFactory.get_serverLogger(logLevel);
        return logger;
    }

    private static void setLogLevel(String logLevel) {
        if(logLevel !=null)
            Application.logLevel = logLevelsMap.get(logLevel.toUpperCase());
    }
}
