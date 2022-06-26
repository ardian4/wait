package com.please.wait.di;

import android.content.Context;

import androidx.room.Room;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.please.data.datastore.ComicsStorage;
import com.please.data.datastore.LruCacheStorage;
import com.please.data.datastore.favorite.FavoriteComicDataStore;
import com.please.data.datastore.favorite.FavoriteComicDataStoreImpl;
import com.please.data.network.services.ApiEndpoints;
import com.please.data.network.services.ComicsServiceApi;
import com.please.data.network.services.ComicsServiceApiImpl;
import com.please.data.persistence.ComicsDatabase;
import com.please.data.persistence.FavoriteComicDOA;
import com.please.data.repository.comics.IncrementalPreloadComicsServiceImpl;
import com.please.data.repository.comics.PreloadComicsService;

import javax.inject.Singleton;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.qualifiers.ApplicationContext;
import dagger.hilt.components.SingletonComponent;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
@InstallIn(SingletonComponent.class)
public abstract class DataModule {

    @Binds
    abstract ComicsServiceApi bindComicsServiceApi(ComicsServiceApiImpl retrofit);

    @Binds
    @Singleton
    abstract ComicsStorage bindComicsStorage(LruCacheStorage cacheStorage);

    @Binds
    abstract FavoriteComicDataStore provideComicDataStore(FavoriteComicDataStoreImpl favoriteComicDataStore);

    @Module
    @InstallIn(SingletonComponent.class)
    public static class DataModuleProvider {

        @Singleton
        @Provides
        ComicsDatabase provideYourDatabase(@ApplicationContext Context app) {
            return Room.databaseBuilder(app, ComicsDatabase.class, "database").build();
        }

        @Singleton
        @Provides
        FavoriteComicDOA provideFavoriteDOA(ComicsDatabase roomDatabase) {
            return roomDatabase.getFavoriteDOA();
        }

        @Provides
        @Singleton
        ApiEndpoints provideRetrofitAPI() {
            Gson gson = new GsonBuilder()
                    .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();
            GsonConverterFactory gsonConverterFactory = GsonConverterFactory.create(gson);

            //todo add api url through BuildConfig
            final Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("https://xkcd.com/")
                    .addConverterFactory(gsonConverterFactory)
                    .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                    .build();
            return retrofit.create(ApiEndpoints.class);
        }

        @Provides
        PreloadComicsService providePreloadService(ComicsStorage cache, ComicsServiceApi api,
                                                   FavoriteComicDataStore favoriteComicDataStore) {
            return new IncrementalPreloadComicsServiceImpl(10, cache, api, favoriteComicDataStore);
        }
    }
}
