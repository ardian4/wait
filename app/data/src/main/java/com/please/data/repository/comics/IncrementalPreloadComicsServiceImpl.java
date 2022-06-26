package com.please.data.repository.comics;

import com.please.core.Result;
import com.please.data.datastore.ComicsStorage;
import com.please.data.datastore.favorite.FavoriteComicDataStore;
import com.please.data.network.services.ComicsServiceApi;
import com.please.domain.model.Comic;

import org.reactivestreams.Publisher;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.functions.Function;


public class IncrementalPreloadComicsServiceImpl implements PreloadComicsService {
    private final int mPreloadSize;
    private final ComicsStorage mCache;
    private final ComicsServiceApi mComicsServiceApi;
    private final FavoriteComicDataStore mFavoriteComicDataStore;


    @Inject
    public IncrementalPreloadComicsServiceImpl(int preloadSize,
                                               ComicsStorage cache,
                                               ComicsServiceApi comicsServiceApi,
                                               FavoriteComicDataStore mFavoriteComicDataStore) {
        mPreloadSize = preloadSize;
        mCache = cache;
        mComicsServiceApi = comicsServiceApi;
        this.mFavoriteComicDataStore = mFavoriteComicDataStore;
    }

    @Override
    public void attemptToGetComic(int id) {
        if (id < mPreloadSize) {
            return;
        }

        //todo improve: filter which ids we need to preload, check if those ids are in favorite
        // table and map
        for (int i = id - 1; i > id - mPreloadSize; i--) {
            if (mCache.hasComic(i)) {
                continue;
            }

            Flowable<Result<Comic>> resultFlowable = mComicsServiceApi
                    .getComic(i)
                    .flatMap((Function<Result<Comic>, Publisher<Result<Comic>>>) comicResult
                            -> mFavoriteComicDataStore
                            .getComicsWithId(comicResult.getData().getId())
                            .map(comic -> {
                                    comicResult.getData().setFavorite(true);
                                    return comicResult;
                            }).onErrorReturnItem(comicResult)
                            .toFlowable()

                    ).onErrorResumeNext(throwable ->
                            Flowable.fromCallable(() -> Result.errorOf(throwable)))
                    .startWithItem(Result.progress(null));


            mCache.putComic(i, resultFlowable);
        }
    }
}


