package com.please.wait;

import android.app.Application;

import javax.inject.Inject;

import dagger.hilt.android.HiltAndroidApp;
import timber.log.Timber;

@HiltAndroidApp
public class WaitApplication extends Application {

    @Inject
    Timber.Tree tree;


    @Override
    public void onCreate() {
        super.onCreate();
        if (tree != null) {
            Timber.plant(tree);
        }
    }
}
