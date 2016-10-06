package com.wrk.mymeadiaplayer.loader;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.util.LruCache;
import android.widget.ImageView;

import com.wrk.mymeadiaplayer.R;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * Created by Mr.W on 2016/9/6.
 */
public class ImageLoader {

    private Context context;
    private LruCache<String, Bitmap> mCaches;


    public ImageLoader(Context context) {
        this.context = context;
        //  获取最大可用内存
        int maxMemory = (int) Runtime.getRuntime().maxMemory();
        int cacheSize = maxMemory / 3;

        mCaches = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getByteCount();
            }
        };
        //  获取文件存储目录
    }

    //  将数据增加到缓存
    public void addBitmapToCache(Bitmap bitmap, String url) {
        if (getBitmapFromCache(url) == null) {
            mCaches.put(url, bitmap);
        }
    }

    //  从缓存中获取数据
    public Bitmap getBitmapFromCache(String url) {
        return mCaches.get(url);
    }


    //  得到图片
    public void showImageByAsyncTask(ImageView imageView, String url) {
        Bitmap bitmap = getBitmapFromCache(url);
        if (bitmap == null) {
            new NewsAsyncTask(imageView, url).execute(url);
        } else {
            if (imageView.getTag().equals(url)) {
                imageView.setImageBitmap(bitmap);
            } else {
                imageView.setImageResource(R.drawable.video_default);
            }
        }

    }

    //  下载图片
    private class NewsAsyncTask extends AsyncTask<String, Void, Bitmap> {

        private ImageView mImageView;
        private String mUrl;


        public NewsAsyncTask(ImageView imageView, String url) {
            mImageView = imageView;
            mUrl = url;
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            Bitmap bitmap;
            String url = params[0];
            InputStream is = null;
            try {
                is = new URL(url).openStream();
            } catch (IOException e) {
                e.printStackTrace();
            }
            bitmap = BitmapFactory.decodeStream(is);
            addBitmapToCache(bitmap, mUrl);
//            addBitmapToFile(bitmap, mUrl);

            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if (mImageView.getTag().equals(mUrl)) {
                Log.e("888", "mImageView.getTag():" + mImageView.getTag());
                mImageView.setImageBitmap(bitmap);
            } else {
                mImageView.setImageResource(R.drawable.video_default);
            }
        }
    }

}
