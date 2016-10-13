package me.yokeyword.sample;

import android.app.Application;

import org.xutils.x;

/**
 * Created by MrbigW on 2016/10/13.
 * weChat:1024057635
 * Github:MrbigW
 * Usage: -.-
 * -------------------=.=------------------------
 */

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        x.Ext.init(this);
        x.Ext.setDebug(true);

    }
}
