package com.please.data.datastore;

import com.please.core.Result;
import com.please.domain.model.Comic;

import io.reactivex.rxjava3.core.Flowable;

//todo rename
public interface ComicsStorage {
    Flowable<Result<Comic>> getComic(int id);

    void putComic(int id, Flowable<Result<Comic>> comicResult);

    boolean hasComic(int id);
}
