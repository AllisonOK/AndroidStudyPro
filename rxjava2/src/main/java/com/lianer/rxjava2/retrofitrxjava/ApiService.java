package com.lianer.rxjava2.retrofitrxjava;

import io.reactivex.Observable;
import retrofit2.http.GET;

interface ApiService {

    @GET("api/4/themes")
    Observable<NewsBean> getNews();

}
