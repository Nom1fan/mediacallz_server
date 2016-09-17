package com.mediacallz.server;

import com.google.gson.Gson;
import logs.LogFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

@SpringBootApplication
public class Application {

    private static Logger logger;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
        logger.info("Server running on port 8080...");
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
    public Logger logger() {
        try {
            LogFactory.createServerLogsDir();
        } catch (IOException e) {
            e.printStackTrace();
        }

        logger = LogFactory.get_serverLogger(Level.INFO);
        return LogFactory.get_serverLogger(Level.INFO);
    }
}
