package org.nanking.knightingal.util;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;

import javax.imageio.ImageIO;

public class QrCodeUtil {
  private QrCodeUtil() {
  }

  public static void generateQrCode(String content, OutputStream outputStream) throws WriterException, IOException {
    BitMatrix bitMatrix = setBitMatrix(content, 200, 200);
    BufferedImage bufferedImage = MatrixToImageWriter.toBufferedImage(bitMatrix);
    ImageIO.write(bufferedImage, "png", outputStream);
  }

  private static BitMatrix setBitMatrix(String content, int width, int height) throws WriterException {
    Map<EncodeHintType, Object> param = Collections.synchronizedMap(new EnumMap<EncodeHintType, Object>(EncodeHintType.class));
    return new MultiFormatWriter().encode(
        content, BarcodeFormat.QR_CODE, width, height, param);
  }
}
