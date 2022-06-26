package com.please.data.persistence;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;

@Dao
public interface FavoriteComicDOA {

    @Query("SELECT * FROM favorite_comic  ORDER BY id ASC")
    Single<List<FavoriteComic>> getAll();

    @Query("SELECT * FROM favorite_comic WHERE id in (:id)")
    Single<List<FavoriteComic>> loadAllByIds(List<Integer> id);

    @Query("SELECT * FROM favorite_comic WHERE id = :id")
    Single<FavoriteComic> loadByID(int id);

    @Delete
    Completable delete(FavoriteComic comic);

    @Insert
    Completable insert(FavoriteComic favoriteComic);

}