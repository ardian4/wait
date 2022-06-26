package com.please.wait.ui.home;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;
import static junit.framework.TestCase.assertTrue;

import androidx.annotation.NonNull;
import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.Observer;

import com.please.core.Result;
import com.please.domain.model.Comic;
import com.please.domain.usecase.browse.comics.ComicsUseCase;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import io.reactivex.rxjava3.core.Flowable;

public class BrowseComicsViewModelTest {

    @Rule
    public TestRule rule = new InstantTaskExecutorRule();
    private FakeComicsUseCase fakeComicsUseCase = new FakeComicsUseCase();

    private BrowseComicsViewModel viewModel = new BrowseComicsViewModel(fakeComicsUseCase, currentComic -> {
        boolean favoriteStatus = !currentComic.isFavorite();
        currentComic.setFavorite(favoriteStatus);
        return Flowable.just(Result.successOf(currentComic));
    });


    @Test
    public void getComicLiveData() {
        assertNotNull(viewModel.getComicLiveData());
    }

    @Test
    public void setup() {
        viewModel.getComicLiveData().observeForever(new Observer<Result<Comic>>() {
            @Override
            public void onChanged(Result<Comic> comicResult) {
                viewModel.getComicLiveData().removeObserver(this);
            }
        });
        viewModel.setup();

        Result<Comic> actual = viewModel.getComicLiveData().getValue();
        int expected = Objects.requireNonNull(fakeComicsUseCase.comicMap.get(fakeComicsUseCase.lastComicId)).getId();
        assertEquals(expected, Objects.requireNonNull(actual).getData().getId());
    }

    //todo use mock to insert data to livedata so we can test only the previous comic
    @Test
    public void previousComic() {
        Observer<Result<Comic>> observer = comicResult -> {
            //no op
        };
        viewModel.getComicLiveData().observeForever(observer);
        try {
            viewModel.setup();
            viewModel.previousComic();

            Result<Comic> actual = viewModel.getComicLiveData().getValue();
            int expected = Objects.requireNonNull(fakeComicsUseCase.comicMap.get(fakeComicsUseCase.lastComicId - 1)).getId();
            assertNotNull(actual);
            assertEquals(expected, actual.getData().getId());
        } finally {
            viewModel.getComicLiveData().removeObserver(observer);
        }
    }

    @Test
    public void nextComic() {
        Observer<Result<Comic>> observer = comicResult -> {
            //no op
        };
        viewModel.getComicLiveData().observeForever(observer);
        try {
            viewModel.setup();
            viewModel.previousComic();
            viewModel.nextComic();

            Result<Comic> actual = viewModel.getComicLiveData().getValue();
            assertNotNull(actual);
            int expected = Objects.requireNonNull(fakeComicsUseCase.comicMap.get(fakeComicsUseCase.lastComicId)).getId();
            assertEquals(expected, actual.getData().getId());
        } finally {
            viewModel.getComicLiveData().removeObserver(observer);
        }
    }

    @Test
    public void nextComicWithFailure() {
        Observer<Result<Comic>> observer = comicResult -> {
            //no op
        };
        viewModel.getComicLiveData().observeForever(observer);
        try {
            viewModel.nextComic();
            viewModel.nextComic();

            Result<Comic> actual = viewModel.getComicLiveData().getValue();
            assertNotNull(actual);
            assertEquals(actual.getStatus(), Result.RESULT_FAIL);
        } finally {
            viewModel.getComicLiveData().removeObserver(observer);
        }
    }


    @Test
    public void likeAction() {
        Observer<Result<Comic>> observer = comicResult -> {
            //no op
        };
        viewModel.getComicLiveData().observeForever(observer);
        Comic comic = fakeComicsUseCase.comicMap.get(fakeComicsUseCase.lastComicId);
        try {
            viewModel.setup();
            viewModel.likeAction();
            assertTrue(Objects.requireNonNull(comic).isFavorite());
        } finally {
            Objects.requireNonNull(comic).setFavorite(false);
            viewModel.getComicLiveData().removeObserver(observer);
        }
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
                String msg = String.format("Next comic with id: %d not found", comicId);
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