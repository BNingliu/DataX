package com.alibaba.datax.plugin.writer.s3writer.util;

import com.alibaba.datax.common.util.Configuration;
import com.alibaba.datax.plugin.writer.s3writer.Constant;
import com.alibaba.datax.plugin.writer.s3writer.Key;
import com.amazonaws.ClientConfiguration;
import com.amazonaws.Protocol;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
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
    public static AmazonS3 initS3Client(Configuration conf) {
        String endpoint = conf.getString(Key.ENDPOINT);
        String secretId = conf.getString(Key.ACCESSID);
        String secretKey = conf.getString(Key.ACCESSKEY);
        String region = conf.getString(Key.REGION);

//        AmazonS3ClientBuilder amazonS3ClientBuilder = AmazonS3ClientBuilder.standard();

        ClientConfiguration clientConfiguration = new ClientConfiguration();
        clientConfiguration.setProtocol(com.amazonaws.Protocol.HTTPS);
        clientConfiguration.setConnectionTimeout(Constant.SOCKETTIMEOUT);
        clientConfiguration.disableSocketProxy();
        clientConfiguration.setProtocol(Protocol.HTTPS);
        clientConfiguration.setConnectionTimeout(Constant.SOCKETTIMEOUT);
        AWSCredentials awsCredentials = new BasicAWSCredentials(secretId, secretKey);

        AmazonS3 amazonS3 = AmazonS3ClientBuilder.standard()
                .withClientConfiguration(clientConfiguration)
                .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(endpoint, null))
                .build();
        return amazonS3;
    }
}