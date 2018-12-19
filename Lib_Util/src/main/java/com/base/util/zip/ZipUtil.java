package com.base.util.zip;

import com.base.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;


public class ZipUtil {

    /**
     * 递归删除目录下的所有文件及子目录下所有文件
     *
     * @param dir 将要删除的文件目录
     * @return boolean Returns "true" if all deletions were successful.
     * If a deletion fails, the method stops attempting to
     * delete and returns "false".
     */
    public static boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            //递归删除目录中的子目录下
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
//        else{
//            try {
//                Log.d("deleteDir", dir.getAbsolutePath());
                return dir.delete();
//            } catch (Exception e) {
//                return false;
//            }
//        }
//        return dir.delete();
        // 不删除根目录
//        return true;
    }


    /**
     * 解压zip格式文件
     *
     * @param originFile zip文件。
     * @param targetDir  要解压到的目标路径。
     * @return 如果目标文件不是zip文件则返回false。
     * @throws IOException 如果发生I/O错误。
     */
//    public static boolean decompressZip(String originFile, String targetDir) throws IOException {
//        File file = new File(originFile);
//        return decompressZip(file, targetDir);
//    }

//    public static boolean decompressZip(File zipFile, String targetDir) {
//        int BUFFER = 4096; //这里缓冲区我们使用4KB，
//        String strEntry; //保存每个zip的条目名称
//        File targetD = new File(targetDir);
//        if (!targetD.exists()) {
//            targetD.mkdirs();
//        }
//        try {
//            BufferedOutputStream dest = null; //缓冲输出流
//            FileInputStream fis = new FileInputStream(zipFile);
//            ZipInputStream zis = new ZipInputStream(new BufferedInputStream(fis));
//            ZipEntry entry; //每个zip条目的实例
//
//            while ((entry = zis.getNextEntry()) != null) {
//
//                try {
//                    int count;
//                    byte data[] = new byte[BUFFER];
//                    strEntry = entry.getName();
//
//                    File entryFile = new File(targetDir + strEntry);
//                    File entryDir = new File(entryFile.getParent()+"/");
//                    if (!entryDir.exists()) {
//                        entryDir.mkdirs();
//                    }
//
//                    FileOutputStream fos = new FileOutputStream(entryFile);
//                    dest = new BufferedOutputStream(fos, BUFFER);
//                    while ((count = zis.read(data, 0, BUFFER)) != -1) {
//                        dest.write(data, 0, count);
//                    }
//                    dest.flush();
//                    dest.close();
//                } catch (Exception ex) {
//                    ex.printStackTrace();
//                }
//            }
//            zis.close();
//            return true;
//        } catch (Exception cwj) {
//            cwj.printStackTrace();
//            return false;
//        }
//    }
    /**
     * 含子目录的文件压缩
     *
     * @throws Exception
     */
    // 第一个参数就是需要解压的文件，第二个就是解压的目录
    public static boolean decompressZip(File zipFile, String folderPath) {
        ZipFile zfile = null;
        try {
            // 转码为GBK格式，支持中文
            zfile = new ZipFile(zipFile);
            Log.d("decompressZip", folderPath);
        } catch (IOException e) {
            e.printStackTrace();
            zipFile.delete();
            return false;
        }
        Enumeration zList = zfile.entries();
        ZipEntry ze = null;
        byte[] buf = new byte[1024];
        while (zList.hasMoreElements()) {
            ze = (ZipEntry) zList.nextElement();
            // 列举的压缩文件里面的各个文件，判断是否为目录
            if (ze.isDirectory()) {
                String dirstr = folderPath + ze.getName();
//                LogUtils.i(TAG, "dirstr=" + dirstr);
                dirstr.trim();
                File f = new File(dirstr);
                f.mkdir();
                continue;
            }
            OutputStream os = null;
            FileOutputStream fos = null;
            // ze.getName()会返回 script/start.script这样的，是为了返回实体的File
            File realFile = getRealFileName(folderPath, ze.getName());
            try {
                fos = new FileOutputStream(realFile);
            } catch (FileNotFoundException e) {
                return false;
            }
            os = new BufferedOutputStream(fos);
            InputStream is = null;
            try {
                is = new BufferedInputStream(zfile.getInputStream(ze));
            } catch (IOException e) {
                return false;
            }
            int readLen = 0;
            // 进行一些内容复制操作
            try {
                while ((readLen = is.read(buf, 0, 1024)) != -1) {
                    os.write(buf, 0, readLen);
                }
            } catch (IOException e) {
                return false;
            }
            try {
                is.close();
                os.close();
            } catch (IOException e) {
                return false;
            }
        }
        try {
            zfile.close();
        } catch (IOException e) {
            return false;
        }
        return true;
    }

    /**
     * 给定根目录，返回一个相对路径所对应的实际文件名.
     *
     * @param baseDir
     *            指定根目录
     * @param absFileName
     *            相对路径名，来自于ZipEntry中的name
     * @return java.io.File 实际的文件
     */
    public static File getRealFileName(String baseDir, String absFileName) {
//        LogUtils.i(TAG, "baseDir=" + baseDir + "------absFileName="+ absFileName);
        absFileName = absFileName.replace("\\", "/");
//        LogUtils.i(TAG, "absFileName=" + absFileName);
        String[] dirs = absFileName.split("/");
//        LogUtils.i(TAG, "dirs=" + dirs);
        File ret = new File(baseDir);
        String substr = null;
        if (dirs.length > 1) {
            for (int i = 0; i < dirs.length - 1; i++) {
                substr = dirs[i];
                ret = new File(ret, substr);
            }

            if (!ret.exists())
                ret.mkdirs();
            substr = dirs[dirs.length - 1];
            ret = new File(ret, substr);
            return ret;
        } else {
            ret = new File(ret, absFileName);
        }
        return ret;
    }


    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static File forceMkdirs(File file) {
        if (!file.exists()) {
            file.mkdirs();
        } else if (!file.isDirectory()) {
            file.delete();
            file.mkdirs();
        }
        return file;
    }

    public static File forceMkdirs(String pathName) {
        return forceMkdirs(new File(pathName));
    }

}