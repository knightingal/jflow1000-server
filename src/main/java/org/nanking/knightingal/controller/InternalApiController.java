package org.nanking.knightingal.controller;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@RestController
public class InternalApiController {

  /**
   * call request to fetch asset from static.makerfac.com for scratch
   * TODO: store and check asset locally
   * @param packageId
   * @return
   * @throws IOException
   */
  @GetMapping("/internalapi/asset/{assetId}")
  public ResponseEntity<byte[]> index(@PathVariable("assetId") String packageId) throws IOException {
    String url = "https://static.makerfac.com/static/internalapi/asset/" + packageId;

        Request request = new Request.Builder().url(url).
                addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36").
                addHeader("Connection", "keep-alive").
                addHeader("Accept", "image/webp,image/*,*/*;q=0.8").
                addHeader("Accept-Encoding", "gzip,deflate,sdch").
                addHeader("Accept-Language", "zh-CN,zh;q=0.8").
                addHeader("Pragma","no-cache").
                addHeader("referer", "https://community.makerfac.com/")
                addHeader("Cache-Control","no-cache").
                build();
    OkHttpClient okHttpClient = new OkHttpClient.Builder()
          .connectTimeout(60, TimeUnit.SECONDS)
          .readTimeout(60, TimeUnit.SECONDS).build();

    Response response = okHttpClient.newCall(request).execute();
    byte[] bytes = response.body().bytes();
    response.body().close();

    return ResponseEntity.ok()
      .header("content-type", response.headers().get("content-type"))    
      .body(bytes);
  }
}
