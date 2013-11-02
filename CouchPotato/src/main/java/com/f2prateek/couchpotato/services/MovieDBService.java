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
import com.f2prateek.couchpotato.MovieDBApi;
import com.f2prateek.couchpotato.bus.DataEvent;
import com.f2prateek.couchpotato.model.moviedb.MovieDBMovie;
import com.f2prateek.couchpotato.util.Ln;
import com.f2prateek.couchpotato.util.SafeAsyncTask;
import java.util.HashMap;
import javax.inject.Inject;

// A service that communicates with MovieDB
public class MovieDBService extends BaseApiService {
  public static final String EXTRA_MOVIE_ID = "MovieDBService.EXTRA_MOVIE_ID";

  @Inject MovieDBApi movieDBApi;
  HashMap<Long, MovieDBMovie> movieCache = new HashMap<Long, MovieDBMovie>();

  @Override public int onStartCommand(Intent intent, int flags, int startId) {
    long id = intent.getLongExtra(EXTRA_MOVIE_ID, 0);
    new GetMovieTask(id).execute();
    return START_STICKY;
  }

  private class GetMovieTask extends SafeAsyncTask<MovieDBMovie> {
    private final long id;

    private GetMovieTask(long id) {
      this.id = id;
    }

    @Override public MovieDBMovie call() throws Exception {
      MovieDBMovie movie = movieCache.get(id);
      if (movieCache.get(id) == null) {
        movie = movieDBApi.get_movie(id);
        movie.casts = movieDBApi.get_movie_cast(id);
        movie.trailers = movieDBApi.get_movie_trailers(id);
        movie.images = movieDBApi.get_movie_images(id);
        movie.similarMovies = movieDBApi.get_movie_similar(id);
      }
      return movie;
    }

    @Override protected void onSuccess(MovieDBMovie movieDBMovie) throws Exception {
      super.onSuccess(movieDBMovie);
      bus.post(new DataEvent<MovieDBMovie>(movieDBMovie));
    }

    @Override protected void onException(Exception e) throws RuntimeException {
      super.onException(e);
      Ln.e(e);
    }
  }
}