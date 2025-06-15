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
}
