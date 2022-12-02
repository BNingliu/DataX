package com.alibaba.datax.core.util;

import org.apache.commons.lang.StringUtils;
import org.bouncycastle.pqc.math.linearalgebra.ByteUtils;

import java.util.Arrays;

/**
 * @program: ytd-data-integration
 * @description:
 * @author: liuningbo
 * @create: 2021/07/02 15:27
 */
public class SMHelper {
    private static final String DEFAULT_ENCODING = "UTF-8";

    private static final String DEFAULT_KEY = "abcdefgh12345678";

    /**
     * SM4加密
     * @param key UTF8编码后的长度必须为128位，即16个英文字符
     * @param text 原文
     * @return
     */
    public static String sm4Encrypt(String key, String text) {
        try {
            if(StringUtils.isBlank(key)) {
                key = DEFAULT_KEY;
            }
//            byte[] data = encryptEbc(key.getBytes(DEFAULT_ENCODING), text.getBytes(DEFAULT_ENCODING));
            byte[] data = SM4Util.encryptEbc(Arrays.copyOf(key.getBytes(DEFAULT_ENCODING), 16), text.getBytes(DEFAULT_ENCODING));
            return ByteUtils.toHexString(data);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String sm4Encrypt( String text) {
        try {
            byte[] data = SM4Util.encryptEbc(DEFAULT_KEY.getBytes(DEFAULT_ENCODING), text.getBytes(DEFAULT_ENCODING));
            return ByteUtils.toHexString(data);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * SM4解密
     * @param key 与SM4加密时使用的值一致
     * @param cipherText SM4加密后的文本
     * @return
     */
    public static String sm4Decrypt(String key, String cipherText) {
        try {
            if(StringUtils.isBlank(key)) {
                key = DEFAULT_KEY;
            }
            byte[] data = SM4Util.decryptEbc(Arrays.copyOf(key.getBytes(DEFAULT_ENCODING), 16), ByteUtils.fromHexString(cipherText));
//            byte[] data = decryptEbc(key.getBytes(DEFAULT_ENCODING), ByteUtils.fromHexString(cipherText));
            return new String(data, DEFAULT_ENCODING);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String sm4Decrypt(String cipherText) {
        try {
            byte[] data =  SM4Util.decryptEbc(DEFAULT_KEY.getBytes(DEFAULT_ENCODING), ByteUtils.fromHexString(cipherText));
            return new String(data, DEFAULT_ENCODING);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }







    /**
     * SM3生成哈希值
     * @param text 原文
     * @return
     */
    public static String sm3Hash(String text) {
        try {
            return ByteUtils.toHexString(SM3Util.hash(text.getBytes(DEFAULT_ENCODING)));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * SM3检验哈希值
     * @param text 原文
     * @param hash 哈希值
     * @return
     */
    public static boolean sm3verify(String text, String hash) {
        return hash.equals(sm3Hash(text));
    }


}
