package com.please.domain.usecase.browse.favorite;

import com.please.core.Result;
import com.please.domain.model.Comic;

import java.util.List;

import io.reactivex.rxjava3.core.Flowable;

public interface BrowseFavoriteComicsUseCaseOutput {
    Flowable<Result<List<Comic>>> getFavorites();
}
