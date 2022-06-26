package com.please.data.datastore.favorite;

import com.please.domain.model.Comic;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Single;

public interface FavoriteComicDataStore {
    Completable saveComic(Comic comic);
    Completable removeComic(Comic comic);
    Single<List<Comic>> getAll();
    Single<Comic> getComicsWithId(int id);
}
