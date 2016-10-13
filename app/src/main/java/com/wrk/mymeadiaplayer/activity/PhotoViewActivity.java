package com.wrk.mymeadiaplayer.activity;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.wrk.mymeadiaplayer.R;

import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

public class PhotoViewActivity extends Activity {

    private PhotoView pt_showimage;
    private PhotoViewAttacher mAttacher;
    private String imageUrl;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_view);

        pt_showimage = (PhotoView) findViewById(R.id.pt_showimage);

        imageUrl = getIntent().getStringExtra("imageurl");

        Glide.with(this).load(imageUrl).asBitmap().into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
               pt_showimage.setImageBitmap(resource);
            }
        });

        mAttacher = new PhotoViewAttacher(pt_showimage);

        mAttacher.update();

    }
}
