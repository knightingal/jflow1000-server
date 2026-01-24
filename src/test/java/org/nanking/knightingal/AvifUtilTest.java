package org.nanking.knightingal;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;
import java.io.IOException;

import org.junit.jupiter.api.Test;
import org.nanking.knightingal.util.AvifUtil;
import org.nanking.knightingal.util.AvifUtil.ImgSize;

public class AvifUtilTest {
  
  @Test
  public void testAvif() throws IOException {
    ImgSize imgSize = AvifUtil.parseImgSize(new File("src/test/hato.profile0.10bpc.yuv420.avif"));
    assertEquals(imgSize.getHeight(), 2048);
    assertEquals(imgSize.getWidth(), 3078);

  }

  @Test
  public void testPng() throws IOException {
    ImgSize imgSize = AvifUtil.parsePngSize(new File("/home/knightingal/Pictures/Screenshots/ScreenshotFrom2026-01-2421-51-29.png"));
    assertEquals(imgSize.getHeight(), 635);
    assertEquals(imgSize.getWidth(), 770);
  }

  @Test
  public void testJpg() throws IOException {
    ImgSize imgSize = AvifUtil.parseJpgSize(new File("/home/knightingal/Pictures/llqdfm.jpg"));
    assertEquals(imgSize.getHeight(), 1920);
    assertEquals(imgSize.getWidth(), 1278);
  }
}
