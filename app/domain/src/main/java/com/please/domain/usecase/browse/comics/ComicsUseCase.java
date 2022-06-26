package com.please.domain.usecase.browse.comics;
import com.please.core.Result;
import com.please.domain.model.Comic;

import io.reactivex.rxjava3.core.Flowable;

public interface ComicsUseCase {
    Flowable<Result<Comic>> getComic(Comic currentComic);
    Flowable<Result<Comic>> getNextComic(Comic currentComic);
    Flowable<Result<Comic>> getPreviousComic(Comic currentComic);
}

