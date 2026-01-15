package org.nanking.knightingal.util;

import org.springframework.beans.factory.annotation.Value;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * @author Knightingal
 */
public class EncryptUtil {

  @Value("${encrypt-util.iv}")
  private String ivString;

  @Value("${encrypt-util.passwd}")
  private String passwd;

  public byte[] encrypt(byte[] dataBytes) {
    byte[] iv = ivString.getBytes();
    try {
      Cipher cipher = Cipher.getInstance("AES/CFB/NoPadding");
      int blockSize = cipher.getBlockSize();
      int length = dataBytes.length;
      if (length % blockSize != 0) {
        length = length + (blockSize - length % blockSize);
      }
      byte[] plaintext = new byte[length];
      System.arraycopy(dataBytes, 0, plaintext, 0, dataBytes.length);
      SecretKeySpec keySpec = new SecretKeySpec(passwd.getBytes(), "AES");
      IvParameterSpec ivSpec = new IvParameterSpec(iv);
      cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);
      return cipher.doFinal(plaintext);
    } catch (Exception e) {
      return new byte[0];
    }
  }

  public byte[] encrypt64(byte[] dataBytes) {
    byte[] iv = ivString.getBytes();
    try {
      Cipher cipher = Cipher.getInstance("AES/CFB64/NoPadding");
      int blockSize = cipher.getBlockSize();
      int length = dataBytes.length;
      if (length % blockSize != 0) {
        length = length + (blockSize - length % blockSize);
      }
      byte[] plaintext = new byte[length];
      System.arraycopy(dataBytes, 0, plaintext, 0, dataBytes.length);
      SecretKeySpec keySpec = new SecretKeySpec(passwd.getBytes(), "AES");
      IvParameterSpec ivSpec = new IvParameterSpec(iv);
      cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);
      return cipher.doFinal(plaintext);
    } catch (Exception e) {
      return new byte[0];
    }
  }
}
