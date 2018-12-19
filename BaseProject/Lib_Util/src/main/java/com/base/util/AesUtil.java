package com.base.util;


import android.support.annotation.Nullable;
import android.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;


/**
 * Created by LZ on 2017/6/21.
 */

public class AesUtil {
    //    private final static String HEX = "0123456789ABCDEF";
//    private static final String CBC_PKCS5_PADDING = "AES/CBC/PKCS5Padding";//AES是加密方式 CBC是工作模式 PKCS5Padding是填充模式
    private static final String CBC_PKCS5_PADDING = "AES/CBC/NoPadding";//AES是加密方式 CBC是工作模式 PKCS5Padding是填充模式
    private static final String AES = "AES";//AES 加密
//    private static final String SHA1PRNG = "SHA1PRNG";//// SHA1PRNG 强随机种子算法, 要区别4.2以上版本的调用方法
    
    /**
     * @param key     位数必须是16的倍数
     * @param content
     *
     * @return
     */
    @Nullable
    public static String encrypt(String key, String content) throws Exception {
        if (key.length() % 16 != 0) {
            throw new Exception("key的长度必须为16的倍数");
        }
        try {
            Cipher aesECB = Cipher.getInstance("AES/ECB/PKCS5Padding");
            SecretKeySpec pass = new SecretKeySpec(key.getBytes(), "AES");
            aesECB.init(Cipher.ENCRYPT_MODE, pass);
            byte[] result = aesECB.doFinal(content.getBytes());
            return Base64.encodeToString(result, Base64.DEFAULT);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    /**
     * @param key     位数必须是16的倍数
     * @param content 使用{@link #encrypt(String, String)}加密过的base64字符串
     *
     * @return null 或者解码后的字符串
     */
    @Nullable
    public static String decrypt(String key, String content) {
        try {
            byte[] data = Base64.decode(content, Base64.DEFAULT);
            Cipher aesECB = null;
            aesECB = Cipher.getInstance("AES/ECB/PKCS5Padding");
            SecretKeySpec pass = new SecretKeySpec(key.getBytes(), "AES");
            aesECB.init(Cipher.DECRYPT_MODE, pass);
            byte[] result = aesECB.doFinal(data);
            return new String(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
//    @Nullable
//    public static String encrypt(String key, String data) {
//        try {
//            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
////            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
//            int blockSize = cipher.getBlockSize();
//
//            byte[] dataBytes = data.getBytes();
//            int plaintextLength = dataBytes.length;
//            if (plaintextLength % blockSize != 0) {
//                plaintextLength = plaintextLength + (blockSize - (plaintextLength % blockSize));
//            }
//
//            byte[] plaintext = new byte[plaintextLength];
//            System.arraycopy(dataBytes, 0, plaintext, 0, dataBytes.length);
//
//            String subKey=key.substring(0, 16);
//            SecretKeySpec keyspec = new SecretKeySpec(subKey.getBytes(), "AES");
//
//
//            cipher.init(Cipher.ENCRYPT_MODE, keyspec);
//            byte[] encrypted = cipher.doFinal(plaintext);
//
//            return Base64.encodeToString(encrypted, Base64.DEFAULT);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
    
}
