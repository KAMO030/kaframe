package com.kamo.core.util;

import java.io.File;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public final class StringUtils {
    private StringUtils() {
    }

    public static String subStringOnAfter(String value, String startValue) {
        return subStringOnAfter(value,startValue,value);
    }
    public static String subStringOnAfter(String value, String startValue, String defaultValue) {
        int startIndex = value.indexOf(startValue);
        return startIndex != -1 ? value.substring(startValue.length()).trim() : defaultValue;
    }
    public static String subStringOnBefore(String value, String endValue) {
        return subStringOnBefore(value,endValue,value);
    }
    public static String subStringOnBefore(String value, String endValue, String defaultValue) {
        int endIndex = value.indexOf(endValue);
        return endIndex != -1 ? value.substring(0,endIndex).trim() : defaultValue;
    }
    public static String replaceSeparators(String string) {
        return string.replace('/', '.').replace(File.separatorChar, '.');
    }
    public static String replaceFileSeparators(String string) {
        return string.replace( '.',File.separatorChar);
    }
    public static String getMD5Hash(String msg) {
        byte[] b = null;
        String md5Code = null;
        try {
            b = MessageDigest.getInstance("md5").digest(msg.getBytes());
            md5Code = new BigInteger(1, b).toString(16);
            for (int i = 0; i < 32 - md5Code.length(); i++) {
                md5Code = "0" + md5Code;
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return md5Code;
    }
}
