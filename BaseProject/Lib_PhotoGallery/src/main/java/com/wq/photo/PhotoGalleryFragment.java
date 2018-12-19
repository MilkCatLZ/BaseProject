package com.wq.photo;


import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import com.base.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListPopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.wq.photo.adapter.FloderAdapter;
import com.wq.photo.adapter.PhotoAdapter;
import com.wq.photo.mode.ImageFloder;
import com.wq.photo.widget.PickConfig;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;


public class PhotoGalleryFragment extends Fragment implements android.os.Handler.Callback {






    public static final String jpg = ".jpg";
    public static final String JPG = ".JPG";
    public static final String PNG = ".PNG";
    public static final String png = ".png";
    public static final String jpeg = ".jpeg";
    public static final String JPEG = ".JPEG";
    private static final int CODE_FOR_WRITE_PERMISSION = 33;

    public int max_chose_count = 9;
    private int spancount = 3;
    public boolean isNeedfcamera = false;
    View rootview;
    RecyclerView my_recycler_view;
    TextView open_gallery;
    ImageView clear;
    PhotoAdapter adapter;
    ArrayList<String> imageses = new ArrayList<>();

    ArrayList<String> currentimageses = new ArrayList<>();
    Handler handler;
    /**
     * 临时的辅助类，用于防止同一个文件夹的多次扫描
     */
    private HashSet<String> mDirPaths = new HashSet<String>();
    int totalCount = 0;
    /**
     * 扫描拿到所有的图片文件夹
     */
    private List<ImageFloder> mImageFloders = new ArrayList<ImageFloder>();
    private int scanType;

    @Override
    public boolean handleMessage(Message msg) {
        if (msg.what == 1) {
            initFloderPop();
        } else {
            adapter.notifyDataSetChanged();
        }
        return false;
    }

    ListPopupWindow popupWindow;
    FloderAdapter floderAdapter;

    /**
     * 初始化文件夹
     */
    private void initFloderPop() {
        if (getActivity() == null) {
            return;
        }
        if (getActivity().isFinishing()) {
            return;
        }
        popupWindow = new ListPopupWindow(getActivity());
        ImageFloder allimgslist = new ImageFloder();
        allimgslist.setDir("/所有图片");
        allimgslist.setCount(imageses.size());
        if (imageses.size() > 0) {
            allimgslist.setFirstImagePath(imageses.get(0));
        }
        mImageFloders.add(0, allimgslist);
        floderAdapter = new FloderAdapter(mImageFloders, getActivity());
        popupWindow.setAdapter(floderAdapter);
        int sWidthPix = getResources().getDisplayMetrics().widthPixels;
        popupWindow.setContentWidth(sWidthPix);
        popupWindow.setHeight(sWidthPix + 100);
        popupWindow.setAnchorView(open_gallery);
        open_gallery.setEnabled(true);

        popupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ImageFloder floder = (ImageFloder) parent.getAdapter().getItem(position);
                floderAdapter.setCheck(position);
                if (floder.getName().equals("/所有图片")) {
                    currentimageses.clear();
                    currentimageses.addAll(imageses);
                    adapter = new PhotoAdapter(getActivity(), currentimageses, spancount,
                                               chose_mode);
                    adapter.setmax_chose_count(max_chose_count);
                    adapter.setDir("");
                    adapter.setNeedCamera(isNeedfcamera);
                    my_recycler_view.setAdapter(adapter);
                    popupWindow.dismiss();
                    open_gallery.setText("所有图片");
                } else {
                    File mImgDir = new File(floder.getDir());
                    List<String> ims =
                        Arrays.asList(mImgDir.list(new FilenameFilter() {
                            @Override
                            public boolean accept(File dir, String filename) {
                                if (filename.endsWith(jpg)
                                    || filename.endsWith(JPG)
                                    || filename.endsWith(png)
                                    || filename.endsWith(PNG)
                                    || filename.endsWith(jpeg)
                                    || filename.endsWith(JPEG))
                                    return true;
                                return false;
                            }
                        }));

                    currentimageses.clear();
                    currentimageses.addAll(ims);
                    /**
                     * 可以看到文件夹的路径和图片的路径分开保存，极大的减少了内存的消耗；
                     */
                    adapter = new PhotoAdapter(getActivity(), currentimageses, spancount,
                                               chose_mode);
                    adapter.setmax_chose_count(max_chose_count);
                    adapter.setDir(floder.getDir());
//                    adapter.setNeedCamera(false);
                    my_recycler_view.setAdapter(adapter);
                    open_gallery.setText(floder.getName());
                    popupWindow.dismiss();
                }
            }
        });

    }

    int chose_mode = 1;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ImagePreviewFragemnt.
     */
    public static PhotoGalleryFragment newInstance() {
        PhotoGalleryFragment fragment = new PhotoGalleryFragment();
        return fragment;
    }

    /**
     * 当拍照之后刷新出来拍照的那张照片
     *
     * @param path
     */
    public void addCaptureFile(final String path) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                currentimageses.add(0, path);
                imageses.add(0, path);
                adapter.setImageses(imageses);
                my_recycler_view.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }
        });
        my_recycler_view.postDelayed(new Runnable() {
            @Override
            public void run() {
                adapter.notifyDataSetChanged();
            }
        }, 2500);
