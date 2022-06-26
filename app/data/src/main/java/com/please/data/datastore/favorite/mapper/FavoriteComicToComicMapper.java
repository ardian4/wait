package com.please.data.datastore.favorite.mapper;

import com.please.data.common.Mapper;
import com.please.data.persistence.FavoriteComic;
import com.please.domain.model.Comic;

public class FavoriteComicToComicMapper implements Mapper<Comic, FavoriteComic> {
    @Override
    public Comic map(FavoriteComic value) {
        Comic comic = new Comic();
        comic.setId(value.getId());
        comic.setTitle(value.getTitle());
        comic.setDescription(value.getDescription());
        comic.setImage(value.getImage());
        comic.setFavorite(true);
        return comic;
    }
}
