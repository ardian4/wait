package com.please.data.datastore;

import android.util.LruCache;
import com.please.core.Result;
import com.please.domain.model.Comic;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Flowable;


public class LruCacheStorage implements ComicsStorage {

    @Inject
    public LruCacheStorage(){
    }

    private final LruCache<Integer, Flowable<Result<Comic>>> lruCache = new LruCache<>(30);


    @Override
    public Flowable<Result<Comic>> getComic(int id) {
        return lruCache.get(id);
    }

    @Override
    public void putComic(int id, Flowable<Result<Comic>> comicResult) {
        lruCache.put(id, comicResult);
    }

    @Override
    public boolean hasComic(int id) {
        return lruCache.get(id) != null;
    }
}
