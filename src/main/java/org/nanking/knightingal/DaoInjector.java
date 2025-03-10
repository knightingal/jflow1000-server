package org.nanking.knightingal;

import lombok.extern.slf4j.Slf4j;
import org.nanking.knightingal.annotation.Repo;
import org.nanking.knightingal.util.ApplicationContextProvider;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.lang.reflect.Proxy;

@Slf4j
public class DaoInjector {

    @SuppressWarnings("unchecked")
    public static <T> T injectDaoToRepo(Class<T> dao) {
        Repo annotation = dao.getAnnotation(Repo.class);

        String[] value = annotation.value();

        if (value.length == 0) {
            throw new RuntimeException("not provide annotation value");
        }

        String repoBeanName = value[0];

        return (T) Proxy.newProxyInstance(dao.getClassLoader(), new Class[]{dao}, (proxy, method, args) -> {

            if (method.getName().equals("hashCode")) {
                return (repoBeanName + "-dao").hashCode();
            }

            Class<?>[] argClazzs = null;
            if (args != null) {
                argClazzs = new Class[args.length];

                for (int i = 0; i < args.length; i++) {
                    argClazzs[i] = args[i].getClass();
                }

                switch (method.getName()) {
                    case "findAll", "findOne":
                        // 处理各种重载版本的findAll，
                        // 这里的入参经常是各种lambda的匿名类，直接getMethod会找不到
                        if (args[0] instanceof Specification) {
                            argClazzs[0] = Specification.class;
                        }
                        // wtf, 参数是继承类型也不能反射出来，垃了
                        if (args.length >= 2 && args[1] instanceof Pageable) {
                            argClazzs[1] = Pageable.class;
                        }
                        break;
                    case "saveAllAndFlush":
                        if (args[0] instanceof Iterable) {
                            argClazzs[0] = Iterable.class;
                        }
                        break;
                    case "saveAndFlush":
                        // 注意这里会有泛型擦除，不能直接那入参的类型去反射方法
                        argClazzs[0] = Object.class;
                        break;
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

    }
}
