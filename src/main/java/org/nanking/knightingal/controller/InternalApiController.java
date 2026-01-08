package org.nanking.knightingal.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletResponse;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
public class InternalApiController {

  @Value("${internal-asset-store-path:/home/knightingal/Pictures/}")
  private String assetStorePath;

  private static final String REMOTE_ASSET_URL = "https://static.makerfac.com/static/internalapi/asset/";

  /**
   * call request to fetch asset from static.makerfac.com for scratch
   * 
   * @param packageId
   * @return
   * @throws IOException
   */
  @GetMapping("/internalapi/asset/{assetId}")
  public void index(@PathVariable("assetId") String packageId, HttpServletResponse servletResponse) throws IOException {
    File assetFile = new File(assetStorePath + packageId);
    String contentType = packageId.endsWith("svg") ? "image/svg+xml" : "image/png";
    servletResponse.addHeader(HttpHeaders.CONTENT_TYPE, contentType);
    if (assetFile.exists()) {
      servletResponse.setStatus(HttpStatus.OK.value());
      try (InputStream inputStream = new FileInputStream(assetFile)) {
        inputStream.transferTo(servletResponse.getOutputStream());
      }
      return;
    }

    String url = REMOTE_ASSET_URL + packageId;

    Request request = new Request.Builder().url(url)
        .addHeader("User-Agent", 
          "Mozilla/5.0 (Windows NT 10.0; Win64; x64) " +
          "AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36")
        .addHeader("Connection", "keep-alive")
        .addHeader("Accept", "image/webp,image/*,*/*;q=0.8")
        .addHeader("Accept-Encoding", "gzip,deflate,sdch")
        .addHeader("Accept-Language", "zh-CN,zh;q=0.8")
        .addHeader("Pragma", "no-cache")
        .addHeader("referer", "https://community.makerfac.com/")
        .addHeader("Cache-Control", "no-cache").build();
    OkHttpClient okHttpClient = new OkHttpClient.Builder()
        .connectTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS).build();

    Response response = okHttpClient.newCall(request).execute();
    byte[] bytes = response.body().bytes();
    response.body().close();
    if (assetFile.createNewFile()) {
      servletResponse.setStatus(HttpStatus.NOT_FOUND.value());
      return;
    }
    try (OutputStream outputStream = new FileOutputStream(assetFile)) {
      outputStream.write(bytes);
      outputStream.flush();
    }
    servletResponse.setStatus(HttpStatus.OK.value());
    servletResponse.getOutputStream().write(bytes);
  }
}
