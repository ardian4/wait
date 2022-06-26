package com.please.data.network.services;

import com.please.core.Result;
import com.please.data.common.Mapper;
import com.please.data.network.mapper.XCToComicMapper;
import com.please.data.network.models.XKCDComic;
import com.please.domain.model.Comic;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Flowable;

public class ComicsServiceApiImpl implements ComicsServiceApi {
    private final ApiEndpoints service;

    @Inject
    public ComicsServiceApiImpl(ApiEndpoints service) {
        this.service = service;
    }

    @Override
    public Flowable<Result<Comic>> getComic() {
        Mapper<Result<Comic>, XKCDComic> mapper = new XCToComicMapper();
        return service.getComic().map(mapper::map);
    }

    @Override
    public Flowable<Result<Comic>> getComic(int id) {
        Mapper<Result<Comic>, XKCDComic> mapper = new XCToComicMapper();
        return service.getComic(id).map(mapper::map);
    }
}
