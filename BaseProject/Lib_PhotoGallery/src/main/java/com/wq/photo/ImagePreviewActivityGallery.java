package com.wq.photo;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;


public class ImagePreviewActivityGallery extends GalleryBaseActivity {


    private ArrayList<String> images;
    private int pos;
    private MypageAdapter adapter;
    private CheckBox checkBox;
    private int currentPostion;
    private TextView txtCount;
    private ViewPager viewPager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_preview_fragemnt);

        initWidgets();

        adapter = new MypageAdapter();
        viewPager.setAdapter(adapter);

        setListener();
        viewPager.setCurrentItem(pos);

    }



    private void checkPreviewOnly() {
        if (previewOnly) {
            confirmItem.setVisible(false);
            txtCount.setVisibility(View.GONE);
            checkBox.setVisibility(View.GONE);
        }
    }

    private void setListener() {
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                currentPostion = position;
                if (MediaChoseActivityGallery.imasgemap.containsKey(images.get(position))) {
                    //set checkbox checked
                    checkBox.setChecked(true);
                } else {
                    //set CheckBox unCheck;
                    checkBox.setChecked(false);
                }
                txtCount.setText(MediaChoseActivityGallery.imasgemap.size() + "/" + MediaChoseActivityGallery.max_chose_count);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if (MediaChoseActivityGallery.imasgemap.size() < MediaChoseActivityGallery.max_chose_count) {
                        MediaChoseActivityGallery.imasgemap.put(images.get(currentPostion), images.get(currentPostion));
                    } else {
                        if (!MediaChoseActivityGallery.imasgemap.containsValue(images.get(currentPostion))) {
                            buttonView.setChecked(false);
                            Toast.makeText(ImagePreviewActivityGallery.this, "你最多只能选择" + MediaChoseActivityGallery.max_chose_count + "张照片", Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    MediaChoseActivityGallery.imasgemap.remove(images.get(currentPostion));
                }
                txtCount.setText(MediaChoseActivityGallery.imasgemap.size() + "/" + MediaChoseActivityGallery.max_chose_count);
            }
        });


    }

    public static final String EXTRA_POS = "pos";
    public static final String EXTRA_IMAGES_LIST = "images";
    public static final String EXTRA_PREVIEW_ONLY = "preview_only";


    boolean previewOnly;

    private void initWidgets() {
        checkBox = (CheckBox) findViewById(R.id.chk_selected);
        txtCount = (TextView) findViewById(R.id.txt_count);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        if (getIntent() != null) {
            images = getIntent().getStringArrayListExtra(EXTRA_IMAGES_LIST);
            pos = getIntent().getIntExtra(EXTRA_POS, 0);

            previewOnly= getIntent().getBooleanExtra(EXTRA_PREVIEW_ONLY, false);
        }
        viewPager = (ViewPager) findViewById(R.id.pager);
    }

    public class MypageAdapter extends PagerAdapter {


        public MypageAdapter() {
        }

        @Override
        public int getCount() {
            return images.size();
        }

        private int mChildCount = 0;

        @Override
        public void notifyDataSetChanged() {
            mChildCount = getCount();
            super.notifyDataSetChanged();
        }

        @Override
        public int getItemPosition(Object object) {
            if (mChildCount > 0) {
                mChildCount--;
                return POSITION_NONE;
            }
            return super.getItemPosition(object);
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {

            container.removeView((View) object);

        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View view = LayoutInflater.from(ImagePreviewActivityGallery.this).inflate(R.layout.pageitem_view, null);
            ImageView imageView = (ImageView) view.findViewById(R.id.iv_image);

            Glide.with(ImagePreviewActivityGallery.this).load(images.get(position))
//                    centerCrop()
                    .crossFade()
                    .diskCacheStrategy(DiskCacheStrategy.RESULT)
                    .placeholder(R.drawable.loadfaild)
                    .into(imageView);
            container.addView(view);

            return view;
        }
    }

    MenuItem confirmItem = null;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_confirm, menu);
        confirmItem = menu.findItem(R.id.action_confirm);
        checkPreviewOnly();
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_confirm) {
            finish();
        }else if(item.getItemId()==android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
