package com.base.update.model;


/**
 * Created by LZ on 2017/3/20.
 * 更新基础类
 * 各项目使用这里的自动更新时，实现该接口即可
 */
public interface BaseUpdate {
    
    /**
     *
     * @return 最新版本号
     */
    String getVersion();
    
    /**
     *
     * @return 更新提示语
     */
    String getContent();
    
    /**
     * @return download_url 下载地址，完整的下载url
     */
    String getDownloadUrl();
    
    /**
     * @return compulsion 标志是否强制更新，1：强制；其他不强制
     */
    int getCompulsion();
    
    
}
