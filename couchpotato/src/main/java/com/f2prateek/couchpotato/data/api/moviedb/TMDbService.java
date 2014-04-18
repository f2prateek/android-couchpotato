package com.f2prateek.couchpotato.data.api.moviedb;

import com.f2prateek.couchpotato.data.api.moviedb.model.DiscoverMoviesResponse;
import com.f2prateek.couchpotato.data.api.moviedb.model.TMDbConfiguration;
import retrofit.http.GET;
import retrofit.http.Query;
import rx.Observable;

public interface TMDbService {
  @GET("/movie/popular") Observable<DiscoverMoviesResponse> popular(@Query("page") int page);

  @GET("/configuration") Observable<TMDbConfiguration> configuration();
}