package org.nanking.knightingal;

import org.nanking.knightingal.util.TimeUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public TimeUtil timeUtil() {
        return new TimeUtil();
    }

    @Bean
    public DateFormat fmt() {
        return new SimpleDateFormat("YYYYMMddhhmmss");
    }

}
