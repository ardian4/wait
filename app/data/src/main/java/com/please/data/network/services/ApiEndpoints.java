package com.please.data.network.services;

import com.please.data.network.models.XKCDComic;

import io.reactivex.rxjava3.core.Flowable;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ApiEndpoints {

    @GET("info.0.json")
    Flowable<XKCDComic> getComic();

    @GET("/{id}/info.0.json")
    Flowable<XKCDComic> getComic(@Path("id") int id);
}
