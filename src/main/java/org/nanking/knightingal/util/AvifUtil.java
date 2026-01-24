package org.nanking.knightingal.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class AvifUtil {

  private AvifUtil() {
  }

  public static ImgSize parsePngSize(File png) throws IOException {
    try (InputStream inputStream = new FileInputStream(png)) {
      ignoreBySize(inputStream, 8);

      int chunkLength = read4Int(inputStream);
      String chunkType = readStringBySize(inputStream, 4);
      if (!chunkType.equals("IHDR")) {
        throw new IOException("not find IHDR");
      }

      int width = read4Int(inputStream);
      int height = read4Int(inputStream);
      return new ImgSize(height, width);
    }  
  }

  public static ImgSize parseJpgSize(File jpg) throws IOException {
    try (InputStream inputStream = new FileInputStream(jpg)) {
      int b1 = inputStream.read();
      int b2 = inputStream.read();
      if (b1 != 0xff || b2 != 0xd8) {
        throw new IOException("not find jpg header");
      }

      while (true) {
        int ff = inputStream.read();
        if (ff != 0xff) {
          throw new IOException("expected 0xff marker");
        }
        int markerType = inputStream.read();
        if (markerType == -1) {
          throw new IOException("unexpected end of file");
        }

        while (markerType == 0xff) {
          markerType = inputStream.read();
        }

        boolean isSof = (markerType >= 0xc0 && markerType <= 0xcf) 
            && markerType != 0xc4 
            && markerType != 0xc8 
            && markerType != 0xcc;

        if (isSof) {
          int lengthHigh = inputStream.read();
          int lengthLow = inputStream.read();

          inputStream.read();

          int heightHigh = inputStream.read();
          int heightLow = inputStream.read();
          int height = (heightHigh << 8) | heightLow;

          int widthHigh = inputStream.read();
          int widthLow = inputStream.read();
          int width = (widthHigh << 8) | widthLow;
          return new ImgSize(height, width);
        } else if (markerType == 0xd9) {
          throw new IOException("Reached end of image without finding size");
        } else if (markerType == 0xd8) {
          continue;

        } else if (markerType >= 0xD0 && markerType <= 0xD7) {
          continue;
        } else if (markerType == 0x01 || markerType == 0x00) {
          continue;

        } else {
          int lengthHigh = inputStream.read();
          int lengthLow = inputStream.read();
          int length = (lengthHigh << 8) | lengthLow;
          ignoreBySize(inputStream, length - 2);
        }

      }
    }
  }


  public static ImgSize parseImgSize(File avif) throws IOException {
    try (InputStream inputStream = new FileInputStream(avif)) {
      parseHeader(inputStream);
      ImgSize imgSize = parseMeta(inputStream);
      return imgSize;
    } catch (Exception e) {
      throw e;
    }
  }

  private static ImgSize parseMeta(InputStream inputStream) throws IOException {
    int size = read4Int(inputStream);
    if (size < 0) {
      throw new IOException("invalid meta size");
    }
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
      } else if (header.type.equals("clap")) {
        ignoreBySize(inputStream, header.size - 8);
      } else if (header.type.equals("irot")) {
        ignoreBySize(inputStream, header.size - 8);
      } else if (header.type.equals("ispe")) {
        ignoreBySize(inputStream, 4);
        int width = read4Int(inputStream);
        int height = read4Int(inputStream);
        return new ImgSize(height, width);
      }

    }

  }

  private static Header readHeader(InputStream inputStream) throws IOException {
    int size = read4Int(inputStream);
    String type = readStringBySize(inputStream, 4);
    return new Header(size, type);

  }

  private static int parseHeader(InputStream inputStream) throws IOException {
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

    int data0 = data[0] & 0xff;
    int data1 = data[1] & 0xff;
    int data2 = data[2] & 0xff;
    int data3 = data[3] & 0xff;

    return (data0 << 24) | (data1 << 16) | (data2 << 8) | data3;
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
