/*
 * Copyright 2014 Prateek Srivastava
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

package com.f2prateek.couchpotato.ui.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import com.f2prateek.couchpotato.R;
import com.f2prateek.couchpotato.data.api.tmdb.model.TMDbMovie;
import com.f2prateek.couchpotato.ui.widget.RatingView;
import com.f2prateek.couchpotato.util.Strings;
import com.squareup.phrase.Phrase;
import com.squareup.picasso.Picasso;

public class MoveInfoItem extends LinearLayout {
  @InjectView(R.id.movie_title) TextView title;
  @InjectView(R.id.movie_tagline) TextView tagline;
  @InjectView(R.id.movie_runtime) TextView runtime;
  @InjectView(R.id.movie_rating) RatingView rating;
  @InjectView(R.id.movie_genres) TextView genres;
  @InjectView(R.id.movie_collection) TextView collection;
  @InjectView(R.id.movie_status) TextView status;
  @InjectView(R.id.movie_languages) TextView languages;
  @InjectView(R.id.movie_countries) TextView countries;
  @InjectView(R.id.movie_companies) TextView companies;

  public MoveInfoItem(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  @Override protected void onFinishInflate() {
    super.onFinishInflate();
    ButterKnife.inject(this);
  }

  public void bindTo(TMDbMovie movie, Picasso picasso) {
    title.setText(movie.getTitle());
    if (!Strings.isBlank(movie.getTagline())) {
      tagline.setText(movie.getTagline());
    }
    runtime.setText(Phrase.from(getContext(), R.string.movie_runtime)
        .put("minutes", movie.getRuntime())
        .format());
    rating.setRating((int) movie.getVoteAverage());
    genres.setText(Strings.join(", ", movie.getGenres()));
    if (!Strings.isBlank(movie.getBelongsToCollection().displayText())) {
      collection.setText(movie.getBelongsToCollection().displayText());
    }
    if (!Strings.isBlank(movie.getStatus())) {
      status.setText(movie.getStatus());
    }
    languages.setText(Strings.join(", ", movie.getSpokenLanguages()));
    countries.setText(Strings.join(", ", movie.getProductionCountries()));
    companies.setText(Strings.join(", ", movie.getProductionCompanies()));
    // movie.getRevenue();
    // movie.getReleaseDate();
    movie.getPopularity();
  }
}
