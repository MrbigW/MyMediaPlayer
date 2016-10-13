package com.wrk.mymeadiaplayer.activity;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.ImageViewTarget;
import com.wrk.mymeadiaplayer.R;

import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

public class PhotoViewActivity extends Activity {

    private PhotoView pt_showimage;
    private PhotoViewAttacher mAttacher;
    private String imageUrl;
    private ImageButton ib_back;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_view);
        ib_back = (ImageButton) findViewById(R.id.ib_back);
        pt_showimage = (PhotoView) findViewById(R.id.pt_showimage);

        imageUrl = getIntent().getStringExtra("imageurl");

        Glide.with(this).load(imageUrl).asBitmap().into(new ImageViewTarget<Bitmap>(pt_showimage) {

            @Override
            protected void setResource(Bitmap resource) {
                pt_showimage.setImageBitmap(resource);
            }

        });

        ib_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                mAttacher.cleanup();
            }
        });


        mAttacher = new PhotoViewAttacher(pt_showimage);

        mAttacher.update();

    }
}
