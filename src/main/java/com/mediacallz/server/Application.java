package com.mediacallz.server;

import com.google.gson.Gson;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.mediacallz.server.db.config.DbConfig;
import com.mediacallz.server.logs.LogFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.embedded.tomcat.TomcatConnectorCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.AbstractJdbcInsert;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.filter.CommonsRequestLoggingFilter;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;

import javax.annotation.PostConstruct;
import javax.servlet.Filter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

@SpringBootApplication
@Slf4j
public class Application {

    @Value("${server.port}")
    private int serverPort;

    private final DbConfig dbConfig;

    @Autowired
    public Application(DbConfig dbConfig) {
        this.dbConfig = dbConfig;
    }

    @PostConstruct
    public void init() {
        log.info("Server running on port:" + serverPort + "...");
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public MultipartResolver getMultiPMultipartResolver() {
        return new StandardServletMultipartResolver();
    }

    @Bean
    public Gson getGson() {
        return new Gson();
    }

    @Bean(name = "myNamedParameterJdbcOperations")
    public NamedParameterJdbcOperations namedParameterJdbcOperations() {
        return new NamedParameterJdbcTemplate(getDataSource());
    }

    @Bean
    public JdbcTemplate jdbcTemplate() {
        return new JdbcTemplate(getDataSource());
    }

    @Bean
    public ComboPooledDataSource getDataSource() {
        ComboPooledDataSource dataSource = new ComboPooledDataSource();
        String jdbcUrl = "jdbc:mysql://" + dbConfig.getHost() + ":" + dbConfig.getPort() + "/" + dbConfig.getDbName();
        dataSource.setJdbcUrl(jdbcUrl);
        dataSource.setUser(dbConfig.getUsername());
        dataSource.setPassword(dbConfig.getPassword());
        dataSource.setMaxPoolSize(dbConfig.getMaxPoolSize());
        dataSource.setAcquireIncrement(dbConfig.getAcquireIncrement());
        dataSource.setTestConnectionOnCheckin(dbConfig.getTestConnectionOnCheckIn());
        dataSource.setIdleConnectionTestPeriod(dbConfig.getIdleConnectionTestPeriod());
        dataSource.setMaxIdleTimeExcessConnections(dbConfig.getMaxIdleTimeExcessConnections());
        return dataSource;
    }

    @Bean
    public Filter logFilter() {
        CommonsRequestLoggingFilter filter = new CommonsRequestLoggingFilter();
        filter.setIncludeClientInfo(true);
        filter.setIncludeQueryString(true);
        filter.setIncludePayload(true);
        filter.setMaxPayloadLength(5120);
        return filter;
    }
}
