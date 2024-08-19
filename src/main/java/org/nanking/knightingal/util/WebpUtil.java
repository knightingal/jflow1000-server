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
    if (!vp8.equals("VP8 ") && !vp8.equals("VP8X")) {
      throw new Exception("unexpected vp8:" + vp8);
    }

    if (vp8.equals("VP8 ")) {
      imageStream.readNBytes(4);

      byte[] data = new byte[0x10];
      readLen = imageStream.read(data);
      if (readLen != 0x10) {
        throw new Exception("read file failed, read header len:" + readLen);
      }
      int data6 = (int)data[6] & 0xff;
      int data7 = (int)data[7] & 0xff;
      int data8 = (int)data[8] & 0xff;
      int data9 = (int)data[9] & 0xff;
      
      int w = ((data7 << 8) | data6) & 0x3fff;
      int h = ((data9 << 8) | data8) & 0x3fff;

      WebpImageSize size = new WebpImageSize();
      size.width = w;
      size.height = h;
      return size;
    } else {
      byte[] data = new byte[20];
      readLen = imageStream.read(data);
      if (readLen != 20) {
        throw new Exception("read file failed, read header len:" + readLen);
      }
      int data12 = (int)data[8] & 0xff;
      int data13 = (int)data[9] & 0xff;
      int data14 = (int)data[10] & 0xff;

      int data15 = (int)data[11] & 0xff;
      int data16 = (int)data[12] & 0xff;
      int data17 = (int)data[13] & 0xff;
      int width = 1 + (data14 << 16 | data13 << 8 | data12);
      int height = 1 + (data17 << 16 | data16 << 8 | data15);
      WebpImageSize size = new WebpImageSize();
      size.width = width;
      size.height = height;
      return size;

    }
  }

  public static class WebpImageSize {
    public int height;
    public int width;
  }

  
}
