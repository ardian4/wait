package com.please.data.persistence;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(version = 1, entities = {FavoriteComic.class})
public abstract class ComicsDatabase extends RoomDatabase {
    public abstract FavoriteComicDOA getFavoriteDOA();
}
