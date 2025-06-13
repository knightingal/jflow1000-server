package org.nanking.knightingal.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class AvifUtil {
  public static void parse(File avif) throws IOException {
    InputStream inputStream = new FileInputStream(avif);
    parseHeader(inputStream);
    

  }

  private static void parseHeader(InputStream inputStream) throws IOException{
    int size = read4Int(inputStream);
    size = size;
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
}
