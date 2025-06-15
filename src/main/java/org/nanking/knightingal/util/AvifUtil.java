package org.nanking.knightingal.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class AvifUtil {
  public static ImgSize parseImgSize(File avif) throws IOException {
    try (InputStream inputStream = new FileInputStream(avif)) {
      parseHeader(inputStream);
      ImgSize imgSize = parseMeta(inputStream);
      return imgSize;
    } catch (Exception e) {
      throw e;
    }
  }

  private static ImgSize parseMeta(InputStream inputStream) throws IOException{
    int size = read4Int(inputStream);
    String type = readStringBySize(inputStream, 4);
    if (!type.equals("meta")) {
      throw new IOException("not find meta");
    }

    int version = read4Int(inputStream);
    if (version != 0) {
      throw new IOException("invalid version");
    }

    while (true) {
      Header header = readHeader(inputStream);

      if (header.type.equals("hdlr")) {
        ignoreBySize(inputStream, header.size - 8);
      } else if (header.type.equals("iloc")) {
        ignoreBySize(inputStream, header.size - 8);
      } else if (header.type.equals("pitm")) {
        ignoreBySize(inputStream, header.size - 8);
      } else if (header.type.equals("idat")) {
        ignoreBySize(inputStream, header.size - 8);
      } else if (header.type.equals("iinf")) {
        ignoreBySize(inputStream, header.size - 8);
      } else if (header.type.equals("iprp")) {
        // ignoreBySize(inputStream, header.size - 8);
        // remain -= header.size - 8;
        return parseIprp(inputStream);
      }
    }


  }

  private static ImgSize parseIprp(InputStream inputStream) throws IOException {
    Header header = readHeader(inputStream);
    if (!header.type.equals("ipco")) {
      throw new IOException("not find ipco");
    }

    while (true) {
      header = readHeader(inputStream);
      if (header.type.equals("pasp")) {
        ignoreBySize(inputStream, header.size - 8);
      } else if (header.type.equals("colr")) {
        ignoreBySize(inputStream, header.size - 8);
      } else if (header.type.equals("av1C")) {
        ignoreBySize(inputStream, header.size - 8);
      } else if (header.type.equals("pasp")) {
        ignoreBySize(inputStream, header.size - 8);
      } else if (header.type.equals("clap")) {
        ignoreBySize(inputStream, header.size - 8);
      } else if (header.type.equals("irot")) {
        ignoreBySize(inputStream, header.size - 8);
      } else if (header.type.equals("ispe")) {
        ignoreBySize(inputStream, 4);
        int width = read4Int(inputStream);
        int height = read4Int(inputStream);
        // width = width;
        // height = height;
        return new ImgSize(height, width);

      }

      
    }

  }

  private static Header readHeader(InputStream inputStream) throws IOException {
    int size = read4Int(inputStream);
    String type = readStringBySize(inputStream, 4);
    return new Header(size, type);

  }

  private static int parseHeader(InputStream inputStream) throws IOException{
    int size = read4Int(inputStream);
    int remain = size;
    remain -= 4;
    String ftyp = readStringBySize(inputStream, 4);
    if (!ftyp.equals("ftyp")) {
      throw new IOException("not find ftyp");
    }
    remain -= 4;

    String ftypValue = readStringBySize(inputStream, 4);
    if (!ftypValue.equals("avif")) {
      throw new IOException("not avif ftyp");
    }
    remain -= 4;

    ignoreBySize(inputStream, remain);


    return size;
  }

  private static String readStringBySize(InputStream inputStream, int size) throws IOException {
    byte[] data = inputStream.readNBytes(size);
    return new String(data);
  }

  private static int read4Int(InputStream inputStream) throws IOException {
    byte[] data = inputStream.readNBytes(4);

    int data0 = (int)data[0] & 0xff;
    int data1 = (int)data[1] & 0xff;
    int data2 = (int)data[2] & 0xff;
    int data3 = (int)data[3] & 0xff;

    int value = (data0 << 24) | (data1 << 16) | (data2 << 8) | data3;
    return value;
  }

  private static void ignoreBySize(InputStream inputStream, int size) throws IOException {
    inputStream.readNBytes(size);
  }


  private static class Header {
    public Header(int size, String type) {
      this.type = type;
      this.size = size;
    }
    private final String type;
    private final int size;

  }

  public static class ImgSize {

    public ImgSize(int height, int width) {
      this.height = height;
      this.width = width;
    }
    private final int height;
    private final int width;

    public int getHeight() {
      return height;
    }
    public int getWidth() {
      return width;
    }

    
  }
}
