package com.please.data.repository.comics;

import com.please.core.Result;
import com.please.data.datastore.favorite.FavoriteComicDataStore;
import com.please.domain.model.Comic;
import com.please.domain.usecase.browse.favorite.BrowseFavoriteComicsUseCaseOutput;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Flowable;


public class BrowseFavoriteComicsRepository implements BrowseFavoriteComicsUseCaseOutput {

    private final FavoriteComicDataStore mFavoriteComicDataStore;

    @Inject
    public BrowseFavoriteComicsRepository(FavoriteComicDataStore favoriteComicDataStore) {
        this.mFavoriteComicDataStore = favoriteComicDataStore;
    }

    @Override
    public Flowable<Result<List<Comic>>> getFavorites() {
        return mFavoriteComicDataStore
                .getAll()
                .map(Result::successOf)
                .toFlowable()
                .onErrorResumeNext(throwable -> Flowable.fromCallable(() -> Result.errorOf(throwable)))
                .startWithItem(Result.progress(null));
    }
}
