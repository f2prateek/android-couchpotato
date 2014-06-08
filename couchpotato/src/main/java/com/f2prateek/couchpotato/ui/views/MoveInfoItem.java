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
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import com.f2prateek.couchpotato.R;
import com.f2prateek.couchpotato.data.api.tmdb.model.TMDbMovie;
import com.f2prateek.couchpotato.data.rx.EndlessObserver;
import com.f2prateek.couchpotato.ui.misc.colorizer.ColorScheme;
import com.f2prateek.couchpotato.ui.widget.RatingView;
import com.f2prateek.couchpotato.util.CollectionUtils;
import com.f2prateek.couchpotato.util.Strings;
import com.squareup.phrase.Phrase;
import com.squareup.picasso.Picasso;
import java.io.IOException;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class MoveInfoItem extends LinearLayout {
  @InjectView(R.id.movie_title) TextView title;
  @InjectView(R.id.movie_tagline) TextView tagline;
  @InjectView(R.id.movie_plot) TextView plot;
  @InjectView(R.id.movie_runtime) TextView runtime;
  @InjectView(R.id.movie_rating_container) LinearLayout ratingContainer;
  @InjectView(R.id.movie_vote_count) TextView voteCount;
  @InjectView(R.id.movie_rating) RatingView rating;
  @InjectView(R.id.movie_status) TextView status;
  @InjectView(R.id.movie_genres) TextView genres;
  @InjectView(R.id.movie_collection) TextView collection;
  @InjectView(R.id.movie_languages_label) TextView languagesLabel;
  @InjectView(R.id.movie_languages) TextView languages;
  @InjectView(R.id.movie_countries) TextView countries;
  @InjectView(R.id.movie_countries_label) TextView countriesLabel;
  @InjectView(R.id.movie_companies) TextView companies;
  @InjectView(R.id.movie_companies_label) TextView companiesLabel;

  private Subscription subscription;

  public MoveInfoItem(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  @Override protected void onFinishInflate() {
    super.onFinishInflate();
    ButterKnife.inject(this);
  }

  public void bindTo(TMDbMovie movie, Picasso picasso) {
    title.setText(movie.getTitle());

    plot.setText(movie.getOverview());

    if (Strings.isBlank(movie.getTagline())) {
      tagline.setVisibility(View.GONE);
    } else {
      tagline.setText(movie.getTagline());
    }

    runtime.setText(
        Phrase.from(this, R.string.movie_runtime).put("minutes", movie.getRuntime()).format());

    voteCount.setText(Phrase.from(this, R.string.movie_vote_count)
        .put("vote_count", movie.getVoteCount())
        .format());

    rating.setRating((int) movie.getVoteAverage());

    // expect it to not be empty
    genres.setText(Strings.join(", ", movie.getGenres()));

    if (!Strings.isBlank(movie.getStatus())) {
      status.setText(movie.getStatus());
    }

    if (movie.getBelongsToCollection() != null && !Strings.isBlank(
        movie.getBelongsToCollection().displayText())) {
      collection.setText(movie.getBelongsToCollection().displayText());
    } else {
      collection.setVisibility(View.GONE);
    }

    if (CollectionUtils.isNullOrEmpty(movie.getSpokenLanguages())) {
      languages.setVisibility(View.GONE);
      languagesLabel.setVisibility(View.GONE);
    } else {
      languages.setText(Strings.join(", ", movie.getSpokenLanguages()));
    }

    if (CollectionUtils.isNullOrEmpty(movie.getProductionCountries())) {
      countries.setVisibility(View.GONE);
      countriesLabel.setVisibility(View.GONE);
    } else {
      countries.setText(Strings.join(", ", movie.getProductionCountries()));
    }

    if (CollectionUtils.isNullOrEmpty(movie.getProductionCompanies())) {
      companies.setVisibility(View.GONE);
      companiesLabel.setVisibility(View.GONE);
    } else {
      companies.setText(Strings.join(", ", movie.getProductionCompanies()));
    }

    updateColorScheme(movie, picasso);
  }

  /**
   * Use the minifiedMovie's poster to find a color scheme and update our views accordingly.
   */
  private void updateColorScheme(TMDbMovie movie, final Picasso picasso) {
    subscription = Observable.from(movie.getPosterPath())
        .map(new Func1<String, Bitmap>() {
          @Override public Bitmap call(String url) {
            try {
              return picasso.load(url).get();
            } catch (IOException e) {
              throw new RuntimeException(e);
            }
          }
        })
        .map(new Func1<Bitmap, ColorScheme>() {
          @Override public ColorScheme call(Bitmap bitmap) {
            return ColorScheme.fromBitmap(bitmap);
          }
        })
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new EndlessObserver<ColorScheme>() {
                     @Override public void onNext(final ColorScheme colorScheme) {
                       rating.setColorScheme(colorScheme);
                     }
                   }
        );
  }

  @Override protected void onDetachedFromWindow() {
    super.onDetachedFromWindow();
    subscription.unsubscribe();
  }
}
