package com.please.domain.usecase.browse.comics;

import com.please.core.Result;
import com.please.domain.model.Comic;

import io.reactivex.rxjava3.core.Flowable;

public interface ComicUseCaseOutput {
    Flowable<Result<Comic>> getComic();
    Flowable<Result<Comic>> getComic(int id);
}
