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

import com.f2prateek.couchpotato.model.couchpotato.DirectoryListResponse;
import com.f2prateek.couchpotato.model.couchpotato.FileTypesResponse;
import com.f2prateek.couchpotato.model.couchpotato.ProfileListResponse;
import com.f2prateek.couchpotato.model.couchpotato.app.AppAvailableResponse;
import com.f2prateek.couchpotato.model.couchpotato.app.AppVersionResponse;
import com.f2prateek.couchpotato.model.couchpotato.logging.LoggingClearResponse;
import com.f2prateek.couchpotato.model.couchpotato.logging.LoggingGetResponse;
import com.f2prateek.couchpotato.model.couchpotato.logging.LoggingLogResponse;
import com.f2prateek.couchpotato.model.couchpotato.logging.LoggingPartialResponse;
import com.f2prateek.couchpotato.model.couchpotato.manage.ManageProgressResponse;
import com.f2prateek.couchpotato.model.couchpotato.manage.ManageUpdateResponse;
import com.f2prateek.couchpotato.model.couchpotato.movie.MovieAddResponse;
import com.f2prateek.couchpotato.model.couchpotato.movie.MovieGetResponse;
import com.f2prateek.couchpotato.model.couchpotato.movie.MovieListResponse;
import com.f2prateek.couchpotato.model.couchpotato.movie.MovieRefreshResponse;
import com.f2prateek.couchpotato.model.couchpotato.movie.search.MovieSearchResponse;
import retrofit.Callback;
import retrofit.client.Response;
import retrofit.http.GET;
import retrofit.http.Query;

public interface CouchPotatoApi {

  // app
  @GET("/app.available") AppAvailableResponse app_available();

  @GET("/app.restart") Response app_restart();

  @GET("/app.shutdown") Response app_shutdown();

  @GET("/app.version") AppVersionResponse app_version();

  // directory
  @GET("/directory.list") DirectoryListResponse directory_list();

  // file
  @GET("/file.types") FileTypesResponse file_types();

  // logging
  @GET("/logging.clear") LoggingClearResponse logging_clear();

  @GET("/logging.get") LoggingGetResponse logging_get(@Query("id") long id);

  @GET("/logging.log") LoggingLogResponse logging_log();

  @GET("/logging.partial") LoggingPartialResponse logging_partial();

  // movie
  @GET("/movie.add") MovieAddResponse movie_add(@Query("profile_id") String profile_id,
      @Query("identifier") String identifier, @Query("title") String title);

  @GET("/movie.list") void movie_list(Callback<MovieListResponse> cb);

  @GET("/movie.get") MovieGetResponse movie_get(@Query("id") long id);

  @GET("/movie.refresh") MovieRefreshResponse movie_refresh(@Query("id") long id);

  @GET("/movie.search") MovieSearchResponse movie_search(@Query("q") String query);

  // manage
  @GET("/manage.progress") ManageProgressResponse manage_progress();

  @GET("/manage.update") ManageUpdateResponse manage_update();

  // profile
  @GET("/profile.list") ProfileListResponse profile_list();
}