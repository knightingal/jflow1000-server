package org.nanking.knightingal;


import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.jupiter.api.Test;

public class SocketTest {

  private static final Log log = LogFactory.getLog(SocketTest.class);
  private final static String GET_REQUET_CONTENT = "GET /examples/servlets/servlet/HelloWorldExample HTTP/1.1\r\n" + //
        "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7\r\n" + //
        "Accept-Encoding: gzip, deflate, br, zstd\r\n" + //
        "Accept-Language: zh-CN,zh;q=0.9,en;q=0.8,en-GB;q=0.7,en-US;q=0.6,zh-TW;q=0.5\r\n" + //
        "Cache-Control: max-age=0\r\n" + //
        "Connection: keep-alive\r\n" + //
        "Host: localhost:8080\r\n" + //
        "Sec-Fetch-Dest: document\r\n" + //
        "Sec-Fetch-Mode: navigate\r\n" + //
        "Sec-Fetch-Site: none\r\n" + //
        "Sec-Fetch-User: ?1\r\n" + //
        "Upgrade-Insecure-Requests: 1\r\n" + //
        "User-Agent: Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/135.0.0.0 Safari/537.36 Edg/135.0.0.0\r\n" + //
        "sec-ch-ua: \"Microsoft Edge\";v=\"135\", \"Not-A.Brand\";v=\"8\", \"Chromium\";v=\"135\"\r\n" + //
        "sec-ch-ua-mobile: ?0\r\n" + //
        "sec-ch-ua-platform: \"Linux\"\r\n\r\n";

  private final static String POST_REQUET_CONTENT = "POST /examples/servlets/servlet/ChunkExample HTTP/1.1\r\n" + //
        "Accept: */*\r\n" + //
        "Accept-Encoding: gzip, deflate, br, zstd\r\n" + //
        "Accept-Language: zh-CN,zh;q=0.9,en;q=0.8,en-GB;q=0.7,en-US;q=0.6,zh-TW;q=0.5\r\n" + //
        "Connection: keep-alive\r\n" + //
        "Content-Length: 10000\r\n" + //
        "Content-Type: application/json\r\n" + //
        "Host: localhost:8080\r\n" + //
        "Origin: http://localhost:8080\r\n" + //
        "Referer: http://localhost:8080/examples/servlets/servlet/ChunkExample\r\n" + //
        "Sec-Fetch-Dest: empty\r\n" + //
        "Sec-Fetch-Mode: cors\r\n" + //
        "Sec-Fetch-Site: same-origin\r\n" + //
        "User-Agent: Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/135.0.0.0 Safari/537.36 Edg/135.0.0.0\r\n" + //
        "sec-ch-ua: \"Microsoft Edge\";v=\"135\", \"Not-A.Brand\";v=\"8\", \"Chromium\";v=\"135\"\r\n" + //
        "sec-ch-ua-mobile: ?0\r\n" + //
        "sec-ch-ua-platform: \"Linux\"\r\n\r\n";

  // @Test
  public void socketGetTest() throws UnknownHostException, IOException {

    log.info("start");
    try (Socket socket = new Socket("localhost", 8080)) {
      socket.setSoTimeout(10 * 1000);
      OutputStream outputStream = socket.getOutputStream();
      outputStream.write(GET_REQUET_CONTENT.getBytes());
      outputStream.flush();

      InputStream inputStream = socket.getInputStream();
      byte[] buff = new byte[1024];
      while (true) {
        try {
          int readLen = inputStream.read(buff);
          if (readLen <= 0) {
            log.info("disconnect");
            break;
          }
          System.out.print(new String(buff, 0, readLen));

        } catch (IOException e) {
            log.info("timeout");
            break;

        }
        
      }
    }

  }

  // @Test
  public void socketPostTest() throws UnknownHostException, IOException, InterruptedException {

    log.info("start");
    try (Socket socket = new Socket("localhost", 8080)) {
      socket.setSoTimeout(90 * 1000);
      final Socket socketLocal = socket;

      Thread thread = new Thread(new Runnable() {

        @Override
        public void run() {
          InputStream inputStream;
          try {
            inputStream = socketLocal.getInputStream();
            byte[] buff = new byte[1024];
            while (true) {
              try {
                int readLen = inputStream.read(buff);
                if (readLen <= 0) {
                  log.info("disconnect");
                  break;
                }
                log.info(new String(buff, 0, readLen));

              } catch (IOException e) {
                  log.info("timeout");
                  break;

              }
              
            }
          } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
          }
        }
        
      });
      thread.start();
      OutputStream outputStream = socket.getOutputStream();
      outputStream.write(POST_REQUET_CONTENT.getBytes());
      for (int i = 0; i < 5; i++) {
        Thread.sleep(2 * 1000);
        outputStream.write("{\"key22\":\"value22\"}".getBytes());
        outputStream.flush();
      } 

      thread.join();

    }

  }
}
