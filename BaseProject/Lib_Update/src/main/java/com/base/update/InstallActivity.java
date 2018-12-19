package com.base.update;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.base.update.update.BaseUpdateHelper;

import java.io.File;


/**
 * Created by LZ on 2016/12/22.
 * 安装apk
 */
public class InstallActivity extends AppCompatActivity {
    public static final String APK_URL = "apk_url";
    
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        File file = new File(getIntent().getStringExtra(APK_URL));
        BaseUpdateHelper.openFile(file, this);
        finish();
    }
}
