package com.please.data.network.services;

import com.please.core.Result;
import com.please.domain.model.Comic;

import io.reactivex.rxjava3.core.Flowable;

public interface ComicsServiceApi {
    Flowable<Result<Comic>> getComic();
    Flowable<Result<Comic>> getComic(int id);
}
