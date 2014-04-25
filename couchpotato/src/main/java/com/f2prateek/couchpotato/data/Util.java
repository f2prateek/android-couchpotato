package com.f2prateek.couchpotato.data;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Util {
  private Util() {
    // no instances
  }

  public static String md5(String s) {
    String md5 = null;
    if (null == s) return null;
    try {
      MessageDigest digest = MessageDigest.getInstance("MD5");
      digest.update(s.getBytes(), 0, s.length());
      //Converts message digest value in base 16 (hex)
      md5 = new BigInteger(1, digest.digest()).toString(16);
    } catch (NoSuchAlgorithmException e) {
      e.printStackTrace();
    }
    return md5;
  }
}
