package org.nanking.knightingal.runnable;

import okhttp3.*;
import okio.Buffer;
import okio.BufferedSource;
import okio.ForwardingSource;
import okio.RealBufferedSource;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.nanking.knightingal.dao.ShipImgDetailDao;
import org.nanking.knightingal.ship.ShipImgDetail;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class ShipDownloadRunnable implements Runnable {

    private ShipImgDetailDao shipImgDetailDao;
    private ShipImgDetail shipImgDetail;
    private String targetFilePath;

    public ShipDownloadRunnable(ShipImgDetailDao shipImgDetailDao, ShipImgDetail shipImgDetail, String targetFilePath) {
        super();
        this.shipImgDetailDao = shipImgDetailDao;
        this.shipImgDetail = shipImgDetail;
        this.targetFilePath = targetFilePath;
    }

    @Override
    public void run() {
        OkHttpClient okHttpClient = makeClient();
        Request request = new Request.Builder()
                .url(this.shipImgDetail.getImgUrl())
                .build();
        try(Response response = okHttpClient.newCall(request).execute()) {
            ResponseBody body = response.body();
            byte[] contentBytes = body.bytes();
            File targetFile = new File(targetFilePath);
            if (!targetFile.createNewFile()) {
                throw new Exception("failed to create target file:" + targetFilePath);
            }
            try (FileOutputStream fileOutputStream = new FileOutputStream(targetFile)) {
                fileOutputStream.write(contentBytes);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private OkHttpClient makeClient() {
        return new OkHttpClient.Builder().addNetworkInterceptor(new Interceptor() {
            @NotNull
            @Override
            public Response intercept(@NotNull Chain chain) throws IOException {
                Response originResponse = chain.proceed(chain.request());
                ResponseBody body = originResponse.body();
                ResponseBody wrappedBody = new ResponseBodyListener(body, new DownloadCounterListener() {

                    @Override
                    public void update(long current, long max) {

                    }
                });
                return originResponse.newBuilder().body(wrappedBody).build();
            }
        }).readTimeout(120, TimeUnit.SECONDS).build();
    }

    interface DownloadCounterListener {
        void update(long current, long max);
    }

    static class ResponseBodyListener extends ResponseBody {
        public ResponseBodyListener(ResponseBody origin, DownloadCounterListener downloadCounterListener) {
            this.origin = origin;
            this.downloadCounterListener = downloadCounterListener;
        }

        private ResponseBody origin;
        private DownloadCounterListener downloadCounterListener;
        private BufferedSource bufferedSource = null;

        @Override
        public long contentLength() {
            return origin.contentLength();
        }

        @Nullable
        @Override
        public MediaType contentType() {
            return origin.contentType();
        }

        @NotNull
        @Override
        public BufferedSource source() {
            if (bufferedSource == null) {
                bufferedSource = new RealBufferedSource(
                    source(origin.source())
                );
            }
            return bufferedSource;
        }

        private okio.Source source(okio.Source source) {
            return new ForwardingSource(source) {
                @Override
                public long read(@NotNull Buffer sink, long byteCount) throws IOException {
                    long read = super.read(sink, byteCount);
                    System.out.println("bytesRead:" + read);
                    if (read >= 0) {
                        downloadCounterListener.update(read, 0);
                    }
                    return read;
                }
            };
        }
    }
}
