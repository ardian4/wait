package com.please.data.datastore.favorite.mapper;

import com.please.data.common.Mapper;
import com.please.data.persistence.FavoriteComic;
import com.please.domain.model.Comic;

public class ComicToFavoriteComicMapper implements Mapper<FavoriteComic, Comic> {
    @Override
    public FavoriteComic map(Comic value) {
        FavoriteComic favoriteComic = new FavoriteComic();
        favoriteComic.setId(value.getId());
        favoriteComic.setTitle(value.getTitle());
        favoriteComic.setDescription(value.getDescription());
        favoriteComic.setImage(value.getImage());
        favoriteComic.setCreatedAt(System.currentTimeMillis());
        return favoriteComic;
    }
}
