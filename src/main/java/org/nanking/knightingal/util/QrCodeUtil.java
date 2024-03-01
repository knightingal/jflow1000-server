package org.nanking.knightingal.util;


import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;


public class QrCodeUtil {
    public static void generateQrCode(String content) {
        try {
            BitMatrix bitMatrix = setBitMatrix(content, 200, 200);
            

        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    private static BitMatrix setBitMatrix(String content, int width, int height) throws WriterException {
        BitMatrix bitMatrix = null;
        bitMatrix = new MultiFormatWriter().encode(
            content, BarcodeFormat.QR_CODE, width, height 
            );

        return bitMatrix;
    }
}
