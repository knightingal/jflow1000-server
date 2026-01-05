package org.nanking.knightingal.util;

import java.io.IOException;
import java.io.InputStream;

import org.nanking.knightingal.exception.CommonException;

public class WebpUtil {
  private WebpUtil() {
  }
  public static WebpImageSize parseWebpImage(InputStream imageStream) throws CommonException, IOException {
    byte[] webpHeader = new byte[0x10];
    int readLen = imageStream.read(webpHeader);
    if (readLen != 0x10) {
      throw new CommonException("read file failed, read header len:" + readLen);
    }
    String riff = new String(webpHeader, 0, 4);
    if (!riff.equals("RIFF")) {
      throw new CommonException("unexpected header:" + riff);
    }
    String type = new String(webpHeader, 8, 4);
    if (!type.equals("WEBP")) {
      throw new CommonException("unexpected type:" + type);
    }
    String vp8 = new String(webpHeader, 12, 4);
    if (!vp8.equals("VP8 ") && !vp8.equals("VP8X")) {
      throw new CommonException("unexpected vp8:" + vp8);
    }

    if (vp8.equals("VP8 ")) {
      imageStream.readNBytes(4);

      byte[] data = new byte[0x10];
      readLen = imageStream.read(data);
      if (readLen != 0x10) {
        throw new CommonException("read file failed, read header len:" + readLen);
      }
      int data6 = data[6] & 0xff;
      int data7 = data[7] & 0xff;
      int data8 = data[8] & 0xff;
      int data9 = data[9] & 0xff;

      int w = ((data7 << 8) | data6) & 0x3fff;
      int h = ((data9 << 8) | data8) & 0x3fff;

      return new  WebpImageSize(h, w);
    } else {
      byte[] data = new byte[20];
      readLen = imageStream.read(data);
      if (readLen != 20) {
        throw new CommonException("read file failed, read header len:" + readLen);
      }
      int data12 = data[8] & 0xff;
      int data13 = data[9] & 0xff;
      int data14 = data[10] & 0xff;

      int data15 = data[11] & 0xff;
      int data16 = data[12] & 0xff;
      int data17 = data[13] & 0xff;
      int width = 1 + (data14 << 16 | data13 << 8 | data12);
      int height = 1 + (data17 << 16 | data16 << 8 | data15);
      return new WebpImageSize(height, width);

    }
  }

  public static class WebpImageSize {

    
    public WebpImageSize(int height, int width) {
      this.height = height;
      this.width = width;
    }

    public final int height;
    public final int width;
  }

}
