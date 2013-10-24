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

package com.f2prateek.couchpotato.ui.activities;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import com.f2prateek.couchpotato.R;
import com.f2prateek.couchpotato.model.movie.Movie;
import com.f2prateek.couchpotato.model.movie.MovieGetResponse;
import com.f2prateek.couchpotato.model.movie.MovieRefreshResponse;
import com.f2prateek.couchpotato.ui.CropPosterTransformation;
import com.f2prateek.couchpotato.util.Ln;
import com.f2prateek.couchpotato.util.RetrofitErrorHandler;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/** An activity to show a single {@link Movie}. This is a movie available in the user's library. */
public class ViewMovieActivity extends AbstractViewMovieActivity {

  private Movie movie;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    movie = new Gson().fromJson(getIntent().getExtras().getString(MOVIE_KEY), Movie.class);
    bindView(movie);
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    super.onCreateOptionsMenu(menu);
    getMenuInflater().inflate(R.menu.movie_activity, menu);
    return true;
  }

  @Override public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case R.id.action_refresh:
        refresh();
        return true;
    }
    return super.onOptionsItemSelected(item);
  }

  @Override public void openImdbPage() {
    openUrl(movie.getImdbPage());
  }

  @Override public void playTrailer() {
    //TODO:stub
  }

  /**
   * Refresh the movie from the server. We still have to fetch it again from the server.
   */
  private void refresh() {
    couchPotatoApi.movie_refresh(movie.id, new Callback<MovieRefreshResponse>() {
      @Override public void success(MovieRefreshResponse movieRefreshResponse, Response response) {
        Ln.d("Refreshed movie: " + String.valueOf(movieRefreshResponse.success));
        if (movieRefreshResponse.success) update();
      }

      @Override public void failure(RetrofitError retrofitError) {
        Ln.e("Could not get refresh with id %d" + movie.id);
        RetrofitErrorHandler.showError(ViewMovieActivity.this, retrofitError);
      }
    });
  }

  /**
   * Update this movie from the server.
   */
  private void update() {
    couchPotatoApi.movie_get(movie.id, new Callback<MovieGetResponse>() {
      @Override public void success(MovieGetResponse movieGetResponse, Response response) {
        Ln.d("Got movie: " + String.valueOf(movieGetResponse.success));
        if (movieGetResponse.success) bindView(movieGetResponse.movie);
      }

      @Override public void failure(RetrofitError retrofitError) {
        Ln.e("Could not get movie with id %d" + movie.id);
        RetrofitErrorHandler.showError(ViewMovieActivity.this, retrofitError);
      }
    });
  }

  public void bindView(Movie movie) {
    getActionBar().setTitle(movie.library.titles.get(0).title);
    Picasso.with(this)
        .load(movie.getBackdropUrl())
        .transform(new CropPosterTransformation(movie_backdrop,
            getResources().getConfiguration().orientation))
        .into(movie_backdrop);
    movie_plot.setText(movie.library.info.plot);
    if (TextUtils.isEmpty(movie.library.info.tagline)) {
      movie_tagline.setVisibility(View.GONE);
    } else {
      movie_tagline.setText(movie.library.info.tagline);
    }
    writeToTextView(movie_genres, movie.library.info.genres);
    writeToTextView(movie_cast, movie.library.info.actors);
    writeToTextView(movie_directors, movie.library.info.directors);
    writeToTextView(movie_writers, movie.library.info.writers);
  }
}
