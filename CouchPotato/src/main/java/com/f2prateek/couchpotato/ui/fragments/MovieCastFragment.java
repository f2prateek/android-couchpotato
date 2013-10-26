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

package com.f2prateek.couchpotato.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.InjectView;
import com.f2prateek.couchpotato.R;
import com.f2prateek.couchpotato.model.couchpotato.movie.Movie;
import com.f2prateek.couchpotato.ui.base.BaseFragment;
import com.google.gson.Gson;
import java.util.ArrayList;

/**
 * Fragment to display the movie's cast, writers, directors, etc.
 * Rather than use a specific MovieBean, supply the info directly.
 */
public class MovieCastFragment extends BaseFragment {

  // TODO : actor images, etc.

  @InjectView(R.id.movie_cast) LinearLayout movie_cast;
  private Movie movie;

  /** Create a new instance of MovieCastFragment */
  public static MovieCastFragment newInstance(Movie movie) {
    MovieCastFragment f = new MovieCastFragment();
    Bundle args = new Bundle();
    args.putString("movie", new Gson().toJson(movie));
    f.setArguments(args);
    return f;
  }

  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    movie = new Gson().fromJson(getArguments().getString("movie"), Movie.class);
  }

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    return inflater.inflate(R.layout.fragment_movie_cast, container, false);
  }

  @Override public void onViewCreated(View view, Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    Context context = getActivity();
    addViewForList(movie.library.info.actors, context);
    addViewForList(movie.library.info.writers, context);
    addViewForList(movie.library.info.directors, context);
  }

  private void addViewForList(ArrayList<String> people, Context context) {
    for (String person : people) {
      movie_cast.addView(getViewForPerson(person, context));
    }
  }

  private View getViewForPerson(String person, Context context) {
    TextView textView = new TextView(context);
    textView.setText(person);
    return textView;
  }
}