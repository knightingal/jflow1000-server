package org.nanking.knightingal;


import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.junit.jupiter.api.Test;

public class AesTest {

    private String passwd = "0000000000000000"; // 16 bytes key for AES-128

    private String ivString = "2021000120210001";

    @Test
    public void encrypt() {
        byte[] iv = ivString.getBytes();

        byte[] dataBytes = "0123456789abcdef0123456789abcdef".getBytes();
        try {
            Cipher cipher = Cipher.getInstance("AES/CFB/NoPadding");
            int blockSize = cipher.getBlockSize();
            int length = dataBytes.length;
            if (length % blockSize != 0) {
                length = length + (blockSize - length % blockSize);
            }
            byte[] plaintext = new byte[length];
            System.arraycopy(dataBytes, 0, plaintext, 0, dataBytes.length);
            SecretKeySpec keySpec
                    = new SecretKeySpec(passwd.getBytes(), "AES");
            IvParameterSpec ivSpec = new IvParameterSpec(iv);
            cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);
            byte[] output = cipher.doFinal(plaintext);
            System.out.println("Encrypted data: " );
            for (byte b : output) {
                System.out.printf("%02x ", b);
            }
            System.out.println("" );
        } catch (Exception e) {
        }
    }

}
