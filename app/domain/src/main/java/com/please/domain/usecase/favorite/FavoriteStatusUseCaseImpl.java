package com.please.domain.usecase.favorite;

import com.please.core.Result;
import com.please.domain.model.Comic;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Flowable;

public class FavoriteStatusUseCaseImpl implements FavoriteStatusUseCase {

    private final FavoriteStatusUseCaseOutput mDelegate;

    @Inject
    public FavoriteStatusUseCaseImpl(FavoriteStatusUseCaseOutput delegate) {
        mDelegate = delegate;
    }


    @Override
    public Flowable<Result<Comic>> changeFavoriteStatus(Comic currentComic) {
        if(currentComic.isFavorite()){
            return mDelegate.removeAsFavorite(currentComic);
        }else{
            return mDelegate.removeAsFavorite(currentComic);
        }
    }
}
