package com.please.domain.usecase.favorite;

import com.please.core.Result;
import com.please.domain.model.Comic;

import io.reactivex.rxjava3.core.Flowable;

public interface FavoriteStatusUseCaseOutput {
    Flowable<Result<Comic>> setAsFavorite(Comic comic);
    Flowable<Result<Comic>> removeAsFavorite(Comic comic);
}
