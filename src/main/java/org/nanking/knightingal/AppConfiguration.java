package org.nanking.knightingal;

import okhttp3.OkHttpClient;
import org.nanking.knightingal.annotation.Repo;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.domain.Specification;

import lombok.extern.slf4j.Slf4j;

import org.nanking.knightingal.dao.Local1000SectionDao;
import org.nanking.knightingal.dao.jpa.Local1000SectionRepo;
import org.nanking.knightingal.util.ApplicationContextProvider;
import org.nanking.knightingal.util.EncryptUtil;
import org.nanking.knightingal.util.FileUtil;
import org.nanking.knightingal.util.TimeUtil;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
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
@Slf4j
public class AppConfiguration {

    // private static final Log log = LogFactory.getLog(AppConfiguration.class);
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


    private static <T> T getDao(Class<T> dao) {
        Repo annotation = dao.getAnnotation(Repo.class);

        String[] value = annotation.value();

        if (value.length == 0) {
            return null;
        }

        String repoBeanName = value[0];

        Object proxy = Proxy.newProxyInstance(dao.getClassLoader(), new Class[]{dao}, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                log.error("call method{}", method.getName());

                if (method.getName().equals("hashCode")) {
                    return "Local1000SectionDao".hashCode();
                }

                Class<?>[] argClazzs = null;
                if (args != null) {
                    argClazzs = new Class[args.length];

                    for (int i = 0; i < args.length; i++) {
                        argClazzs[i] = args[i].getClass();
                    }

                    // 处理各种重载版本的findAll，
                    // 这里的入参经常是各种lambda的匿名类，直接getMethod会找不到
                    if (method.getName().equals("findAll")) {
                        if (args[0] instanceof Specification) {
                            argClazzs[0] = Specification.class;
                        }
                    } else if (method.getName().equals("saveAllAndFlush")) {
                        if (args[0] instanceof Iterable) {
                            argClazzs[0] = Iterable.class;
                        }

                    }

                }

                Object target = ApplicationContextProvider.getBean(repoBeanName);

                try {
                    method = target.getClass().getMethod(method.getName(), argClazzs);
                } catch (NoSuchMethodException e) {
                    log.error("method not found", e);
                    throw new RuntimeException("method not found");
                }

                return method.invoke(target, args);
            }
        });

        return (T) proxy;
    }

    @Bean("local1000SectionDao")
    public Local1000SectionDao local1000SectionDao() {
        return getDao(Local1000SectionDao.class);
    }

}
