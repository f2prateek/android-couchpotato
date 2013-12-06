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
  @GET("/app.available") void app_available(Callback<AppAvailableResponse> cb);

  @GET("/app.restart") void app_restart(Callback<Response> cb);

  @GET("/app.shutdown") void app_shutdown(Callback<Response> cb);

  @GET("/app.version") void app_version(Callback<AppVersionResponse> cb);

  // directory
  @GET("/directory.list") void directory_list(Callback<DirectoryListResponse> cb);

  // file
  @GET("/file.types") void file_types(Callback<FileTypesResponse> cb);

  // logging
  @GET("/logging.clear") void logging_clear(Callback<LoggingClearResponse> cb);

  @GET("/logging.get") void logging_get(@Query("id") long id, Callback<LoggingGetResponse> cb);

  @GET("/logging.log") void logging_log(Callback<LoggingLogResponse> cb);

  @GET("/logging.partial") void logging_partial(Callback<LoggingPartialResponse> cb);

  // movie
  @GET("/movie.add")
  void movie_add(@Query("profile_id") String profile_id, @Query("identifier") String identifier,
      @Query("title") String title, Callback<MovieAddResponse> cb);

  @GET("/movie.list") void movie_list(Callback<MovieListResponse> cb);

  @GET("/movie.get") void movie_get(@Query("id") long id, Callback<MovieGetResponse> cb);

  @GET("/movie.refresh")
  void movie_refresh(@Query("id") long id, Callback<MovieRefreshResponse> cb);

  @GET("/movie.search")
  void movie_search(@Query("q") String query, Callback<MovieSearchResponse> cb);

  // manage
  @GET("/manage.progress") void manage_progress(Callback<ManageProgressResponse> cb);

  @GET("/manage.update") void manage_update(Callback<ManageUpdateResponse> cb);

  // profile
  @GET("/profile.list") void profile_list(Callback<ProfileListResponse> cb);
}