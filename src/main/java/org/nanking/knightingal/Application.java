package org.nanking.knightingal;

import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.nanking.knightingal.util.EncryptUtil;
import org.nanking.knightingal.util.FileUtil;
import org.nanking.knightingal.util.TimeUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        ApplicationContext applicationContext = SpringApplication.run(Application.class, args);

    }

    @Bean
    public TimeUtil timeUtil() {
        return new TimeUtil();
    }

    @Bean
    public DateFormat fmt() {
        return new SimpleDateFormat("YYYYMMddHHmmss");
    }

    @Bean
    public FileUtil fileUtil() {
        return new FileUtil();
    }

    @Bean
    public Executor downloadImgThreadPoolExecutor() {
        return Executors.newScheduledThreadPool(30);
    }

    @Bean
    public Executor downloadSectionThreadPoolExecutor() {
        return Executors.newScheduledThreadPool(3);
    }

    @Bean
    public EncryptUtil encryptUtil() {
        return new EncryptUtil();
    }
}
