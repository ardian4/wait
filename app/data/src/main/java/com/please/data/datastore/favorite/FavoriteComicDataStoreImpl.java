package com.please.data.datastore.favorite;

import com.please.data.common.Mapper;
import com.please.data.datastore.favorite.mapper.ComicToFavoriteComicMapper;
import com.please.data.datastore.favorite.mapper.FavoriteComicToComicMapper;
import com.please.data.persistence.FavoriteComic;
import com.please.data.persistence.FavoriteComicDOA;
import com.please.domain.model.Comic;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class FavoriteComicDataStoreImpl implements FavoriteComicDataStore {

    private final FavoriteComicDOA doa;

    @Inject
    public FavoriteComicDataStoreImpl(FavoriteComicDOA doa) {
        this.doa = doa;
    }

    @Override
    public Completable saveComic(Comic comic) {
        Mapper<FavoriteComic, Comic> mapper = new ComicToFavoriteComicMapper();
        FavoriteComic favoriteComic = mapper.map(comic);
        return doa.insert(favoriteComic).subscribeOn(Schedulers.io());
    }

    @Override
    public Completable removeComic(Comic comic) {
        Mapper<FavoriteComic, Comic> mapper = new ComicToFavoriteComicMapper();
        FavoriteComic favoriteComic = mapper.map(comic);
        return doa.delete(favoriteComic).subscribeOn(Schedulers.io());
    }

    @Override
    public Single<List<Comic>> getAll() {
        Mapper<Comic, FavoriteComic> mapper = new FavoriteComicToComicMapper();
        return doa.getAll().map(favoriteComics -> {
            List<Comic> comics = new ArrayList<>(favoriteComics.size());
            for (FavoriteComic favoriteComic : favoriteComics) {
                comics.add(mapper.map(favoriteComic));
            }
            return comics;
        }).subscribeOn(Schedulers.io());
    }

    @Override
    public Single<Comic> getComicsWithId(int id) {
        Mapper<Comic, FavoriteComic> mapper = new FavoriteComicToComicMapper();
        return doa.loadByID(id).subscribeOn(Schedulers.io()).map(mapper::map);
    }
}
