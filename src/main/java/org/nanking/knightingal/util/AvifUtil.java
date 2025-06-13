package org.nanking.knightingal.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class AvifUtil {
  public static void parse(File avif) throws IOException {
    InputStream inputStream = new FileInputStream(avif);
    int size = parseHeader(inputStream);
    parseMeta(inputStream);
    

  }

  private static int parseMeta(InputStream inputStream) throws IOException{
    int size = read4Int(inputStream);
    int remain = size;
    remain -= 4;
    String ftyp = readStringBySize(inputStream, 4);
    if (!ftyp.equals("meta")) {
      throw new IOException("not find meta");
    }
    remain -= 4;


    ignoreBySize(inputStream, remain);


    return size;
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
}
