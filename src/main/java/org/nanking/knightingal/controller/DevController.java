package org.nanking.knightingal.controller;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.nanking.knightingal.util.ApplicationContextProvider;
import org.nanking.knightingal.util.EncryptUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author Knightingal
 */
@RestController
@RequestMapping("/dev")
public class DevController {

  private static final Logger LOG = LogManager.getLogger(DevController.class);

  @Value("${encrypt-util.passwd}")
  private String passwd;

  @RequestMapping("/passwd")
  public String getPasswd() {
    return passwd;
  }

  @Value("${encrypt-util.sample-encrypted-pic-path:/mnt/linux1000/encrypted/20160318000005BB-36_USS_NEVADA/46-013759.jpg.bin}")
  private String sampleEncryptedPicPath;

  @Value("${encrypt-util.sample-pic-path:/mnt/linux1000/source/20160318000005BB-36_USS_NEVADA/46-013759.jpg}")
  private String samplePicPath;

  @PostMapping("/image-upload")
  public void imageUpload(@RequestBody byte[] entity, HttpServletResponse servletResponse) throws IOException {
    LOG.debug("image length: {}", entity.length);

    String filePath = "" + new Date().getTime() + "46-013759.jpg";
    try (FileOutputStream fileOutputStream = new FileOutputStream(filePath)) {
      fileOutputStream.write(entity);
    }

    servletResponse.setStatus(200);
  }

  @GetMapping("/gen-aes-image")
  public void getAesImage() throws IOException {
    String path = samplePicPath;
    EncryptUtil encryptUtil = (EncryptUtil) ApplicationContextProvider.getBean("encryptUtil");
    FileInputStream fileInputStream = new FileInputStream(path);
    byte[] content = fileInputStream.readAllBytes();
    byte[] encrypted = encryptUtil.encrypt(content);
    fileInputStream.close();

    FileOutputStream outputStream = new FileOutputStream("./encrypted.bin");
    outputStream.write(encrypted);
    outputStream.close();

  }

  @GetMapping("/aes-test")
  public void aesTestHandle(HttpServletResponse servletResponse) throws IOException {
    EncryptUtil encryptUtil = (EncryptUtil) ApplicationContextProvider.getBean("encryptUtil");
    servletResponse.addHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, "*");
    byte[] encrypt64 = encryptUtil.encrypt("1234567890abcdef1234567890abcdef1234567890abcdef".getBytes());
    StringBuilder sb = new StringBuilder();
    for (byte b : encrypt64) {
      sb.append(String.format("%02x", b));
    }
    LOG.debug(sb);
    servletResponse.getOutputStream().write(sb.toString().getBytes());
  }

  @GetMapping("/aes-image")
  public void aseImageHandle(HttpServletResponse servletResponse) throws IOException {
    servletResponse.addHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, "*");
    ServletOutputStream outputStream = servletResponse.getOutputStream();
    String filePath = sampleEncryptedPicPath;
    try (FileInputStream fileInputStream = new FileInputStream(filePath)) {
      fileInputStream.transferTo(outputStream);
    }
  }

  @GetMapping("/image")
  public void imageHandle(HttpServletResponse servletResponse) throws IOException {
    servletResponse.addHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, "*");
    ServletOutputStream outputStream = servletResponse.getOutputStream();
    String filePath = samplePicPath;
    try (FileInputStream fileInputStream = new FileInputStream(filePath)) {
      fileInputStream.transferTo(outputStream);
    }
  }
}
