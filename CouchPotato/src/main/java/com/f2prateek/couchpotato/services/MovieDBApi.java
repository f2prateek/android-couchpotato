/*
 * Copyright 2013 Prateek Srivastava (@f2prateek)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.f2prateek.couchpotato.services;

import com.f2prateek.couchpotato.model.moviedb.Casts;
import com.f2prateek.couchpotato.model.moviedb.Configuration;
import com.f2prateek.couchpotato.model.moviedb.Images;
import com.f2prateek.couchpotato.model.moviedb.MovieDBMovie;
import com.f2prateek.couchpotato.model.moviedb.SimilarMovies;
import com.f2prateek.couchpotato.model.moviedb.Trailers;
import retrofit.http.GET;
import retrofit.http.Path;

public interface MovieDBApi {
  @GET("/configuration") Configuration get_configuration();

  @GET("/movie/{id}") MovieDBMovie get_movie(@Path("id") long id);

  @GET("/movie/{id}/casts") Casts get_movie_cast(@Path("id") long id);

  @GET("/movie/{id}/images") Images get_movie_images(@Path("id") long id);

  @GET("/movie/{id}/trailers") Trailers get_movie_trailers(@Path("id") long id);

  @GET("/movie/{id}/similar_movies") SimilarMovies get_movie_similar(@Path("id") long id);
}