package com.please.wait.di;

import com.please.wait.BuildConfig;
import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;
import timber.log.Timber;

@Module
@InstallIn(SingletonComponent.class)
public class AppModule {

    @Provides
    Timber.Tree provideLog(){
        return BuildConfig.DEBUG ? new Timber.DebugTree() : null;
    }
}
