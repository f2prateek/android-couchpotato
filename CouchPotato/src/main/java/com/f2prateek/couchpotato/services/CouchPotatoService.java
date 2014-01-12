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

import android.content.Intent;
import com.f2prateek.couchpotato.model.couchpotato.DirectoryListResponse;
import com.f2prateek.couchpotato.model.couchpotato.app.AppAvailableResponse;
import com.f2prateek.couchpotato.model.couchpotato.movie.CouchPotatoMovie;
import com.f2prateek.couchpotato.model.couchpotato.movie.MovieGetResponse;
import com.f2prateek.couchpotato.model.couchpotato.movie.MovieListResponse;
import com.f2prateek.couchpotato.services.couchpotato.CouchPotatoAppApi;
import com.f2prateek.couchpotato.services.couchpotato.CouchPotatoLoggingApi;
import com.f2prateek.couchpotato.services.couchpotato.CouchPotatoManageApi;
import com.f2prateek.couchpotato.services.couchpotato.CouchPotatoMiscApi;
import com.f2prateek.couchpotato.services.couchpotato.CouchPotatoMovieApi;
import java.util.List;
import javax.inject.Inject;
import retrofit.client.Response;

public class CouchPotatoService extends BaseApiService {

  public static final String ACTION_GET_MOVIE = "CouchPotatoService.ACTION_GET_MOVIE";
  public static final String ACTION_GET_MOVIES = "CouchPotatoService.ACTION_GET_MOVIES";
  public static final String ACTION_IS_APP_AVAILABLE = "CouchPotatoService.ACTION_IS_APP_AVAILABLE";
  public static final String ACTION_RESTART_APP = "CouchPotatoService.ACTION_RESTART_APP";
  public static final String ACTION_SHUTDOWN_APP = "CouchPotatoService.ACTION_SHUTDOWN_APP";
  public static final String ACTION_GET_DIRECTORIES = "CouchPotatoService.ACTION_GET_DIRECTORIES";

  public static final String EXTRA_MOVIE_ID = "CouchPotatoService.EXTRA_MOVIE_ID";

  @Inject CouchPotatoAppApi couchPotatoAppApi;
  @Inject CouchPotatoLoggingApi couchPotatoLoggingApi;
  @Inject CouchPotatoManageApi couchPotatoManageApi;
  @Inject CouchPotatoMiscApi couchPotatoMiscApi;
  @Inject CouchPotatoMovieApi couchPotatoMovieApi;

  @Override public int onStartCommand(Intent intent, int flags, int startId) {
    if (ACTION_GET_MOVIES.equals(intent.getAction())) {
      new GetMoviesTask().execute();
    } else if (ACTION_GET_MOVIE.equals(intent.getAction())) {
      long id = intent.getLongExtra(EXTRA_MOVIE_ID, 0);
      new GetMovieTask(id).execute();
    } else if (ACTION_IS_APP_AVAILABLE.equals(intent.getAction())) {
      new GetAppAvailableTask().execute();
    } else if (ACTION_RESTART_APP.equals(intent.getAction())) {
      new AppRestartTask().execute();
    } else if (ACTION_SHUTDOWN_APP.equals(intent.getAction())) {
      new AppShutdownTask().execute();
    } else if (ACTION_GET_DIRECTORIES.equals(intent.getAction())) {
      new GetDirectoriesTask().execute();
    }
    return START_REDELIVER_INTENT;
  }

  private class GetMoviesTask extends DataEventTask<List<CouchPotatoMovie>> {

    @Override public List<CouchPotatoMovie> call() throws Exception {
      MovieListResponse response = couchPotatoMovieApi.list();
      if (response.success) {
        return response.movies;
      } else {
        return null;
      }
    }
  }

  private class GetMovieTask extends DataEventTask<CouchPotatoMovie> {
    private final long id;

    private GetMovieTask(long id) {
      this.id = id;
    }

    @Override public CouchPotatoMovie call() throws Exception {
      MovieGetResponse response = couchPotatoMovieApi.get(id);
      if (response.success) {
        return response.movie;
      } else {
        return null;
      }
    }
  }

  private class GetAppAvailableTask extends DataEventTask<AppAvailableResponse> {
    @Override public AppAvailableResponse call() throws Exception {
      return couchPotatoAppApi.available();
    }
  }

  private class AppRestartTask extends DataEventTask<Response> {
    @Override public Response call() throws Exception {
      return couchPotatoAppApi.restart();
    }
  }

  private class AppShutdownTask extends DataEventTask<Response> {
    @Override public Response call() throws Exception {
      return couchPotatoAppApi.shutdown();
    }
  }

  private class GetDirectoriesTask extends SaveableDataEventTask<DirectoryListResponse> {
    public GetDirectoriesTask() {
      super(DirectoryListResponse.class);
    }

    @Override public DirectoryListResponse get() {
      return couchPotatoMiscApi.directory_list();
    }
  }
}