package com.base.util;


import android.content.Context;
import android.support.annotation.NonNull;

import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


/**
 * 缓存本地文件
 *
 * @author LiuZhi
 * @see Context#getExternalCacheDir().getAbsolutePath() 图片缓存目录
 */
public class FileManager {
    static String tag = "LocalImageManager";

    public static String dirPath = "";

    /**
     * 判断URL对应是否有本地图片
     *
     * @param url     图片路径
     * @param context 上下文
     * @return true:存在，否则不存在
     */
    public static boolean hasTheCacheFile(String url, Context context) {
        String filename = convertUrlToFileName(url);
        String dir = getDirectory(context);
        File file = new File(dir, filename);
        if (file.exists()) {
            return true;
        }
        return false;
    }

    /**
     * 判断URL对应是否有本地图片
     *
     * @param path 图片路径
     * @return true:存在，否则不存在
     */
    public static boolean hasFile(String path) {
        File file = new File(path);
        return file.exists();
    }

    /**
     * 根据url生成文件名
     *
     * @param url http地址
     * @return 文件名
     */
    public static String convertUrlToFileName(String url) {

        int index = url.lastIndexOf("/");
        if (index != -1) {
            String name = url.substring(index);
            return name;
        } else
            return url;
    }

    /**
     * @param context 上下文
     * @return 获取图片缓存路径
     */
    public static String getDirectory(@NonNull Context context) {

        try {
            if (context.getExternalCacheDir() != null)
                dirPath = context.getExternalCacheDir().getAbsolutePath() + "/cache";
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
        return dirPath;
    }

    /**
     * 获取文件夹大小
     *
     * @param file File实例
     * @return long 单位为M
     * @throws Exception
     */
    public static float getFolderSize(File file) {
        float size = 0;
        File[] fileList = file.listFiles();
        if (fileList == null) {
            return 0.00f;
        }
        for (File aFileList : fileList) {
            if (aFileList.isDirectory()) {
                size = size + getFolderSize(aFileList);
            } else {
                size = size + aFileList.length();
            }
        }
        return size / 1048576;
    }

    public static void clearCache() {
        try {
            deleteDir(new File(dirPath));
        } catch (Exception ignored) {

        }
    }

    public static void clearCache(File file) {
        try {
            deleteDir(file);
        } catch (Exception ignored) {

        }
    }


    /**
     * 递归删除目录下的所有文件及子目录下所有文件
     *
     * @param dir 将要删除的文件目录
     * @return boolean Returns "true" if all deletions were successful.
     * If a deletion fails, the method stops attempting to
     * delete and returns "false".
     */
    private static boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();//递归删除目录中的子目录下
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        // 目录此时为空，可以删除
        return dir.delete();
    }

    public static String saveFile(String source, String dir, String fileName) {

        return saveFile(new File(source), dir, fileName);
    }

    public static String saveFile(File source, String dir, String fileName) {
        String file = dir + "/" + fileName;
        //保存在本地
        try {

            File newFile = new File(file);
            if (!newFile.exists())
                newFile.createNewFile();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
        return file;
    }

    /**
     * 复制文件
     *
     * @param oldPath
     * @param newPath
     */
    public static String copyFile(String oldPath, String newPath) {
        try {
            int bytesum = 0;
            int byteread = 0;
            File oldfile = new File(oldPath);
            File newFile = new File(newPath);
            if (newFile.exists()) {
                newFile.delete();
            }
            if (oldfile.exists()) { //文件存在时
                InputStream inStream = new FileInputStream(oldPath); //读入原文件
                FileOutputStream fs = new FileOutputStream(newPath);
                byte[] buffer = new byte[1444];
                while ((byteread = inStream.read(buffer)) != -1) {
                    bytesum += byteread; //字节数 文件大小
                    System.out.println(bytesum);
                    fs.write(buffer, 0, byteread);
                }
                inStream.close();
            }
            Log.d("copy-file", oldPath + "--->>>" + newPath);
        } catch (Exception e) {
            System.out.println("复制单个文件操作出错");
            e.printStackTrace();

        }
        return newPath;
    }

    /**
     * get file md5
     *
     * @param file
     * @return
     * @throws NoSuchAlgorithmException
     * @throws IOException
     */
    public static String getFileMD5(File file) throws NoSuchAlgorithmException, IOException {
        if (!file.isFile()) {
            return null;
        }
        MessageDigest digest;
        FileInputStream in;
        byte buffer[] = new byte[1024];
        int len;
        digest = MessageDigest.getInstance("MD5");
        in = new FileInputStream(file);
        while ((len = in.read(buffer, 0, 1024)) != -1) {
            digest.update(buffer, 0, len);
        }
        in.close();
        BigInteger bigInt = new BigInteger(1, digest.digest());
        String md5 = bigInt.toString(16);
        return md5.toLowerCase();
    }

    public static File getFile(@NonNull InputStream inputStream, File file) throws IOException {
        FileOutputStream fos = null;
        try {
            byte[] data = new byte[2048];
            int nbread = 0;
            fos = new FileOutputStream(file);
            while ((nbread = inputStream.read(data)) > -1) {
                fos.write(data, 0, nbread);
            }
        } catch (Exception ex) {

        } finally {
            if (fos != null) {
                fos.close();
            }
        }
        return file;
    }

}
