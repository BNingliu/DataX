package com.alibaba.datax.plugin.reader.tencentossreader.util;

import com.alibaba.datax.common.util.Configuration;
import com.alibaba.datax.plugin.reader.tencentossreader.Constant;
import com.alibaba.datax.plugin.reader.tencentossreader.Key;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.auth.COSStaticCredentialsProvider;
import com.qcloud.cos.http.HttpProtocol;
import org.jets3t.service.security.AWSCredentials;


import javax.swing.plaf.synth.Region;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description: obs 工具类
 * @Author: chenweifeng
 * @Date: 2022年11月18日 下午1:56
 **/
public class CosUtil {
    public static void main(String[] args) {
        Map<String, Object> map = new HashMap<>();
        map.put(Key.ENDPOINT, "https://s3.ap-northeast-1.amazonaws.com");
        map.put(Key.ACCESSID, "AKIAY6QF3IWQTNWZEQGS");
        map.put(Key.ACCESSKEY, "SdIyvU+oez2Gxqtvb+EzehioUVAWF1wS82rfXRgn");
        Configuration conf = Configuration.from(map);
        initCosClient(conf);
    }

    /**
     * 初始化S3Client
     *
     * @param conf
     * @return
     */
    public static COSClient initCosClient(Configuration conf) {
        String endpoint = conf.getString(Key.ENDPOINT);
        String secretId = conf.getString(Key.ACCESSID);
        String secretKey = conf.getString(Key.ACCESSKEY);

        ClientConfig clientConfiguration = new ClientConfig();

        clientConfiguration.setHttpProtocol(HttpProtocol.https);
        // 未设置bucket 地域
        clientConfiguration.setConnectionTimeout(Constant.SOCKETTIMEOUT);
        COSCredentials cred = new BasicCOSCredentials(secretId, secretKey);

        COSClient cosClient = new COSClient(cred,clientConfiguration);


        return cosClient;
    }
}