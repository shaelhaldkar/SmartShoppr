package com.sukritapp.smartshoppr.util;

import android.app.Application;
import android.content.Context;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

/**
 * Created by abc on 12/25/2016.
 */

public class SmartShopprApp extends Application {

    private final String TAG = AppLog.getClassName();

    private static SmartShopprApp sSmartShopprApp = null;

    public static SmartShopprApp getApplication() {
        return sSmartShopprApp;
    }

    @Override
    public void onCreate() {
        AppLog.enter(TAG, AppLog.getMethodName());

        super.onCreate();
        sSmartShopprApp = this;
        initImageLoader(getApplicationContext());
        AppLog.exit(TAG, AppLog.getMethodName());
    }
    public static void initImageLoader(Context context) {
        // This configuration tuning is custom. You can tune every option, you may tune some of them,
        // or you can create default configuration by
        //  ImageLoaderConfiguration.createDefault(this);
        // method.
        ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder(context);
        config.threadPriority(Thread.NORM_PRIORITY - 2);
        config.denyCacheImageMultipleSizesInMemory();
        config.diskCacheFileNameGenerator(new Md5FileNameGenerator());
        config.diskCacheSize(50 * 1024 * 1024); // 50 MiB
        config.tasksProcessingOrder(QueueProcessingType.LIFO);
        config.writeDebugLogs(); // Remove for release app

        // Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(config.build());
    }
}
