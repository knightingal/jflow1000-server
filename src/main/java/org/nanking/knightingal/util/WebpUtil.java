package org.nanking.knightingal.util;

import java.io.InputStream;

public class WebpUtil {
  public static WebpImageSize parseWebpImage(InputStream imageStream) throws Exception {
    byte[] webpHeader = new byte[0x10];
    int readLen = imageStream.read(webpHeader);
    if (readLen != 0x10) {
      throw new Exception("read file failed, read header len:" + readLen);
    }
    String riff = new String(webpHeader, 0, 4);
    if (!riff.equals("RIFF")) {
      throw new Exception("unexpected header:" + riff);
    }
    String type = new String(webpHeader, 8, 4);
    if (!type.equals("WEBP")) {
      throw new Exception("unexpected type:" + type);
    }
    String vp8 = new String(webpHeader, 12, 4);
    if (!vp8.equals("VP8 ")) {
      throw new Exception("unexpected vp8:" + vp8);
    }

    imageStream.readNBytes(4);

    byte[] data = new byte[0x10];
    readLen = imageStream.read(data);
    if (readLen != 0x10) {
      throw new Exception("read file failed, read header len:" + readLen);
    }
    
    int w = ((data[7] << 8) | data[6]) & 0x3fff;
    int h = ((data[9] << 8) | data[8]) & 0x3fff;

    WebpImageSize size = new WebpImageSize();
    size.width = w;
    size.height = h;
    return size;
  }

  public static class WebpImageSize {
    public int height;
    public int width;
  }

  
}
