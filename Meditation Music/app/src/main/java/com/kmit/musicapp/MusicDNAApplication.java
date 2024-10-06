package com.kmit.musicapp;

import android.content.Context;

import com.downloader.PRDownloader;
import com.downloader.PRDownloaderConfig;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;
import com.zeugmasolutions.localehelper.LocaleAwareApplication;

//import com.crashlytics.android.Crashlytics;

//import io.fabric.sdk.android.Fabric;

/**
 * Created by Harjot on 28-Aug-16.
 */
public class MusicDNAApplication extends LocaleAwareApplication {

    private RefWatcher refWatcher;

    public static RefWatcher getRefWatcher(Context context) {
        MusicDNAApplication application = (MusicDNAApplication) context.getApplicationContext();
        return application.refWatcher;
    }

    @Override
    public void onCreate() {
        super.onCreate();
//        Fabric.with(this, new Crashlytics());
        refWatcher = LeakCanary.install(this);

        PRDownloaderConfig config = PRDownloaderConfig.newBuilder()
                .setDatabaseEnabled(true)
                .build();
        PRDownloader.initialize(this, config);
    }

}
