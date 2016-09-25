package com.mediacallz.server;

import com.google.gson.Gson;
import com.mediacallz.server.logs.LogFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

@SpringBootApplication
public class Application {

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
        logger.info("Server running on port 8080...");
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
