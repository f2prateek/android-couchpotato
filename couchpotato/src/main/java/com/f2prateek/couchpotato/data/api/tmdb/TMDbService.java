/*
 * Copyright 2014 Prateek Srivastava (@f2prateek)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.f2prateek.couchpotato.data.api.tmdb;

import com.f2prateek.couchpotato.data.api.tmdb.model.Configuration;
import com.f2prateek.couchpotato.data.api.tmdb.model.Images;
import com.f2prateek.couchpotato.data.api.tmdb.model.TMDbMovie;
import com.f2prateek.couchpotato.data.api.tmdb.model.MovieCollectionResponse;
import com.f2prateek.couchpotato.data.api.tmdb.model.MovieCreditsResponse;
import com.f2prateek.couchpotato.data.api.tmdb.model.MovieVideosResponse;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;
import rx.Observable;

public interface TMDbService {
  @GET("/movie/popular") Observable<MovieCollectionResponse> popular(@Query("page") int page);

  @GET("/movie/{id}") Observable<TMDbMovie> movie(@Path("id") long id);

  @GET("/movie/{id}/images") Observable<Images> movieImages(@Path("id") long id);

  @GET("/movie/{id}/credits") Observable<MovieCreditsResponse> movieCredits(@Path("id") long id);

  @GET("/movie/{id}/videos") Observable<MovieVideosResponse> movieVideos(@Path("id") long id);

  @GET("/movie/{id}/similar_movies") Observable<MovieCollectionResponse> movieSimilar(
      @Path("id") long id);

  @GET("/configuration") Observable<Configuration> configuration();
}