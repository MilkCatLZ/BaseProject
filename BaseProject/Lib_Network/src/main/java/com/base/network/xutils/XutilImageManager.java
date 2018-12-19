package com.base.network.xutils;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import org.xutils.common.Callback.ProgressCallback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;


/**
 * Created by LZ on 2017/12/4.
 */

public class XutilImageManager {
    
    public interface CallBack {
        void onSucces(File result);
        void onError(Throwable ex);
    }
    
    /**
     * 保存图片到相册
     * @param context
     * @param url
     * @param callBack
     */
    public static void saveImageToAlubum(final Context context, final String url, final CallBack callBack) {
        File filesDir = context.getExternalFilesDir(null);
        //获取文件名:/february_2016-001.jpg
        String fileName = url.substring(url.lastIndexOf("/"));
        //存到本地的绝对路径
        final String filePath = filesDir + "/picture" + fileName;
        File file = new File(filesDir + "/picture");
        //如果不存在
        if (!file.exists()) {
            //创建
            file.mkdirs();
        }
        RequestParams params = new RequestParams(url);
        params.setSaveFilePath(filePath);
    
        x.http().get(params,new ProgressCallback<File>() {
            @Override
            public void onWaiting() {
        
            }
    
            @Override
            public void onStarted() {
        
            }
    
            @Override
            public void onLoading(long total, long current, boolean isDownloading) {
        
            }
    
            @Override
            public void onSuccess(File result) {
//                    String newUrl = MediaStore.Images.Media.insertImage(context.getContentResolver(), url, "lianni_invite.jpg", "lianni_invite");
                context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(result)));
                callBack.onSucces(result);
            }
    
            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                callBack.onError(ex);
            }
    
            @Override
            public void onCancelled(CancelledException cex) {
        
            }
    
            @Override
            public void onFinished() {
        
            }
        });
        
//        XUtils.downLoad(params, );
        
    }
}
