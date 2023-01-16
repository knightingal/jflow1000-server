package org.nanking.knightingal;

import okhttp3.OkHttpClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.domain.Specification;
import org.nanking.knightingal.dao.Local1000SectionDao;
import org.nanking.knightingal.dao.jpa.Local1000SectionRepo;
import org.nanking.knightingal.util.ApplicationContextProvider;
import org.nanking.knightingal.util.EncryptUtil;
import org.nanking.knightingal.util.FileUtil;
import org.nanking.knightingal.util.TimeUtil;

import java.lang.reflect.Proxy;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @author Knightingal
 */
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
        return new OkHttpClient.Builder().connectTimeout(60, TimeUnit.SECONDS).readTimeout(60, TimeUnit.SECONDS).build();
    }

    @Bean("local1000SectionDao")
    public Local1000SectionDao local1000SectionDao() {
        // return new Local1000SectionDaoImpl();

        Local1000SectionDao local1000SectionDao = (Local1000SectionDao) Proxy.newProxyInstance(getClass().getClassLoader(), new Class[]{Local1000SectionDao.class}, (proxy, method, args) -> {

            if (method.getName().equals("hashCode")) {
                return "Local1000SectionDao".hashCode();
            }

            Class[] argClazzs = null;
            if (args != null) {
                argClazzs = new Class[args.length];

                for (int i = 0; i < args.length; i++) {
                    argClazzs[i] = args[i].getClass();
                }

            }

            if (method.getName().equals("findAll")) {
                if (args[0] instanceof Specification) {
                    argClazzs[0] = Specification.class;
                }
            }

            Local1000SectionRepo target = ApplicationContextProvider.getBean(Local1000SectionRepo.class);

            method = Local1000SectionRepo.class.getMethod(method.getName(), argClazzs);
            if (method == null) {
                throw new RuntimeException("method not found");
            }
            return method.invoke(target, args);
        });

        return local1000SectionDao;
    }

}
