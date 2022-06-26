package com.please.wait.ui.favorite;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.LiveDataReactiveStreams;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModel;

import com.please.core.Result;
import com.please.domain.model.Comic;
import com.please.domain.usecase.browse.favorite.BrowseFavoriteComicsUseCase;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.rxjava3.core.Flowable;

@HiltViewModel
public class BrowseFavoriteViewModel extends ViewModel {

    private final BrowseFavoriteComicsUseCase mBrowseFavoriteComicsUseCase;
    private final MediatorLiveData<Result<List<Comic>>> mComics=new MediatorLiveData<>();

    @Inject
    public BrowseFavoriteViewModel(BrowseFavoriteComicsUseCase browseFavoriteComicsUseCase) {
        mBrowseFavoriteComicsUseCase = browseFavoriteComicsUseCase;
    }

    public LiveData<Result<List<Comic>>> getComicLiveData() {
        return mComics;
    }

    public void setup() {
        if (mComics.getValue() != null) {
            return;
        }
        getFavorite();
    }

    private void getFavorite() {
        Flowable<Result<List<Comic>>> comicFlowable = mBrowseFavoriteComicsUseCase.getFavorites();
        convertToLiveData(comicFlowable);
    }
    
    private void convertToLiveData(Flowable<Result<List<Comic>>> comicFlowable) {
        LiveData<Result<List<Comic>>> favorite = LiveDataReactiveStreams.fromPublisher(comicFlowable);
        mComics.addSource(favorite, comicResult -> {
            mComics.postValue(comicResult);
            if (comicResult != null) {
                if (comicResult.getStatus() == Result.RESULT_PROGRESS) {
                    return; // do not remove source, there maybe another emit
                }
            }
            mComics.removeSource(favorite);
        });
    }
}
