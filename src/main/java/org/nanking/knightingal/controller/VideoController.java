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
import org.springframework.web.bind.annotation.GetMapping;


@RestController
public class VideoController {

  @Resource
  private Environment environment;

  @GetMapping("/video.mp4")
  public void getMethodName(HttpServletRequest request, HttpServletResponse response) throws IOException {

    
    File file = new File(environment.getProperty("DEMO_VIDEO"));
    response.setStatus(200);
    long fileSize = file.length();
    response.setHeader("Content-Lenght", String.format("%d", fileSize));
    response.setHeader("Content-Type", "video/mp4");
    String etag = UUID.randomUUID().toString();
    response.setHeader("etag", etag);
    long start = 0;
    long end = fileSize - 1;
    FileInputStream is = new FileInputStream(file);
    OutputStream os = response.getOutputStream();
    byte[] buffer = new byte[1024];
    is.skip(start);
    while (true) {
      int readLen = is.read(buffer);
      if (readLen > 0) {
        os.write(buffer, 0, readLen);
      } else {
        break;
      }
    }
    is.close();





  }
  

}
