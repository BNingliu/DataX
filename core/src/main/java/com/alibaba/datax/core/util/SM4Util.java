package com.alibaba.datax.core.util;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.GeneralSecurityException;
import java.security.Key;
import java.security.SecureRandom;
import java.security.Security;

/**
 * @program: ytd-data-integration
 * @description:
 * @author: liuningbo
 * @create: 2021/07/02 15:37
 */
public class SM4Util {

    static {
        //提供器添加到JDK中
        Security.addProvider(new BouncyCastleProvider());
    }
    private static final String ALGORITHM_NAME = "SM4";
    private static final String ALGORITHM_NAME_ECB_PADDING = "SM4/ECB/PKCS5Padding";
    private static final String ALGORITHM_NAME_CBC_PADDING = "SM4/CBC/PKCS5Padding";
    private static final int DEFAULT_KEY_SIZE = 128;

    /**
     * 生成随机密钥
     * @return
     * @throws GeneralSecurityException
     */
    public static byte[] generateKey() throws GeneralSecurityException {
        KeyGenerator kg = KeyGenerator.getInstance(ALGORITHM_NAME, BouncyCastleProvider.PROVIDER_NAME);
        kg.init(DEFAULT_KEY_SIZE, new SecureRandom());
        return kg.generateKey().getEncoded();
    }

    /**
     * EBC加密
     * @param key
     * @param data
     * @return
     * @throws GeneralSecurityException
     */
    public static byte[] encryptEbc(byte[] key, byte[] data) throws GeneralSecurityException {
        Cipher cipher = generateCipher(Cipher.ENCRYPT_MODE, key, null);
        return cipher.doFinal(data);
    }

    /**
     * EBC解密
     * @param key
     * @param cipherData
     * @return
     * @throws GeneralSecurityException
     */
    public static byte[] decryptEbc(byte[] key, byte[] cipherData) throws GeneralSecurityException {
        Cipher cipher = generateCipher(Cipher.DECRYPT_MODE, key, null);
        return cipher.doFinal(cipherData);
    }

    /**
     * CBC加密
     * @param key
     * @param iv
     * @param data
     * @return
     * @throws GeneralSecurityException
     */
    public static byte[] encryptCbc(byte[] key, byte[] iv, byte[] data) throws GeneralSecurityException {
        Cipher cipher = generateCipher(Cipher.ENCRYPT_MODE, key, iv);
        return cipher.doFinal(data);
    }

    /**
     * CBC解密
     * @param key
     * @param iv
     * @param cipherData
     * @return
     * @throws GeneralSecurityException
     */
    public static byte[] decryptCbc(byte[] key, byte[] iv, byte[] cipherData) throws GeneralSecurityException {
        Cipher cipher = generateCipher(Cipher.DECRYPT_MODE, key, iv);
        return cipher.doFinal(cipherData);
    }

    /**
     * 获取Cipher
     * @param mode
     * @param key
     * @param iv
     * @return
     * @throws GeneralSecurityException
     */
    private static Cipher generateCipher(int mode, byte[] key, byte[] iv) throws GeneralSecurityException {
        String algorithmName = iv == null ? ALGORITHM_NAME_ECB_PADDING : ALGORITHM_NAME_CBC_PADDING;
        Cipher cipher = Cipher.getInstance(algorithmName, BouncyCastleProvider.PROVIDER_NAME);
        Key sm4Key = new SecretKeySpec(key, ALGORITHM_NAME);
        if (iv != null) {
            IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);
            cipher.init(mode, sm4Key, ivParameterSpec);
        } else {
            cipher.init(mode, sm4Key);
        }
        return cipher;
    }




    public static void main(String[] args) throws Exception {
        System.out.println("----SM4开始测试----");

        String text = "三体世界文明毁灭qwq";
        text="javawx";

        String key = "abcdefgh12345678";
        System.out.println("原文：" + text);
        System.out.println("密钥：" + key);
        String eText = SMHelper.sm4Encrypt(key, text);
        String dText = SMHelper.sm4Decrypt(key, eText);
        System.out.println("加密信息:" + eText);
        System.out.println("解密信息:" + dText);

        if (!dText.equals(text)) {
            throw new Exception("SM4 解密不对。。。");
        }
        int times = 10000;
        long t = System.currentTimeMillis();
        for (int i = 0; i < times; i++) {
            eText = SMHelper.sm4Encrypt(key, text);
            SMHelper.sm4Decrypt(key, eText);
        }
        System.out.println(times + "次对称加密与解密平均耗时：" + ((double)(System.currentTimeMillis() - t) / times) + "毫秒");

        System.out.println("----SM4测试结束----\n");

    }
}
