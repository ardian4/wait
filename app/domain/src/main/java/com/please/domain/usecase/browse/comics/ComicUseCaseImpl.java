package com.please.domain.usecase.browse.comics;

import com.please.core.Result;
import com.please.domain.model.Comic;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.BackpressureStrategy;
import io.reactivex.rxjava3.core.Flowable;

public class ComicUseCaseImpl implements ComicsUseCase {

    private final ComicUseCaseOutput mDelegate;

    @Inject
    public ComicUseCaseImpl(ComicUseCaseOutput delegate) {
        mDelegate = delegate;
    }

    @Override
    public Flowable<Result<Comic>> getComic(Comic currentComic) {
        if (currentComic == null) {
            return this.mDelegate.getComic();
        }
        return this.mDelegate.getComic(currentComic.getId());
    }

    @Override
    public Flowable<Result<Comic>> getNextComic(Comic currentComic) {
        if (currentComic == null) {
            return createErrorWhenIdIsMissing("Can not fetch next comic!");
        }
        return startGettingComic(currentComic.getId() + 1);
    }

    @Override
    public Flowable<Result<Comic>> getPreviousComic(Comic currentComic) {
        if (currentComic == null) {
            return createErrorWhenIdIsMissing("Can not fetch previous comic!");
        }
        return startGettingComic(currentComic.getId() - 1);
    }

    private Flowable<Result<Comic>> createErrorWhenIdIsMissing(String message) {
        Exception exception = new Exception(message);
        return Flowable.create(emitter -> {
            emitter.onNext(Result.errorOf(exception));
            emitter.onComplete();
        }, BackpressureStrategy.BUFFER);
    }

    private Flowable<Result<Comic>> createOnProgress() {
        return Flowable.create(emitter -> {
            emitter.onNext(Result.progress());
            emitter.onComplete();
        }, BackpressureStrategy.BUFFER);
    }

    private Flowable<Result<Comic>> startGettingComic(int id) {
        return mDelegate.getComic(id);
    }
}
