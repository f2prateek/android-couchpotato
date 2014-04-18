package com.f2prateek.couchpotato.data.api.moviedb;

import com.f2prateek.couchpotato.data.api.moviedb.model.DiscoverMoviesResponse;
import com.f2prateek.couchpotato.data.api.moviedb.model.TMDbConfiguration;
import retrofit.http.GET;
import rx.Observable;

public interface TMDbService {
  @GET("/movie/popular") Observable<DiscoverMoviesResponse> popular();

  @GET("/configuration") Observable<TMDbConfiguration> configuration();
}