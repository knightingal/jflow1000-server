package org.nanking.knightingal;


import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;

import org.junit.jupiter.api.Test;
import org.nanking.knightingal.util.QrCodeUtil;

import com.google.zxing.WriterException;

public class QrCodeUtilTest {
    @Test
    public void testGenerateQrCode() throws WriterException, IOException {
        String fileName = QrCodeUtil.generateQrCode("hello");
        assertEquals(fileName, "./qrcode.png");

    }
}
