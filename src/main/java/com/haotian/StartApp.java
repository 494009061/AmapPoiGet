package com.haotian;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author zhanghaotian
 */
@SpringBootApplication
public class StartApp {
    private static Logger log = LoggerFactory.getLogger(StartApp.class);

    public static void main(String[] args) {
        SpringApplication.run(StartApp.class, args);
        log.info("\n--------------------------------\n" +
                "Welcome to poi to get the program\n" +
                "Author: Mr.Zhang\n" +
                "Mobile: 17611681582\n" +
                "-----------------------------------");
    }
}
