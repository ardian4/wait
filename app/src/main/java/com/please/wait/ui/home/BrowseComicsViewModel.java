package com.please.wait.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.LiveDataReactiveStreams;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModel;

import com.please.core.Result;
import com.please.domain.model.Comic;
import com.please.domain.usecase.browse.comics.ComicsUseCase;
import com.please.domain.usecase.favorite.FavoriteStatusUseCase;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.rxjava3.core.Flowable;

@HiltViewModel
public class BrowseComicsViewModel extends ViewModel {

    private final ComicsUseCase comicUseCase;
    private final FavoriteStatusUseCase mFavoriteStatusUseCase;
    private final MediatorLiveData<Result<Comic>> currentComic = new MediatorLiveData<>();
    private Comic lastValidComic; // retain comic in case livedata value gets corrupted

    @Inject
    public BrowseComicsViewModel(ComicsUseCase comicUseCase, FavoriteStatusUseCase favoriteStatusUseCase) {
        this.comicUseCase = comicUseCase;
        this.mFavoriteStatusUseCase = favoriteStatusUseCase;
    }


    public LiveData<Result<Comic>> getComicLiveData() {
        return currentComic;
    }

    public void setup() {
        if (currentComic.getValue() != null) {
            return;
        }
        getCurrentComic();
    }

    private void getCurrentComic() {
        Flowable<Result<Comic>> comicFlowable = comicUseCase.getComic(null);
        convertToLiveData(comicFlowable);
    }

    public void previousComic() {
        Comic comic = lastValidCommit();
        if (comic == null) {
            return;
        }
        Flowable<Result<Comic>> previousComic = comicUseCase.getPreviousComic(comic);
        convertToLiveData(previousComic);
    }

    public void nextComic() {
        Comic comic = lastValidCommit();
        if (comic == null) {
            return;
        }
        Flowable<Result<Comic>> previousComic = comicUseCase.getNextComic(comic);
        convertToLiveData(previousComic);
    }

    public void likeAction() {
        Comic comic = lastValidCommit();
        if (comic == null) {
            return;
        }
        Flowable<Result<Comic>> resultFlowable = mFavoriteStatusUseCase.changeFavoriteStatus(comic);
        convertToLiveData(resultFlowable);
    }

    private void convertToLiveData(Flowable<Result<Comic>> comicFlowable) {
        LiveData<Result<Comic>> comicLiveData = LiveDataReactiveStreams.fromPublisher(comicFlowable);
        addSource(comicLiveData);
    }

    private void addSource(LiveData<Result<Comic>> comicLiveData) {
        currentComic.addSource(comicLiveData, comicResult -> {
            currentComic.postValue(comicResult);
            if (comicResult != null) {
                if (comicResult.getStatus() == Result.RESULT_PROGRESS) {
                    return; // do not remove source, there maybe another emit
                }
                if (comicResult.getStatus() == Result.RESULT_SUCCESS) {
                    lastValidComic = comicResult.getData();
                }
            }
            currentComic.removeSource(comicLiveData);
        });
    }

    private Comic lastValidCommit() {
        if (currentComic.getValue() == null || currentComic.getValue().getData() == null) {
            return lastValidComic;
        } else {
            return currentComic.getValue().getData();
        }
    }
}
