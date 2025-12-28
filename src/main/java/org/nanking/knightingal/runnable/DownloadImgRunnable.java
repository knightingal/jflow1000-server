package org.nanking.knightingal.runnable;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nanking.knightingal.bean.Flow1000Img;
import org.nanking.knightingal.util.ApplicationContextProvider;
import org.nanking.knightingal.util.EncryptUtil;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.concurrent.CountDownLatch;

/**
 * @author Knightingal
 */
public class DownloadImgRunnable implements Runnable {

  private static final Log log = LogFactory.getLog(DownloadImgRunnable.class);

  private final static EncryptUtil ENCRYPT_UTIL = (EncryptUtil) ApplicationContextProvider.getBean("encryptUtil");

  private final static OkHttpClient CLIENT = (OkHttpClient) ApplicationContextProvider.getBean("client");

  private final String dirName;

  private final Flow1000Img flow1000Img;

  private final CountDownLatch countDownLatch;

  private final String baseDir;

  private final static int TRY_LOOP_COUNT = 64;

  public DownloadImgRunnable(Flow1000Img flow1000Img, String dirName, CountDownLatch countDownLatch, String baseDir) {
    this.dirName = dirName;
    this.flow1000Img = flow1000Img;
    this.countDownLatch = countDownLatch;
    this.baseDir = baseDir;
  }

  @Override
  public void run() {
    String fileName = flow1000Img.getName();

    log.info("start to download " + flow1000Img.getSrc() + " to dirName " + dirName);
    Request request = new Request.Builder().url(flow1000Img.getSrc()).addHeader("User-Agent",
        "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36")
        .addHeader("Connection", "keep-alive").addHeader("Accept", "image/webp,image/*,*/*;q=0.8")
        .addHeader("Accept-Encoding", "gzip,deflate,sdch").addHeader("Accept-Language", "zh-CN,zh;q=0.8")
        .addHeader("Referer", flow1000Img.getHref()).addHeader("Pragma", "no-cache")
        .addHeader("Cache-Control", "no-cache").build();
    for (int i = 0; i < TRY_LOOP_COUNT; i++) {
      try {
        Response response = CLIENT.newCall(request).execute();
        byte[] respBytes = response.body().bytes();
        String absPath = baseDir + "\\source\\" + dirName + "\\";
        File dirFile = new File(absPath);
        dirFile.mkdirs();
        File file = new File(absPath + fileName);
        boolean createRet = file.createNewFile();
        if (!createRet) {
          log.error("cannot create " + absPath);
          return;
        }
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        fileOutputStream.write(respBytes);
        fileOutputStream.close();

        BufferedImage sourceImg = ImageIO.read(new FileInputStream(file));
        int width = sourceImg.getWidth();
        int height = sourceImg.getHeight();
        log.info("file name:" + fileName + " width:" + width + " height:" + height);

        byte[] encryptedBytes = ENCRYPT_UTIL.encrypt(respBytes);
        absPath = baseDir + "\\encrypted\\" + dirName + "\\";

        dirFile = new File(absPath);
        dirFile.mkdirs();
        file = new File(absPath + fileName + ".bin");
        createRet = file.createNewFile();
        if (!createRet) {
          log.error("cannot create " + absPath);
          return;
        }
        fileOutputStream = new FileOutputStream(file);
        fileOutputStream.write(encryptedBytes);
        fileOutputStream.close();
        this.flow1000Img.setWidth(width);
        this.flow1000Img.setHeight(height);
        break;
      } catch (Exception e) {
        log.warn("download " + flow1000Img.getSrc() + " failed, try again...");
      }
    }

    log.info(flow1000Img.getSrc() + " download end");
    countDownLatch.countDown();
  }
}
