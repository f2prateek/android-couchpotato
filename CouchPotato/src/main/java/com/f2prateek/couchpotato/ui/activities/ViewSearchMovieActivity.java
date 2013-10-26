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
import android.view.SubMenu;
import android.view.View;
import android.widget.Toast;
import com.f2prateek.couchpotato.R;
import com.f2prateek.couchpotato.model.couchpotato.ProfileListResponse;
import com.f2prateek.couchpotato.model.couchpotato.movie.MovieAddResponse;
import com.f2prateek.couchpotato.model.couchpotato.movie.search.SearchMovie;
import com.f2prateek.couchpotato.ui.CropPosterTransformation;
import com.f2prateek.couchpotato.util.Ln;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import java.util.List;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/** An activity to show a single {@link com.f2prateek.couchpotato.model.couchpotato.movie.search.SearchMovie}. */
public class ViewSearchMovieActivity extends AbstractViewMovieActivity {
  private SearchMovie movie;
  private List<ProfileListResponse.Profile> profiles;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    movie = new Gson().fromJson(getIntent().getExtras().getString(MOVIE_KEY), SearchMovie.class);
    bindView(movie);

    couchPotatoApi.profile_list(new Callback<ProfileListResponse>() {
      @Override public void success(ProfileListResponse profileListResponse, Response response) {
        if (profileListResponse.success) {
          profiles = profileListResponse.profiles;
          invalidateOptionsMenu();
        } else {
          Ln.e("Unable to query profiles. Body = " + response.getBody());
        }
      }

      @Override public void failure(RetrofitError retrofitError) {
        Ln.e(retrofitError.getCause(), "Unable to query profiles.");
      }
    });
  }

  @Override protected void setupPages() {

  }

  public void bindView(SearchMovie movie) {
    getActionBar().setTitle(movie.original_title);
    Picasso.with(this)
        .load(movie.getBackdropUrl())
        .transform(new CropPosterTransformation(movie_backdrop,
            getResources().getConfiguration().orientation))
        .into(movie_backdrop);
    //movie_plot.setText(movie.plot);
    if (TextUtils.isEmpty(movie.tagline)) {
      movie_tagline.setVisibility(View.GONE);
    } else {
      movie_tagline.setText(movie.tagline);
    }
    //writeToTextView(movie_genres, movie.genres);
    //writeToTextView(movie_cast, movie.actors);
    //writeToTextView(movie_directors, movie.directors);
    //writeToTextView(movie_writers, movie.writers);
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    if (profiles != null) {
      Ln.d("Profiles = " + profiles);
      SubMenu subMenu = menu.addSubMenu(R.string.action_add);
      // TODO : add an icon
      for (ProfileListResponse.Profile profile : profiles) {
        subMenu.add(profile.label);
      }
    }
    super.onCreateOptionsMenu(menu);
    return true;
  }

  @Override public boolean onOptionsItemSelected(MenuItem item) {
    if (profiles != null) {
      for (ProfileListResponse.Profile profile : profiles) {
        if (item.getTitle().toString().compareToIgnoreCase(profile.label) == 0) {
          addMovie(profile);
        }
      }
    }
    return super.onOptionsItemSelected(item);
  }

  @Override public void openImdbPage() {
    openUrl(movie.getImdbPage());
  }

  @Override public void playTrailer() {
    //TODO:stub
  }

  public void addMovie(ProfileListResponse.Profile profile) {
    Ln.d("Adding movie with Profile = " + profile);
    couchPotatoApi.movie_add(String.valueOf(profile.id), movie.imdb, null,
        new Callback<MovieAddResponse>() {
          @Override public void success(MovieAddResponse movieAddResponse, Response response) {
            Toast.makeText(ViewSearchMovieActivity.this, R.string.movie_added, Toast.LENGTH_SHORT)
                .show();
          }

          @Override public void failure(RetrofitError retrofitError) {
            Ln.e(retrofitError.getCause(), "Unable to add movie");
          }
        });
    // TODO : stub
  }
}