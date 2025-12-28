package org.nanking.knightingal.controller;

import org.springframework.web.bind.annotation.RestController;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.UUID;

import org.springframework.core.env.Environment;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
public class VideoController {

  @Resource
  private Environment environment;

  @GetMapping("/video.mp4")
  public void getMethodName(HttpServletRequest request, HttpServletResponse response) throws IOException {

    File file = new File(environment.getProperty("DEMO_VIDEO"));
    String ifRangeHeader = request.getHeader("If-Range");
    String rangeHeader = request.getHeader("Range");
    String etag = UUID.randomUUID().toString();
    if (!ObjectUtils.isEmpty(ifRangeHeader)) {
      etag = ifRangeHeader;
    }
    long fileSize = file.length();
    long start = 0;
    long end = fileSize - 1;
    if (!ObjectUtils.isEmpty(rangeHeader)) {
      String rangeValue = rangeHeader.split("=")[1];
      String[] rangeVauleArray = rangeValue.split("-");
      start = Long.parseLong(rangeVauleArray[0]);
      if (rangeVauleArray.length > 1) {
        end = Long.parseLong(rangeVauleArray[1]);
      }
      response.setHeader("Content-Range", String.format("bytes %d-%d/%d", start, end, fileSize));
      response.setStatus(206);
    } else {
      response.setStatus(200);
    }
    response.setHeader("Content-Lenght", String.format("%d", fileSize - start));
    response.setHeader("Content-Type", "video/mp4");

    response.setHeader("etag", etag);
    FileInputStream is = new FileInputStream(file);
    OutputStream os = response.getOutputStream();
    byte[] buffer = new byte[1024];
    is.skip(start);
    while (true) {
      int readLen = is.read(buffer);
      if (readLen > 0) {
        os.write(buffer, 0, readLen);
        os.flush();
      } else {
        break;
      }
    }
    is.close();
  }
}
