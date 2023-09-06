package com.alibaba.datax.plugin.writer.tencentosswriter.util;

import com.alibaba.datax.common.util.Configuration;
import com.alibaba.datax.plugin.writer.tencentosswriter.Constant;
import com.alibaba.datax.plugin.writer.tencentosswriter.Key;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.http.HttpProtocol;

/**
 * @Description: obs 工具类
 * @Author: chenweifeng
 * @Date: 2022年11月18日 下午1:56
 **/
public class CosUtil {
//    public static void main(String[] args) {
//        Map<String, Object> map = new HashMap<>();
//        map.put(Key.ENDPOINT, "http://x.x.x.x:9383");
//        map.put(Key.ACCESSID, "admin");
//        map.put(Key.ACCESSKEY, "123456");
//        Configuration conf = Configuration.from(map);
//        initS3Client(conf);
//    }

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
//        String region = conf.getString(Key.REGION);

//        AmazonS3ClientBuilder amazonS3ClientBuilder = AmazonS3ClientBuilder.standard();

        ClientConfig clientConfiguration = new ClientConfig();
        clientConfiguration.setHttpProtocol(HttpProtocol.https);
        clientConfiguration.setConnectionTimeout(Constant.SOCKETTIMEOUT);

        COSCredentials cred = new BasicCOSCredentials(secretId, secretKey);

        COSClient cosClient = new COSClient(cred,clientConfiguration);

        return cosClient;
    }
}