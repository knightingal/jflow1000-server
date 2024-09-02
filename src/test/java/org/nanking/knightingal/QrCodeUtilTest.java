package org.nanking.knightingal;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.junit.jupiter.api.Test;
import org.nanking.knightingal.util.WebpUtil;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.zxing.WriterException;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class QrCodeUtilTest {
    @Test
    public void testGenerateQrCode() throws WriterException, IOException {

    }

    @Test
    public void testParseWebpImage() {
      File file = new File("/mnt/linux1000/1807/some_dir/35.webp");
      try {
        InputStream inputStream = new FileInputStream(file);
        WebpUtil.parseWebpImage(inputStream);
        inputStream.close();

      } catch (Exception e) {
        e.printStackTrace();

      }

    }
    
    @Test
    public void testParseJpg() {

      try {
        BufferedImage sourceImg = ImageIO.read(Files.newInputStream(Path.of(
          "/mnt/linux/1803/some_dir/036-09155561.jpg")));
        int width = sourceImg.getWidth();
        int height = sourceImg.getHeight();

      } catch (Exception e) {
        e.printStackTrace();
      }
    }

    @Test
    public void parseRGB24() throws Exception {
      File rgbFile = new File("/home/knightingal/Videos/Screencasts/Screencast.bin");
      int width = 1920;
      int height = 1080;
      InputStream inputStream = new FileInputStream(rgbFile);
      List<List<Integer>> colorMatrix = new ArrayList<>();
      for (int i = 0; i < 200; i++) {
        List<Integer> colorLine = new ArrayList<>();
        for (int j = 0; j < width; j++) {
          byte[] pix = new byte[3];
          inputStream.read(pix);
          int r = (int)pix[0] & 0xff;
          int g = (int)pix[1] & 0xff;
          int b = (int)pix[2] & 0xff;
          int color = r << 16 | g << 8 | b;
          colorLine.add(color);
        }
        colorMatrix.add(colorLine);
      }
      inputStream.close();

      ObjectMapper objectMapper = new ObjectMapper();
      objectMapper.writeValue(new File("color.json"), colorMatrix);


    }
}
