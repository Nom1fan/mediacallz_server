package com.mediacallz.server;

import com.google.gson.Gson;
import logs.LogFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.io.IOException;
import java.util.logging.Logger;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public Gson getGson() {
        return new Gson();
    }

    @Bean
    public Logger logger() {
        try {
            LogFactory.createServerLogsDir();
            //LogFactory.clearLogs();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return LogFactory.get_serverLogger();
    }
}
