package com.base.photo;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.view.View;

import com.wq.photo.R;

import java.util.ArrayList;


/**
 * Created by LZ on 2016/9/23.
 */
public class ImageAlbum {
    private int position;
    private ArrayList<String> list = new ArrayList<>();

    private ImageAlbum() {

    }


    public static ImageAlbum instance() {
        ImageAlbum imageAlbum = new ImageAlbum();
        return imageAlbum;
    }

    public ImageAlbum position(int position) {
        this.position = position;
        return this;
    }

    public ImageAlbum imageList(ArrayList list) {
        this.list = list;
        return this;
    }

    /**
     * view中必须带transitionName:R.string.album_image_share;
     *
     * @param context
     * @param shareView
     */
    public void start(Context context, View shareView) {
        Intent intent = new Intent(context, ImageAlbumActivity.class);
        intent.putExtra(ImageAlbumActivity.POSITION, position);
        intent.putExtra(ImageAlbumActivity.IMAGES, list);
        if (shareView != null) {

//            ActivityOptionsCompat options = ActivityOptionsCompat.makeScaleUpAnimation(shareView,(int)shareView.getX()+50,(int)shareView.getY()+50,shareView.getWidth()+100,shareView.getHeight()+100);
//            ImageView imageView= (ImageView) shareView;
//            ActivityOptionsCompat options = ActivityOptionsCompat.makeThumbnailScaleUpAnimation(shareView,imageView.getDrawingCache(),(int)shareView.getX(),(int)shareView.getY());
            ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation((Activity) context, shareView, context.getString(R.string.album_image_share));
            ActivityCompat.startActivity((Activity) context, intent,
                                         options.toBundle());
        } else {
            ActivityCompat.startActivity((Activity) context, intent, null);
        }
    }

    public void start(Context context) {
        start(context, null);
    }
}
