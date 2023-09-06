package com.alibaba.datax.plugin.reader.s3reader.util;

import com.alibaba.datax.common.util.Configuration;
import com.alibaba.datax.plugin.reader.s3reader.Key;
import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description: obs 工具类
 * @Author: chenweifeng
 * @Date: 2022年11月18日 下午1:56
 **/
public class S3Util {
    public static void main(String[] args) {
        Map<String, Object> map = new HashMap<>();
        map.put(Key.ENDPOINT, "https://s3.ap-northeast-1.amazonaws.com");
        map.put(Key.ACCESSID, "AKIAY6QF3IWQTNWZEQGS");
        map.put(Key.ACCESSKEY, "SdIyvU+oez2Gxqtvb+EzehioUVAWF1wS82rfXRgn");
        Configuration conf = Configuration.from(map);
        initS3Client(conf);
    }

    /**
     * 初始化S3Client
     *
     * @param conf
     * @return
     */
    public static AmazonS3 initS3Client(Configuration conf) {
        String endpoint = conf.getString(Key.ENDPOINT);
        String secretId = conf.getString(Key.ACCESSID);
        String secretKey = conf.getString(Key.ACCESSKEY);
        String region = conf.getString(Key.REGION);
//        ClientConfiguration clientConfiguration = new ClientConfiguration();
//        clientConfiguration.disableSocketProxy();
//        clientConfiguration.setProtocol(Protocol.HTTPS);
//        clientConfiguration.setConnectionTimeout(Constant.SOCKETTIMEOUT);
        AWSCredentials awsCredentials = new BasicAWSCredentials(secretId, secretKey);

        AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
//                .withRegion(Regions.US_EAST_1)
                .withClientConfiguration(new ClientConfiguration()
                        .withConnectionTimeout(30 * 1000)  // 设置连接超时时间
                        .withMaxConnections(100)           // 设置连接池的最大连接数
                        .withMaxErrorRetry(10)             // 设置最大重试次数
                        .withSocketTimeout(1200 * 1000))     // 设置读取超时时间为1分钟
                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(endpoint, null))
                .build();

//        AmazonS3 amazonS3 = AmazonS3ClientBuilder.standard()
//                .withClientConfiguration(clientConfiguration)
//                .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
//                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(endpoint, null))
//                .build();
        return s3Client;
    }
}