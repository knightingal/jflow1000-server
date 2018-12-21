package org.nanking.knightingal.runnable;

import org.nanking.knightingal.bean.Urls1000Body;

public class DownloadImgRunnable implements Runnable {
    public DownloadImgRunnable(Urls1000Body.ImgSrcBean imgSrcBean, String dirName) {
        this.imgSrcBean = imgSrcBean;
        this.dirName = dirName;
    }

    private Urls1000Body.ImgSrcBean imgSrcBean;

    private String dirName;


    @Override
    public void run() {

        System.out.println("start to download " + imgSrcBean.getSrc() + " to dirName " + dirName);
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("download end");
    }
}
