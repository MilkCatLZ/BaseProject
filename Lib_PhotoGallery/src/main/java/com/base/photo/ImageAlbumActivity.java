package com.base.photo;


import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.wq.photo.R;

import java.util.List;

import uk.co.senab.photoview.PhotoView;


/**
 * @author LiuZhi
 *         本地图片相册，尚未加入网络图片功能，预订加入type参数，标记是网络图片相册还是本地图片相册
 *         传入参数 intent.putStringArrayListExtra({@link #IMAGES},list)必传
 *         intent.putStringArrayListExtra({@link #POSITION},0)可选,图片当前位置
 */
public class ImageAlbumActivity extends AppCompatActivity implements OnPageChangeListener {
    public static String IMAGES = "images";
    public static String POSITION = "position";
//    public static String EXTRA_IMAGE = "image_album_activity";

    private List<String> list;
    private TextView mCurrentIndexTextView;
    private int mCurrentPageIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_album_layout);
        initActionBar();
        initViewPager();
        initCache();
    }

    private void initCache() {
        Glide.with(ImageAlbumActivity.this)
             .load(list.get(mCurrentPageIndex))
             .into((ImageView) findViewById(R.id.img_cache));
    }

    //    @Override
//    protected void onResume() {
//        super.onResume();
//        findViewById(R.id.img_cache).setVisibility(View.GONE);
//    }
//
//    @Override
//    public void finish() {
//        findViewById(R.id.img_cache).setVisibility(View.VISIBLE);
//        super.finish();
//    }
    ViewPagerWithPhotoView viewPager;

    private void initViewPager() {

        viewPager = (ViewPagerWithPhotoView) findViewById(R.id.viewPager1);
        mCurrentIndexTextView = (TextView) findViewById(R.id.txt_image_album_current_index);
        mCurrentPageIndex = getIntent().getIntExtra(POSITION, 0);
        /* 接收参数，本地图片列表 */
        list = getIntent().getStringArrayListExtra(IMAGES);

        viewPager.setOffscreenPageLimit(1);
        viewPager.setOnPageChangeListener(this);
        viewPager.setAdapter(new MyAdapter());
        viewPager.setCurrentItem(mCurrentPageIndex);

        onPageSelected(getIntent().getIntExtra(POSITION, 0));
    }

    private void initActionBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("图片预览");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setTitleTextColor(Color.parseColor("#ffffff"));
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp);
    }


    class MyAdapter extends PagerAdapter {

        @Override
        public Object instantiateItem(ViewGroup container, int position) {

            View view = getLayoutInflater().inflate(R.layout.image_album_item, null);
            final PhotoView imageView = (PhotoView) view.findViewById(R.id.imageView1);

            Glide.with(ImageAlbumActivity.this)
                 .load(list.get(position))
                 .into(new SimpleTarget<GlideDrawable>() {
                     @Override
                     public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                         imageView.setImageDrawable(resource);
                     }
                 });


            container.addView(view);
            imageView.setTag(position);
            return imageView;
        }

        @Override
        public int getCount() {
            
            return list.size();
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            
            return arg0 == arg1;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            
            try {
                ((ViewPager) container).removeView((View) object);
            } catch (Exception e) {

            }
        }

    }

    @Override
    public void onPageScrollStateChanged(int arg0) {
        

    }

    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {
        

    }

    @Override
    public void onPageSelected(int arg0) {
        int page = arg0 + 1;
        if (mCurrentPageIndex == arg0) {
            findViewById(R.id.img_cache).setVisibility(View.VISIBLE);
        } else {
            findViewById(R.id.img_cache).setVisibility(View.GONE);
        }
        mCurrentIndexTextView.setText(page + "/" + list.size());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (mCurrentPageIndex == viewPager.getCurrentItem()) {
            super.onBackPressed();
        } else {
            finish();
        }
    }
}
