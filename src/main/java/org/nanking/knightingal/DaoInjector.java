package org.nanking.knightingal;

import lombok.extern.slf4j.Slf4j;
import org.nanking.knightingal.annotation.Repo;
import org.nanking.knightingal.util.ApplicationContextProvider;
import org.springframework.data.jpa.domain.Specification;

import java.lang.reflect.Proxy;

@Slf4j
public class DaoInjector {

    public static <T> T injectDaoToRepo(Class<T> dao) {
        Repo annotation = dao.getAnnotation(Repo.class);

        String[] value = annotation.value();

        if (value.length == 0) {
            return null;
        }

        String repoBeanName = value[0];

        Object proxyObj = Proxy.newProxyInstance(dao.getClassLoader(), new Class[]{dao}, (proxy, method, args) -> {
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
        });

        return (T) proxyObj;
    }
}
