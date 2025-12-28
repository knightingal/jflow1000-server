package org.nanking.knightingal;

import okhttp3.OkHttpClient;
import org.nanking.knightingal.dao.Local1000AlbumConfigDao;
import org.nanking.knightingal.dao.Local1000ApkConfigDao;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.nanking.knightingal.dao.Local1000SectionDao;
import org.nanking.knightingal.util.EncryptUtil;
import org.nanking.knightingal.util.FileUtil;
import org.nanking.knightingal.util.TimeUtil;

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

  public static final String pattern = "yyyyMMddHHmmss";

  // private static final Log log = LogFactory.getLog(AppConfiguration.class);
  @Bean
  public TimeUtil timeUtil() {
    return new TimeUtil();
  }

  @Bean
  public DateFormat fmt() {
    return new SimpleDateFormat(pattern);
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
    return DaoInjector.injectDaoToRepo(Local1000SectionDao.class);
  }

  @Bean("local1000AlbumConfigDao")
  public Local1000AlbumConfigDao local1000AlbumConfigDao() {
    return DaoInjector.injectDaoToRepo(Local1000AlbumConfigDao.class);
  }

  @Bean("local1000ApkConfigDao")
  public Local1000ApkConfigDao local1000ApkConfigDao() {
    return DaoInjector.injectDaoToRepo(Local1000ApkConfigDao.class);
  }

}
