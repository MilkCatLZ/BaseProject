package com.wq.photo;


import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.base.util.Log;
import com.base.util.ToastManager;
import com.wq.photo.widget.PickConfig;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;


/**
 * 调用媒体选择库
 * 需要在inten中传递2个参数
 * 1. 选择模式 chose_mode  0  //单选 1多选
 * 2. 选择张数 max_chose_count  多选模式默认 9 张
 */
public class MediaChoseActivityGallery extends GalleryBaseActivity {

    public CharSequence getActivityName() {
        return "选择图片";
    }

    public static int max_chose_count = 1;
    public static LinkedHashMap imasgemap = new LinkedHashMap();
    //    public LinkedHashSet imagesChose = new LinkedHashSet();
    PhotoGalleryFragment photoGalleryFragment;

    int chosemode = PickConfig.MODE_SINGLE_PICK;
    int crop_image_w, crop_image_h;
    int colorPrimary;
    int spanCount;

    boolean isCropOver = false;
    boolean isPriview = false;
    boolean isneedCrop = false;
    boolean isNeedActionbar = false;
    boolean isNeedfcamera = false;

    private TextView txtCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media_chose);
        if (imasgemap != null) {
            imasgemap.clear();
        }
        if (getSupportActionBar() == null) {
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            setTitle(getActivityName());
        }

        txtCount = (TextView) findViewById(R.id.txt_count);
        FragmentTransaction fragmentTransaction =
                getSupportFragmentManager().beginTransaction();
        Bundle bundle = getIntent().getBundleExtra(PickConfig.EXTRA_PICK_BUNDLE);
        spanCount = bundle.getInt(PickConfig.EXTRA_SPAN_COUNT, PickConfig.DEFAULT_SPANCOUNT);
        chosemode = bundle.getInt(PickConfig.EXTRA_PICK_MODE, PickConfig.MODE_SINGLE_PICK);
        max_chose_count = bundle.getInt(PickConfig.EXTRA_MAX_SIZE, PickConfig.DEFAULT_PICKSIZE);
        isNeedActionbar = bundle.getBoolean(PickConfig.EXTRA_IS_NEED_ACTIONBAR, true);
        isneedCrop = bundle.getBoolean(PickConfig.EXTRA_IS_NEED_CROP, false);
        isNeedfcamera = bundle.getBoolean(PickConfig.EXTRA_IS_NEED_CAMERA, true);
        crop_image_w = bundle.getInt(PickConfig.EXTRA_CROP_WIDTH, 0);
        crop_image_h = bundle.getInt(PickConfig.EXTRA_CROP_HEIGHT, 0);
        if (chosemode == PickConfig.MODE_MULTIP_PICK) {
            isneedCrop = false;
        }
        photoGalleryFragment = PhotoGalleryFragment.newInstance();
        photoGalleryFragment.setArguments(bundle);
        fragmentTransaction.add(R.id.container, photoGalleryFragment,
                PhotoGalleryFragment.class.getSimpleName());
        fragmentTransaction.commit();
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        List<String> list = bundle.getStringArrayList(PickConfig.EXTRA_SELECTED_LIST);
        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                imasgemap.put(list.get(i), list.get(i));
            }
        }
    }


    public void starPriview(LinkedHashMap map, String currentimage, ArrayList<String> list) {
        if (isneedCrop && !isCropOver) {
            sendStarCrop(currentimage);
        } else {


            int pos = list.indexOf(currentimage);

            Intent intent = new Intent(this, ImagePreviewActivityGallery.class);
            intent.putExtra(ImagePreviewActivityGallery.EXTRA_IMAGES_LIST, list);
            intent.putExtra(ImagePreviewActivityGallery.EXTRA_POS, pos);
//            intent.putExtra(ImagePreviewActivityGallery.EXTRA_PREVIEW_ONLY, true);
            startActivity(intent);

        }
    }

    public Fragment getCurrentFragment(String tag) {
        return getSupportFragmentManager().findFragmentByTag(tag);
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (!isNeedActionbar) {
            getSupportActionBar().hide();
        }
    }

    public LinkedHashMap getImageChoseMap() {
        return imasgemap;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.photo_gallery_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            if (isPriview) {
                popFragment();
            } else {
                finish();
            }
        } else if (item.getItemId() == R.id.menu_photo_delete) {
            ImagePreviewFragemnt fragemnt = (ImagePreviewFragemnt) getCurrentFragment(
                    ImagePreviewFragemnt.class.getSimpleName());
            if (fragemnt != null) {
                String img = fragemnt.delete();
                Iterator iterator = imasgemap.keySet()
                        .iterator();
                while (iterator.hasNext()) {
                    String key = (String) iterator.next();
                    if (imasgemap.get(key)
                            .equals(img)) {
                        iterator.remove();
                    }
                }
                invalidateOptionsMenu();
            }
        } else if (item.getItemId() == R.id.menu_photo_count) {
            if (imasgemap == null || imasgemap.isEmpty()) {
                ToastManager.showShortToast(this, "请选择图片");
            } else {
                sendImages();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        FragmentManager fm = getSupportFragmentManager();
        if (keyCode == KeyEvent.KEYCODE_BACK && fm.getBackStackEntryCount() > 0) {
            popFragment();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void log(String msg) {
        Log.i("gallery", msg);
    }

    public void popFragment() {
        FragmentManager fm = getSupportFragmentManager();
        fm.popBackStackImmediate();
//        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        isPriview = false;
        invalidateOptionsMenu();
        if (photoGalleryFragment != null && chosemode == PickConfig.MODE_MULTIP_PICK) {
            photoGalleryFragment.notifyDataSetChanged();
        }
    }

    public void sendImages() {
        if (isneedCrop && !isCropOver) {
            Iterator iterator = imasgemap.keySet()
                    .iterator();
            File file = new File(iterator.next()
                    .toString());
            if (!file.exists()) {
                Toast.makeText(this, R.string.str_get_file_failed, Toast.LENGTH_SHORT)
                        .show();
            }
            sendStarCrop(file.getAbsolutePath());
        } else {
            Intent intent = new Intent();
            ArrayList<String> img = new ArrayList<>();
            Iterator iterator = imasgemap.keySet()
                    .iterator();
            while (iterator.hasNext()) {
                String key = (String) iterator.next();
                img.add((String) imasgemap.get(key));
            }
            intent.putExtra(PickConfig.DATA, img);
            setResult(RESULT_OK, intent);
            finish();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE_CROP && (chosemode == PickConfig.MODE_SINGLE_PICK)) {
            Intent intent = new Intent();
            ArrayList<String> img = new ArrayList<>();
            String crop_path = data.getStringExtra("crop_path");
            isCropOver = true;
            if (crop_path != null && new File(crop_path) != null) {
                img.add(crop_path);
                intent.putExtra(PickConfig.DATA, img);
                setResult(RESULT_OK, intent);
                finish();
            } else {
                Toast.makeText(this, R.string.str_cut_photo_failed, Toast.LENGTH_SHORT)
                        .show();
            }
        } else if (resultCode == RESULT_OK && requestCode == REQUEST_CODE_CAMERA && (chosemode == PickConfig.MODE_SINGLE_PICK)) {
            if (currentfile != null && currentfile.exists() && currentfile.length() > 10) {
                if (isneedCrop && !isCropOver) {
                    sendStarCrop(currentfile.getAbsolutePath());
                } else {
                    Intent intent = new Intent();
                    ArrayList<String> img = new ArrayList<>();
                    img.add(currentfile.getAbsolutePath());
                    intent.putExtra(PickConfig.DATA, img);
                    setResult(RESULT_OK, intent);
                    finish();
                }
                insertImage(currentfile.getAbsolutePath());
            } else {
                Toast.makeText(MediaChoseActivityGallery.this, getString(R.string.str_get_photo_failed), Toast.LENGTH_SHORT)
                        .show();
            }
        } else if (resultCode == RESULT_OK && requestCode == REQUEST_CODE_CAMERA && (chosemode == PickConfig.MODE_MULTIP_PICK)) {
            new AsyncTask<String, String, String>() {

                @Override
                protected void onPostExecute(String s) {
                    super.onPostExecute(s);
                    if (s == null) {
                        getImageChoseMap().put(currentfile.getAbsolutePath(),
                                currentfile.getAbsolutePath());
                        invalidateOptionsMenu();
                        insertImage(currentfile.getAbsolutePath());
                    } else {
                        Toast.makeText(MediaChoseActivityGallery.this, s, Toast.LENGTH_SHORT)
                                .show();
                    }
                }

                @Override
                protected String doInBackground(String... params) {
                    for (int i = 0; i < 100; i++) {
                        if (currentfile != null && currentfile.exists() && currentfile.length() > 100) {

//                            Log.i("doInBackground", "currentfile not NUll get Pic Successed");
                            return null;
                        } else {
                            try {
                                Thread.sleep(100);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
//                            Log.i("doInBackGround", params[0]+"");
                            currentfile = new File(tempCurrentFilePath);
//                            Log.i("doInBackground", "currentfile is Null Null Null Null!!!");
                            continue;
                        }
                    }
                    return getString(R.string.str_get_photo_failed);
//                   Toast.makeText(MediaChoseActivityGallery.this, "获取图片失败", Toast.LENGTH_SHORT).show();
//                   return null;
                }
            }.execute(tempCurrentFilePath);
//            if (currentfile != null && currentfile.exists() /*&& currentfile.length() > 10*/) {
//                getImageChoseMap().put(currentfile.getAbsolutePath(), currentfile.getAbsolutePath());
//                invalidateOptionsMenu();
//                insertImage(currentfile.getAbsolutePath());
//            } else {
//                Toast.makeText(MediaChoseActivityGallery.this, "获取图片失败", Toast.LENGTH_SHORT).show();
//            }
        }
    }

    public void insertImage(String fileName) {
//        try {
        //多余了，拍照成功后已经有一张图片了
//            MediaStore.Images.Media.insertImage(getContentResolver(),
//                                                fileName, new File(fileName).getName(),
//                                                new File(fileName).getName());
        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri uri = Uri.fromFile(new File(fileName));
        intent.setData(uri);
        sendBroadcast(intent);
//            photoGalleryFragment.addCaptureFile(fileName);
        MediaScannerConnection.scanFile(getApplicationContext(), new String[]{fileName},
                new String[]{"image/jpeg"},
                new MediaScannerConnection.MediaScannerConnectionClient() {
                    @Override
                    public void onMediaScannerConnected() {
                    }

                    @Override
                    public void onScanCompleted(String path, Uri uri) {
                        photoGalleryFragment.addCaptureFile(path);
                        Log.i("onScanCompleted",
                                "onScanCompleted path = " + path);
                        Log.i("onScanCompleted",
                                "onScanCompleted tempCurrentFilePath = " + tempCurrentFilePath);
                    }
                });

//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
    }

    public static final int REQUEST_CODE_CAMERA = 2001;
    public static final int REQUEST_CODE_CROP = 2002;
    File currentfile;
    static String tempCurrentFilePath;

    public void sendStarCamera() {
        currentfile = getTempFile();
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (Build.VERSION.SDK_INT <= 21) {
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(currentfile));
        } else {
            String packageName = getPackageName();
            intent.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(this, packageName+".provider", currentfile));
        }
        tempCurrentFilePath = currentfile.getAbsolutePath();
        intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 0.5);
        startActivityForResult(intent, REQUEST_CODE_CAMERA);
    }

    public void sendStarCrop(String path) {
        Intent intent = new Intent(this, CropImageActivityGallery.class);
        intent.setData(Uri.fromFile(new File(path)));
        if (crop_image_w > 0) {
            intent.putExtra(PickConfig.CROP_IMAGE_W, crop_image_w);
        }
        if (crop_image_h > 0) {
            intent.putExtra(PickConfig.CROP_IMAGE_H, crop_image_h);
        }
        intent.putExtra(MediaStore.EXTRA_OUTPUT, getCropFile().getAbsolutePath());
        startActivityForResult(intent, REQUEST_CODE_CROP);
    }

    public File getTempFile() {
        String str = null;
        Date date = null;
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        date = new Date(System.currentTimeMillis());
        str = format.format(date);
        return new File(Environment.getExternalStorageDirectory(),
                "IMG_" + str + ".jpg");
    }

    public File getCropFile() {
        return new File(getTmpPhotos());
    }

    /**
     * 获取tmp path
     *
     * @return
     */
    public String getTmpPhotos() {
        return new File(getCacheFile(),
                ".tmpcamara" + System.currentTimeMillis() + ".jpg").getAbsolutePath();
    }

    /**
     * 临时缓存目录
     *
     * @return
     */
    public String getCacheFile() {
        return getDir("post_temp", Context.MODE_PRIVATE).getAbsolutePath();
    }

    public void notifySelectedTInfo() {
        try {
            txtCount.setText(imasgemap.size() + "/" + max_chose_count);
        } catch (Exception e) {

        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onRequestPermissionsResult(
            int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        try {
            for (Fragment fragment : getSupportFragmentManager().getFragments()) {
                fragment.onRequestPermissionsResult(requestCode, permissions, grantResults);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
