package com.alibaba.datax.plugin.writer.ftpwriter.util;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.util.Zip4jConstants;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

public class ZipPassWord {

    public static String zip(String src,  String passwd) {
        File srcFile = new File(src);
        String dest = buildDestinationZipFilePath(srcFile, null);
        ZipParameters parameters = new ZipParameters();
        // 压缩方式
        parameters.setCompressionMethod(Zip4jConstants.COMP_DEFLATE);
        // 压缩级别
        parameters.setCompressionLevel(Zip4jConstants.DEFLATE_LEVEL_NORMAL);
        if (!StringUtils.isEmpty(passwd)) {
            parameters.setEncryptFiles(true);
            // 加密方式
            parameters.setEncryptionMethod(Zip4jConstants.ENC_METHOD_STANDARD);
            parameters.setPassword(passwd.toCharArray());
        }
        try {
            ZipFile zipFile = new ZipFile(dest);

            zipFile.addFile(srcFile, parameters);

            return dest;
        } catch (ZipException e) {
            e.printStackTrace();
        }

        return null;
    }


    /**
     * 使用给定密码压缩指定文件或文件夹到指定位置.
     * <p>
     * dest可传最终压缩文件存放的绝对路径,也可以传存放目录,也可以传null或者""
     * 如果传null或者""则将压缩文件存放在当前目录,即跟源文件同目录,压缩文件名取源文件名,以.zip为后缀;
     * 如果以路径分隔符(File.separator)结尾,则视为目录,压缩文件名取源文件名,以.zip为后缀,否则视为文件名.
     *
     * @param src         要压缩的文件或文件夹路径
     * @param dest        压缩文件存放路径
     * @param isCreateDir 是否在压缩文件里创建目录,仅在压缩文件为目录时有效.
     *                    如果为false,将直接压缩目录下文件到压缩文件.
     * @param passwd      压缩使用的密码
     * @return 最终的压缩文件存放的绝对路径, 如果为null则说明压缩失败.
     */
    public static String zip(String src, String dest, boolean isCreateDir, String passwd) {
        File srcFile = new File(src);
        dest = buildDestinationZipFilePath(srcFile, dest);
        ZipParameters parameters = new ZipParameters();
        // 压缩方式
        parameters.setCompressionMethod(Zip4jConstants.COMP_DEFLATE);
        // 压缩级别
        parameters.setCompressionLevel(Zip4jConstants.DEFLATE_LEVEL_NORMAL);
        if (!StringUtils.isEmpty(passwd)) {
            parameters.setEncryptFiles(true);
            // 加密方式
            parameters.setEncryptionMethod(Zip4jConstants.ENC_METHOD_STANDARD);
            parameters.setPassword(passwd.toCharArray());
        }
        try {
            ZipFile zipFile = new ZipFile(dest);
            if (srcFile.isDirectory()) {
                // 如果不创建目录的话,将直接把给定目录下的文件压缩到压缩文件,即没有目录结构
                if (!isCreateDir) {
                    File[] subFiles = srcFile.listFiles();
                    for (int i = 0; i < subFiles.length; i++) {
                        if (subFiles[i].isDirectory()) {
                            zipFile.addFolder(subFiles[i], parameters);
                        } else {
                            ArrayList<File> temp = new ArrayList<File>();
                            Collections.addAll(temp, subFiles[i]);
                            zipFile.addFiles(temp, parameters);
                        }
                    }
                    return dest;
                }
                zipFile.addFolder(srcFile, parameters);
            } else {
                zipFile.addFile(srcFile, parameters);
            }
            return dest;
        } catch (ZipException e) {
            e.printStackTrace();
        }

        return null;
    }

    private static String buildDestinationZipFilePath(File srcFile, String destParam) {
        if (StringUtils.isEmpty(destParam)) {
            if (srcFile.isDirectory()) {
                destParam = srcFile.getParent() + File.separator + srcFile.getName() + ".zip";
            } else {
                String fileName = srcFile.getName().substring(0, srcFile.getName().lastIndexOf("."));
                destParam = srcFile.getParent() + File.separator + fileName + ".zip";
            }
        } else {
            // 在指定路径不存在的情况下将其创建出来
            createDestDirectoryIfNecessary(destParam);
            if (destParam.endsWith(File.separator)) {
                String fileName = "";
                if (srcFile.isDirectory()) {
                    fileName = srcFile.getName();
                } else {
                    fileName = srcFile.getName().substring(0, srcFile.getName().lastIndexOf("."));
                }
                destParam += fileName + ".zip";
            }
        }
        return destParam;
    }

    private static void createDestDirectoryIfNecessary(String destParam) {
        File destDir = null;
        if (destParam.endsWith(File.separator)) {
            destDir = new File(destParam);
        } else {
            destDir = new File(destParam.substring(0, destParam.lastIndexOf("/")));
        }
        if (!destDir.exists()) {
            destDir.mkdirs();
        }
    }
}

