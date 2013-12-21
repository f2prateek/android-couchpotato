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

package com.f2prateek.couchpotato.ui.widgets;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.text.style.StyleSpan;
import android.text.style.TextAppearanceSpan;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.InjectView;
import butterknife.Views;
import com.f2prateek.couchpotato.R;
import com.f2prateek.couchpotato.model.moviedb.Genre;
import com.f2prateek.couchpotato.model.moviedb.MovieDBMovie;
import com.f2prateek.couchpotato.model.moviedb.MovieDbConfiguration;
import com.google.common.base.Joiner;
import com.squareup.picasso.Picasso;
import java.util.Calendar;
import java.util.List;

/**
 * Compound view to display some movie information.
 */
public class MovieInfoView extends RelativeLayout {

  private static final Joiner commaJoiner = Joiner.on(", ");

  @InjectView(R.id.movie_poster) ImageView poster;
  @InjectView(R.id.movie_title) TextView title;
  @InjectView(R.id.movie_runtime) TextView runtime;
  @InjectView(R.id.movie_rating) TextView rating;
  @InjectView(R.id.movie_genres) TextView genres;

  private static String titleTextFormat;
  private static String runtimeTextFormat;
  private static String ratingTextFormat;
  private static StyleSpan boldSpan;
  private static TextAppearanceSpan mediumSizeSpan;
  private static ForegroundColorSpan greenColorSpan;
  private static ForegroundColorSpan redColorSpan;
  private static ImageSpan starSpan;
  private static ImageSpan labelSpan;
  private MovieDBMovie movie;

  public MovieInfoView(Context context, AttributeSet attributeSet) {
    super(context, attributeSet);
    LayoutInflater inflater =
        (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    inflater.inflate(R.layout.detailed_movie_info, this, true);
    Views.inject(this);

    if (titleTextFormat == null) {
      // These resources only need to be initialized once.
      Resources resources = getResources();
      titleTextFormat = resources.getString(R.string.title_text_format);
      runtimeTextFormat = resources.getString(R.string.runtime_text_format);
      ratingTextFormat = resources.getString(R.string.rating_text_format);
      boldSpan = new StyleSpan(android.graphics.Typeface.BOLD);
      mediumSizeSpan =
          new TextAppearanceSpan(context, android.R.style.TextAppearance_DeviceDefault_Medium);
      starSpan =
          new ImageSpan(BitmapFactory.decodeResource(resources, R.drawable.ic_action_star_0));
      labelSpan = new ImageSpan(BitmapFactory.decodeResource(resources, R.drawable.ic_action_tags));
      greenColorSpan = new ForegroundColorSpan(resources.getColor(android.R.color.holo_green_dark));
      redColorSpan = new ForegroundColorSpan(resources.getColor(android.R.color.holo_red_dark));
    }
  }

  public void setMovie(MovieDBMovie movie, MovieDbConfiguration movieDbConfiguration) {
    this.movie = movie;
    update(movieDbConfiguration);
  }

  public void update(MovieDbConfiguration movieDbConfiguration) {
    Calendar cal = Calendar.getInstance();
    cal.setTime(movie.release_date);
    Picasso.with(getContext()).load(movie.getSmallPosterUrl(movieDbConfiguration)).into(poster);
    // TODO, use movie.release_date
    title.setText(getTitleText(movie.title, cal.get(Calendar.YEAR)));
    runtime.setText(getRuntimeText(movie.runtime));
    rating.setText(getRatingText(movie.vote_average));
    genres.setText(getGenreText(movie.genres));
  }

  private Spannable getTitleText(String title, int year) {
    String titleText = String.format(titleTextFormat, title, year);
    SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(titleText);
    spannableStringBuilder.setSpan(mediumSizeSpan, titleText.indexOf(title), title.length(),
        Spannable.SPAN_INCLUSIVE_INCLUSIVE);
    spannableStringBuilder.setSpan(boldSpan, titleText.indexOf(title), title.length(),
        Spannable.SPAN_INCLUSIVE_INCLUSIVE);
    return spannableStringBuilder;
  }

  private Spanned getRuntimeText(int time) {
    int hours = time / 60;
    int minutes = time % 60;
    return Html.fromHtml(String.format(runtimeTextFormat, hours, minutes));
  }

  private Spanned getRatingText(Double rating) {
    // Padding for the star
    String ratingText = "  " + String.format(ratingTextFormat, rating.toString());
    SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(ratingText);
    spannableStringBuilder.setSpan(starSpan, 0, 1, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
    if (rating.compareTo(5.0d) < 0) {
      spannableStringBuilder.setSpan(redColorSpan, ratingText.indexOf(rating.toString()),
          ratingText.indexOf(rating.toString()) + rating.toString().length(),
          Spannable.SPAN_INCLUSIVE_INCLUSIVE);
    } else {
      spannableStringBuilder.setSpan(greenColorSpan, ratingText.indexOf(rating.toString()),
          ratingText.indexOf(rating.toString()) + rating.toString().length(),
          Spannable.SPAN_INCLUSIVE_INCLUSIVE);
    }
    spannableStringBuilder.setSpan(boldSpan, ratingText.indexOf(rating.toString()),
        ratingText.indexOf(rating.toString()) + rating.toString().length(),
        Spannable.SPAN_INCLUSIVE_INCLUSIVE);
    return spannableStringBuilder;
  }

  private Spanned getGenreText(List<Genre> genres) {
    SpannableStringBuilder spannableStringBuilder =
        new SpannableStringBuilder("  " + commaJoiner.join(genres));
    spannableStringBuilder.setSpan(labelSpan, 0, 1, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
    return spannableStringBuilder;
  }
}
