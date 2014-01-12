/*
 * Copyright 2013 Prateek Srivastava (@f2prateek)
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.f2prateek.couchpotato.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import com.f2prateek.couchpotato.R;
import com.f2prateek.couchpotato.model.moviedb.MovieDBMovie;
import com.f2prateek.couchpotato.model.moviedb.MovieDbConfiguration;
import com.f2prateek.couchpotato.ui.views.MovieInfoView;
import com.google.gson.Gson;
import javax.inject.Inject;

/**
 * Fragment to display the movie's cast, writers, directors, etc.
 */
public class MovieInfoFragment extends BaseFragment {

  @Inject MovieDbConfiguration movieDbConfiguration;

  @InjectView(R.id.movie_plot) TextView plot;
  @InjectView(R.id.movie_info_container) MovieInfoView movieInfoContainer;

  private MovieDBMovie movie;

  /** Create a new instance of MovieInfoFragment */
  public static MovieInfoFragment newInstance(Gson gson, MovieDBMovie movie) {
    MovieInfoFragment f = new MovieInfoFragment();
    Bundle args = new Bundle();
    args.putString("movie", gson.toJson(movie));
    f.setArguments(args);
    return f;
  }

  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    movie = gson.fromJson(getArguments().getString("movie"), MovieDBMovie.class);
  }

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    return inflater.inflate(R.layout.fragment_movie_info, container, false);
  }

  @Override public void onViewCreated(View view, Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    ButterKnife.inject(this, view);
    movieInfoContainer.setMovie(movie, movieDbConfiguration);
    plot.setText(movie.overview);
  }
}