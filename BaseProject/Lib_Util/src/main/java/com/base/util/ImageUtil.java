package com.base.util;


import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Base64;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;


/**
 * Created by LZ on 2017/10/12.
 * 保存图片到相册
 */
public class ImageUtil {
    
    /**
     * 保存bitmap到指定路径 并刷新相册
     * @param context
     * @param bitmap
     * @param dir
     * @return
     */
    public static Uri saveBitmapToAlbum(Context context, Bitmap bitmap, String dir) {
        String fullPath = saveBitmapToSD(bitmap, dir);
        Uri uri = exportToGallery(context, fullPath);
        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri));
        return uri;
    }
    
//    public static Uri saveImageToAlbum(Context context, Bitmap bitmap) {
//        String newUrl = MediaStore.Images.Media.insertImage(context.getContentResolver(), bitmap, "lianni_invite.jpg", "lianni_invite");
//        File fileNew = new File(newUrl);
//        Uri uri = exportToGallery(context, fileNew.getAbsolutePath());
//        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri));
//        return uri;
//    }
//
    /**
     * 字符图片转成图片
     *
     * @param data
     *
     * @return
     */
    public static Bitmap stringToBitmap(String data) {
        Bitmap bitmap = null;
        
        try {
            byte[] bitmapArray = Base64.decode(data, Base64.DEFAULT);
            bitmap = BitmapFactory.decodeByteArray(bitmapArray, 0, bitmapArray.length);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }
    
    private static Uri exportToGallery(Context context, String filename) {
        // Save the name and description of a video in a ContentValues map.
        final ContentValues values = new ContentValues(2);
        values.put(MediaStore.Video.Media.MIME_TYPE, "image/jpeg");
        values.put(MediaStore.Video.Media.DATA, filename);
        // Add a new record (identified by uri)
        final Uri uri = context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                                                            values);
        return uri;
    }
    
    /**
     * 保存bitmap到指定路径
     *
     * @param bitmap
     * @param dir
     *
     * @return
     */
    public static String saveBitmapToSD(Bitmap bitmap, String dir) {
        File file = new File(dir);
        if (!file.exists()) {
            file.mkdirs();
        }
        String fullPath = dir + "/" + String.valueOf(System.currentTimeMillis()) + ".jpg";
        FileOutputStream out = null;
        
        File imageFile = new File(fullPath);//
        try {
            out = new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            System.out.println("___________保存的__sd___下_______________________");
        } catch (FileNotFoundException e) {
        }
        try {
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        return fullPath;
    }

//    /**
//     * 复制单个文件
//     *
//     * @param oldPath String 原文件路径 如：c:/fqf.txt
//     * @param newPath String 复制后路径 如：f:/fqf.txt
//     *
//     * @return boolean
//     */
//    static void copyFile(String oldPath, String newPath) {
//        try {
//            int bytesum = 0;
//            int byteread = 0;
//            File oldfile = new File(oldPath);
//            if (oldfile.exists()) { //文件存在时
//                InputStream inStream = new FileInputStream(oldPath); //读入原文件
//                FileOutputStream fs = new FileOutputStream(newPath);
//                byte[] buffer = new byte[1444];
//                int length;
//                while ((byteread = inStream.read(buffer)) != -1) {
//                    bytesum += byteread; //字节数 文件大小
//                    System.out.println(bytesum);
//                    fs.write(buffer, 0, byteread);
//                }
//                inStream.close();
//            }
//        } catch (Exception e) {
//        }
//    }
}
