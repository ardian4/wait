package com.please.wait.di;

import com.please.data.repository.comics.BrowseComicsRepository;
import com.please.domain.usecase.browse.comics.ComicUseCaseImpl;
import com.please.domain.usecase.browse.comics.ComicUseCaseOutput;
import com.please.domain.usecase.browse.comics.ComicsUseCase;
import com.please.domain.usecase.favorite.FavoriteStatusUseCase;
import com.please.domain.usecase.favorite.FavoriteStatusUseCaseImpl;
import com.please.domain.usecase.favorite.FavoriteStatusUseCaseOutput;

import dagger.Binds;
import dagger.Module;
import dagger.hilt.InstallIn;
import dagger.hilt.android.components.ViewModelComponent;

@Module
@InstallIn(ViewModelComponent.class)
public abstract class BrowseComicsViewModelModule {
    @Binds
    abstract ComicsUseCase bindComicsUseCase(ComicUseCaseImpl instance);

    @Binds
    abstract ComicUseCaseOutput bindComicUseCaseOutput(BrowseComicsRepository repository);

    @Binds
    abstract FavoriteStatusUseCase bindFavoriteStatusUseCase(FavoriteStatusUseCaseImpl repository);

    @Binds
    abstract FavoriteStatusUseCaseOutput bindFavoriteStatusUseCaseOutput(BrowseComicsRepository repository);
}
