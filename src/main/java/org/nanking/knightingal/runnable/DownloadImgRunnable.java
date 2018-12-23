package org.nanking.knightingal.runnable;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.nanking.knightingal.bean.Urls1000Body;
import org.nanking.knightingal.util.ApplicationContextProvider;
import org.nanking.knightingal.util.EncryptUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class DownloadImgRunnable implements Runnable {

    public DownloadImgRunnable(Urls1000Body.ImgSrcBean imgSrcBean, String dirName, String fileName) {
        this.imgSrcBean = imgSrcBean;
        this.dirName = dirName;
        this.fileName = fileName;
    }

    private EncryptUtil encryptUtil = (EncryptUtil) ApplicationContextProvider.getBean("encryptUtil");

    private final Urls1000Body.ImgSrcBean imgSrcBean;

    private final String dirName;

    private final String fileName;

    OkHttpClient client = new OkHttpClient();

    String run(String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .build();

        Response response = client.newCall(request).execute();
        return response.body().string();
    }

    @Override
    public void run() {


        System.out.println("start to download " + imgSrcBean.getSrc() + " to dirName " + dirName);
        Request request = new Request.Builder().url(imgSrcBean.getSrc()).
                addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36").
                addHeader("Connection", "keep-alive").
                addHeader("Accept", "image/webp,image/*,*/*;q=0.8").
                addHeader("Accept-Encoding", "gzip,deflate,sdch").
                addHeader("Accept-Language", "zh-CN,zh;q=0.8").
                addHeader("Referer", imgSrcBean.getRef()).
                addHeader("Pragma","no-cache").
                addHeader("Cache-Control","no-cache").
                build();
        try {
            Response response = client.newCall(request).execute();
            byte[] respBytes = response.body().bytes();
            String absPath = "/home/knightingal/download/linux1000/source/" + dirName + "/";
            File dirFile = new File(absPath);
            dirFile.mkdirs();
            File file = new File(absPath + fileName);
            boolean createRet = file.createNewFile();
            if (!createRet) {
                System.out.println("cannot create " + absPath);
                return;
            }
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write(respBytes);
            fileOutputStream.close();


            byte[] encryptedBytes = encryptUtil.encrypt(respBytes);
            absPath = "/home/knightingal/download/linux1000/encrypted/" + dirName + "/";
            dirFile = new File(absPath);
            dirFile.mkdirs();
            file = new File(absPath + fileName + ".bin");
            createRet = file.createNewFile();
            if (!createRet) {
                System.out.println("cannot create " + absPath);
                return;
            }
            fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write(encryptedBytes);
            fileOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


        System.out.println(imgSrcBean.getSrc() + " download end");
    }
}
