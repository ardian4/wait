package com.please.domain.usecase.browse.favorite;

import com.please.core.Result;
import com.please.domain.model.Comic;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Flowable;

public class BrowseFavoriteComicsUseCaseImpl implements BrowseFavoriteComicsUseCase {

    private final BrowseFavoriteComicsUseCaseOutput mDelegate;

    @Inject
    public BrowseFavoriteComicsUseCaseImpl(BrowseFavoriteComicsUseCaseOutput delegate) {
        mDelegate = delegate;
    }

    @Override
    public Flowable<Result<List<Comic>>> getFavorites() {
        return mDelegate.getFavorites();
    }
}
