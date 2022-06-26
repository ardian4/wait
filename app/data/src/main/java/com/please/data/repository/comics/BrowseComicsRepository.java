package com.please.data.repository.comics;

import androidx.annotation.NonNull;

import com.please.core.Result;
import com.please.data.datastore.ComicsStorage;
import com.please.data.datastore.favorite.FavoriteComicDataStore;
import com.please.data.network.services.ComicsServiceApi;
import com.please.domain.model.Comic;
import com.please.domain.usecase.browse.comics.ComicUseCaseOutput;
import com.please.domain.usecase.favorite.FavoriteStatusUseCaseOutput;

import org.reactivestreams.Publisher;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.functions.Function;

public class BrowseComicsRepository implements ComicUseCaseOutput, FavoriteStatusUseCaseOutput {

    private final ComicsServiceApi mComicsServiceApi;
    private final ComicsStorage mCache;
    private final PreloadComicsService mPreloadComicsService;
    private final FavoriteComicDataStore mFavoriteComicDataStore;

    @Inject
    public BrowseComicsRepository(ComicsServiceApi api,
                                  ComicsStorage cache,
                                  PreloadComicsService preloadComicsService,
                                  FavoriteComicDataStore favoriteComicDataStore) {
        mCache = cache;
        mPreloadComicsService = preloadComicsService;
        mComicsServiceApi = api;
        mFavoriteComicDataStore = favoriteComicDataStore;
    }

    @Override
    public Flowable<Result<Comic>> getComic() {
        return mComicsServiceApi
                .getComic().flatMap((Function<Result<Comic>, Publisher<Result<Comic>>>) this::checkAndApplyFavoriteStatus
                ).map(comicResult -> {
                    mCache.putComic(comicResult.getData().getId(),
                            Flowable.fromCallable(() -> comicResult));
                    return comicResult;
                }).onErrorResumeNext(throwable ->
                        Flowable.fromCallable(() -> Result.errorOf(throwable)))
                .startWithItem(Result.progress(null));
    }


    /**
     * Modify comic if is one of the favorites
     * <p>
     * In case comic is one of the favorites, modify  otherwise the flow will throw ex.
     * then return the comic without modifying
     *
     * @param comicResult comic to modify
     * @return comicResult
     */
    @NonNull
    private Flowable<Result<Comic>> checkAndApplyFavoriteStatus(Result<Comic> comicResult) {
        return mFavoriteComicDataStore
                .getComicsWithId(comicResult.getData().getId())
                .map(comic -> {
                    comic.setFavorite(true);
                    return Result.successOf(comic);
                })
                .onErrorReturn(throwable -> comicResult).toFlowable();
    }

    @Override
    public Flowable<Result<Comic>> getComic(int id) {
        Flowable<Result<Comic>> result = mCache.getComic(id);

        if (result != null) {
            return result;
        }

        result = mComicsServiceApi
                .getComic(id)
                .flatMap(this::checkAndApplyFavoriteStatus)
                .onErrorResumeNext(throwable ->
                        Flowable.fromCallable(() -> Result.errorOf(throwable)))
                .startWithItem(Result.progress(null));


        mCache.putComic(id, result);
        mPreloadComicsService.attemptToGetComic(id);
        return result;
    }

    @Override
    public Flowable<Result<Comic>> setAsFavorite(Comic comic) {
        Completable completable = mFavoriteComicDataStore.saveComic(comic);
        return completable.andThen(Flowable.fromCallable(() -> {
            comic.setFavorite(true);
            return Result.successOf(comic);
        })).onErrorReturn(Result::errorOf);
    }

    @Override
    public Flowable<Result<Comic>> removeAsFavorite(Comic comic) {
        Completable completable = mFavoriteComicDataStore.removeComic(comic);
        return completable.andThen(Flowable.fromCallable(() -> {
            comic.setFavorite(false);
            return Result.successOf(comic);
        })).onErrorReturn(Result::errorOf);
    }
}
