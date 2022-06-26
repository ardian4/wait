package com.please.domain.usecase.favorite;
import com.please.core.Result;
import com.please.domain.model.Comic;

import io.reactivex.rxjava3.core.Flowable;

public interface FavoriteStatusUseCase {
    Flowable<Result<Comic>> changeFavoriteStatus(Comic currentComic);
}

