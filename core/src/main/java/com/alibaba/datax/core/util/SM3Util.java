package com.alibaba.datax.core.util;

import org.bouncycastle.crypto.digests.SM3Digest;
import org.bouncycastle.crypto.macs.HMac;
import org.bouncycastle.crypto.params.KeyParameter;

/**
 * @program: ytd-data-integration
 * @description:
 * @author: liuningbo
 * @create: 2021/07/02 15:22
 */
public class SM3Util {
    /**
     * 生成哈希值
     * @param data
     * @return
     */
    public static byte[] hash(byte[] data) {
        SM3Digest digest = new SM3Digest();
        digest.update(data, 0, data.length);
        byte[] hash = new byte[digest.getDigestSize()];
        digest.doFinal(hash, 0);
        return hash;
    }

    /**
     * 生成HMAC值
     * @param key
     * @param data
     * @return
     */
    public static byte[] hmac(byte[] key, byte[] data) {
        KeyParameter keyParameter = new KeyParameter(key);
        SM3Digest digest = new SM3Digest();
        HMac mac = new HMac(digest);
        mac.init(keyParameter);
        mac.update(data, 0, data.length);
        byte[] result = new byte[mac.getMacSize()];
        mac.doFinal(result, 0);
        return result;
    }


}
