package com.please.data.network.mapper;

import com.please.core.Result;
import com.please.data.common.Mapper;
import com.please.data.network.models.XKCDComic;
import com.please.domain.model.Comic;

//todo add comment why we need to keep in simple class
public class XCToComicMapper implements Mapper<Result<Comic>, XKCDComic> {
    @Override
    public Result<Comic> map(XKCDComic value) {
        Comic comic = new Comic();
        comic.setId(value.getNum());
        comic.setImage(value.getImg());
        comic.setTitle(value.getTitle());
        comic.setDescription(value.getAlt());
        return Result.successOf(comic);
    }
}
