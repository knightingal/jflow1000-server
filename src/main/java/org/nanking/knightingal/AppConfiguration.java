package org.nanking.knightingal;

import okhttp3.OkHttpClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.nanking.knightingal.util.EncryptUtil;
import org.nanking.knightingal.util.FileUtil;
import org.nanking.knightingal.util.TimeUtil;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
@Configuration
public class AppConfiguration {
    @Bean
    public TimeUtil timeUtil() {
        return new TimeUtil();
    }

    @Bean
    public DateFormat fmt() {
        return new SimpleDateFormat("yyyyMMddHHmmss");
    }

    @Bean
    public FileUtil fileUtil() {
        return new FileUtil();
    }

    @Bean("downloadImgThreadPoolExecutor")
    public Executor imgThreadPoolExecutor() {
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

    @Bean("client")
    public OkHttpClient okHttpclient() {
        return new OkHttpClient();
    }
}
