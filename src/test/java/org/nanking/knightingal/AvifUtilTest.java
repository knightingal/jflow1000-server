package org.nanking.knightingal;

import java.io.File;
import java.io.IOException;

import org.junit.jupiter.api.Test;
import org.nanking.knightingal.util.AvifUtil;

public class AvifUtilTest {
  
  @Test
  public void testAvif() throws IOException {
    AvifUtil.parse(new File("/home/knightingal/Downloads/hato.profile0.10bpc.yuv420.avif"));
  }
}