//        try {
//            currentimageses.add(0, path);
//            imageses.add(0, path);
//            adapter.notifyItemInserted(0);
//        }catch (Exception e){
//
//        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        handler = new Handler(this);
    }

    public void time(String msg) {
        Log.i("milles",
              msg + System.currentTimeMillis() + "thread" + Thread.currentThread().getName());
    }

    @Override
    public View onCreateView(
        LayoutInflater inflater,
        @Nullable ViewGroup container,
        @Nullable Bundle savedInstanceState) {
        if (rootview == null) {
            rootview = inflater.inflate(R.layout.fragment_photogallery_layout, container, false);
            my_recycler_view = (RecyclerView) rootview.findViewById(R.id.my_recycler_view);
            open_gallery = (TextView) rootview.findViewById(R.id.open_gallery);
            clear = (ImageView) rootview.findViewById(R.id.clear);
            open_gallery.setEnabled(false);
        }
        if (adapter == null) {
            adapter = new PhotoAdapter(getActivity(), currentimageses, spancount, chose_mode);
            adapter.setDir("");
            adapter.setNeedCamera(isNeedfcamera);
            adapter.setmax_chose_count(max_chose_count);
        }
        //如果可以确定每个item的高度是固定的，设置这个选项可以提高性能
        my_recycler_view.setHasFixedSize(true);
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), spancount);
        my_recycler_view.setLayoutManager(layoutManager);
        my_recycler_view.setAdapter(adapter);
        open_gallery.setText("所有图片");
        loadAllImages();
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
        return rootview;
    }

    boolean isshowiing = false;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        try {
            open_gallery.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isshowiing) {
                        isshowiing = false;
                        popupWindow.dismiss();
                    } else {
                        isshowiing = true;
                        popupWindow.show();
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void notifyDataSetChanged() {
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }

    public void loadAllImages() {
        if (!Environment.getExternalStorageState().equals(
            Environment.MEDIA_MOUNTED)) {
            Toast.makeText(getActivity(), "暂无外部存储", Toast.LENGTH_SHORT).show();
            return;
        }
        if (Build.VERSION.SDK_INT >= 23) {
            int hasWriteContactsPermission = ContextCompat.checkSelfPermission(getActivity(),
                                                                               Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if (hasWriteContactsPermission != PackageManager.PERMISSION_GRANTED) {
                Activity activty = getActivity();
                ActivityCompat.requestPermissions(activty,
                                                  new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                                  CODE_FOR_WRITE_PERMISSION);
                scanType = 99;
                return;
            }
        }
        getAllImageThread();
    }

    private void getAllImageThread() {
        new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    if (getActivity() != null) {
                        String[] columns = {MediaStore.Images.Media.DATA, MediaStore.Images.Media.MIME_TYPE, MediaStore.Images.Media._ID, MediaStore.Images.Media.DATE_MODIFIED};
                        Cursor cursor = getActivity().getContentResolver()
                                                     .query(MediaStore.Images.
                                                                Media.EXTERNAL_CONTENT_URI, columns,
                                                            MediaStore.Images.Media.MIME_TYPE + "=? or " +
                                                            MediaStore.Images.Media.MIME_TYPE + "=? or " +
                                                            MediaStore.Images.Media.MIME_TYPE + "=?",
                                                            new String[]{"image/jpeg", "image/png", "image/gif"},
                                                            MediaStore.Images.Media.DATE_MODIFIED + " DESC");
                        int dataColumnIndex = cursor.getColumnIndex(MediaStore.Images.Media.DATA);
                        while (cursor.moveToNext()) {
                            String photopath = cursor.getString(dataColumnIndex);
                            if (photopath != null && new File(photopath).exists()) {
                                imageses.add(photopath);
                            }
                        }
                        if (cursor != null) {
                            cursor.close();
                        }
                        currentimageses.clear();
                        currentimageses.addAll(imageses);
                        handler.sendEmptyMessage(0);
                        getImages();
                    }
                } catch (Exception e) {

                }
            }
        }).start();
    }

    /**
     * 利用ContentProvider扫描手机中的图片，此方法在运行在子线程中 完成图片的扫描，最终获得jpg最多的那个文件夹
     */
    private void getImages() {
        if (Build.VERSION.SDK_INT >= 23) {
            int hasWriteContactsPermission = ContextCompat.checkSelfPermission(getActivity(),
                                                                               Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if (hasWriteContactsPermission != PackageManager.PERMISSION_GRANTED) {
                Activity activty = getActivity();
                ActivityCompat.requestPermissions(activty,
                                                  new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                                  CODE_FOR_WRITE_PERMISSION);
                scanType = 98;
                return;
            }
        }
        getImagesThread();
    }

    private void checkAndGetImage() {
        if (scanType == 98) {
            getImagesThread();
        } else if (scanType == 99) {
            getAllImageThread();
        }
    }


    private void getImagesThread() {

        new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    if (getActivity() != null) {
                        String firstImage = null;

                        Uri mImageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                        ContentResolver mContentResolver = getActivity()
                            .getContentResolver();

                        // 只查询jpeg和png的图片
                        Cursor mCursor = mContentResolver.query(mImageUri, null,
                                                                MediaStore.Images.Media.MIME_TYPE + "=? or " +
                                                                MediaStore.Images.Media.MIME_TYPE + "=? or " +
                                                                MediaStore.Images.Media.MIME_TYPE + "=?",
                                                                new String[]{"image/jpeg", "image/png", "image/gif"},
                                                                MediaStore.Images.Media.DATE_MODIFIED);

                        Log.e("TAG", mCursor.getCount() + "");
                        while (mCursor.moveToNext()) {
                            // 获取图片的路径
                            String path = mCursor.getString(mCursor
                                                                .getColumnIndex(
                                                                    MediaStore.Images.Media.DATA));
                            // 拿到第一张图片的路径
                            if (firstImage == null)
                                firstImage = path;
                            // 获取该图片的父路径名
                            File parentFile = new File(path);
                            if (parentFile == null)
                                continue;
                            String dirPath = parentFile.getParentFile()
                                                       .getAbsolutePath();
                            ImageFloder imageFloder = null;
                            File file = new File(dirPath);
                            if (file != null && file.isDirectory() && file.list().length > 0) {
                                // 利用一个HashSet防止多次扫描同一个文件夹（不加这个判断，图片多起来还是相当恐怖的~~）
                                if (mDirPaths.contains(dirPath)) {
                                    continue;
                                } else {
                                    mDirPaths.add(dirPath);
                                    // 初始化imageFloder
                                    imageFloder = new ImageFloder();
                                    imageFloder.setDir(dirPath);
                                    imageFloder.setFirstImagePath(path);
                                }

                                int picSize = file.list(new FilenameFilter() {
                                    @Override
                                    public boolean accept(File dir, String filename) {
                                        if (filename == null) {
                                            return false;
                                        }
                                        if (filename.endsWith(jpg)
                                            || filename.endsWith(JPG)
                                            || filename.endsWith(png)
                                            || filename.endsWith(PNG)
                                            || filename.endsWith(jpeg)
                                            || filename.endsWith(JPEG))
                                            return true;
                                        return false;
                                    }
                                }).length;
                                totalCount += picSize;
                                imageFloder.setCount(picSize);
                                mImageFloders.add(imageFloder);
                            }
                        }
                        Collections.sort(mImageFloders, new Comparator<ImageFloder>() {
                            @Override
                            public int compare(ImageFloder lhs, ImageFloder rhs) {

                                String lc = lhs.getName().substring(1,  2);
                                String rc = rhs.getName().substring(1,  2);
                                return lc.compareTo(rc);
                            }
                        });
                        mCursor.close();
                        // 扫描完成，辅助的HashSet也就可以释放内存了
                        mDirPaths = null;
                        // 通知Handler扫描图片完成
                        handler.sendEmptyMessage(1);
                    }
                } catch (Exception e) {
                    //getActvity可能为空，这时候不需要处理任何事件了
                }
            }
        }).start();
    }

    @Override
    public void onRequestPermissionsResult(
        int requestCode,
        String[] permissions,
        int[] grantResults) {
        if (requestCode == CODE_FOR_WRITE_PERMISSION) {
            if (permissions[0].equals(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //用户同意使用write
                checkAndGetImage();
            } else {
                //用户不同意，自行处理即可

                //用户不同意，向用户展示该权限作用
                if (!ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                                                                         Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    AlertDialog dialog = new AlertDialog.Builder(getActivity())
                        .setMessage("该相册需要赋予访问存储的权限，不开启将无法正常工作！")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                getActivity().finish();
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                getActivity().finish();
                            }
                        }).create();
                    dialog.show();
                    return;
                }
                getActivity().finish();
            }
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        Bundle bundle = getArguments();
        if (bundle == null) return;
        chose_mode = bundle.getInt(PickConfig.EXTRA_PICK_MODE);
        max_chose_count = bundle.getInt(PickConfig.EXTRA_MAX_SIZE);
        spancount = bundle.getInt(PickConfig.EXTRA_SPAN_COUNT);
        isNeedfcamera = bundle.getBoolean(PickConfig.EXTRA_IS_NEED_CAMERA);
    }

    @Override
    public void onResume() {
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
        super.onResume();
    }

//    @Override
//    public void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
//    }

}
