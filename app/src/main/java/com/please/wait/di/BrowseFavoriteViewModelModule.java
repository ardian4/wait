package com.please.wait.di;

import com.please.data.repository.comics.BrowseFavoriteComicsRepository;
import com.please.domain.usecase.browse.favorite.BrowseFavoriteComicsUseCase;
import com.please.domain.usecase.browse.favorite.BrowseFavoriteComicsUseCaseImpl;
import com.please.domain.usecase.browse.favorite.BrowseFavoriteComicsUseCaseOutput;

import dagger.Binds;
import dagger.Module;
import dagger.hilt.InstallIn;
import dagger.hilt.android.components.ViewModelComponent;

@Module
@InstallIn(ViewModelComponent.class)
public abstract class BrowseFavoriteViewModelModule {
    @Binds
    abstract BrowseFavoriteComicsUseCase bindBrowseFavoriteComicsUseCase(BrowseFavoriteComicsUseCaseImpl instance);


    @Binds
    abstract BrowseFavoriteComicsUseCaseOutput bindBrowseFavoriteComicsUseCaseOutput(BrowseFavoriteComicsRepository repository);

}
