package com.please.wait.ui.home;

import static junit.framework.TestCase.assertEquals;

import androidx.annotation.NonNull;
import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.Observer;

import com.please.core.Result;
import com.please.domain.model.Comic;
import com.please.domain.usecase.browse.comics.ComicsUseCase;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import io.reactivex.rxjava3.core.Flowable;

@RunWith(JUnit4.class)
public class BrowseComicTest {
    @Rule
    public TestRule rule = new InstantTaskExecutorRule();
    FakeComicsUseCase fakeComicsUseCase = new FakeComicsUseCase();

    private BrowseComicsViewModel viewModel = new BrowseComicsViewModel(fakeComicsUseCase, currentComic -> {
        boolean favoriteStatus = !currentComic.isFavorite();
        currentComic.setFavorite(favoriteStatus);
        return Flowable.just(Result.successOf(currentComic));
    });

    @Test
    public void getCurrentComic() {
        viewModel.setup();
        viewModel.getComicLiveData().observeForever(new Observer<Result<Comic>>() {
            @Override
            public void onChanged(Result<Comic> comicResult) {
                viewModel.getComicLiveData().removeObserver(this);
            }
        });
        Result<Comic> value = viewModel.getComicLiveData().getValue();
        assert value != null;
        assertEquals(Objects.requireNonNull(fakeComicsUseCase.comicMap.get(fakeComicsUseCase.lastComicId)).getId(),
                value.getData().getId()
        );

    }


    private static class FakeComicsUseCase implements ComicsUseCase {
        Integer lastComicId = 10;
        Map<Integer, Comic> comicMap = new HashMap<>();

        {
            for (int i = lastComicId; i > 0; i--) {
                Comic comic = new Comic();
                comic.setId(i);
                comicMap.put(i, comic);
            }
        }

        public Flowable<Result<Comic>> getComic(Comic currentComic) {
            int comicId = currentComic == null ? lastComicId : currentComic.getId();
            Comic comic = comicMap.get(comicId);
            Result<Comic> comicResult = Result.successOf(comic);
            return Flowable.just(comicResult);
        }

        @Override
        public Flowable<Result<Comic>> getNextComic(Comic currentComic) {
            return getComic(currentComic.getId() + 1);
        }

        @NonNull
        private Flowable<Result<Comic>> getComic(int comicId) {
            Comic comic = comicMap.get(comicId);
            if (comic == null) {
                String msg = String.format("Comic with id: %d not found", comicId);
                return Flowable.just(Result.errorOf(new Throwable(msg)));
            }
            return Flowable.just(Result.successOf(comic));
        }

        @Override
        public Flowable<Result<Comic>> getPreviousComic(Comic currentComic) {
            return getComic(currentComic.getId() - 1);
        }
    }
}
