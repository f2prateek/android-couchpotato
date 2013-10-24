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
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.text.style.StyleSpan;
import android.text.style.TextAppearanceSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.InjectView;
import butterknife.Views;
import com.f2prateek.couchpotato.CouchPotatoApi;
import com.f2prateek.couchpotato.R;
import com.f2prateek.couchpotato.model.movie.search.MovieSearchResponse;
import com.f2prateek.couchpotato.model.movie.search.SearchMovie;
import com.f2prateek.couchpotato.ui.activities.ViewSearchMovieActivity;
import com.f2prateek.couchpotato.ui.base.BaseProgressGridFragment;
import com.f2prateek.couchpotato.ui.util.BindingListAdapter;
import com.f2prateek.couchpotato.util.Ln;
import com.google.common.base.Joiner;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/** A gridFragment to search for movies. */
public class MovieSearchGridFragment extends BaseProgressGridFragment
    implements Callback<MovieSearchResponse> {

  @Inject CouchPotatoApi couchPotatoApi;

  /** Create a new instance of MovieSearchGridFragment */
  public static MovieSearchGridFragment newInstance(String query) {
    MovieSearchGridFragment f = new MovieSearchGridFragment();

    Bundle args = new Bundle();
    args.putString("query", query);
    f.setArguments(args);

    return f;
  }

  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    String query = getArguments().getString("query");
    couchPotatoApi.movie_search(query, this);
    setRetainInstance(true);
  }

  @Override public void onViewCreated(View view, Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    GridView gridView = getGridView();
    gridView.setStretchMode(GridView.STRETCH_COLUMN_WIDTH);
    gridView.setNumColumns(GridView.AUTO_FIT);
    gridView.setColumnWidth(
        getResources().getDimensionPixelOffset(R.dimen.detailed_grid_column_width));
    gridView.setFastScrollEnabled(true);
    gridView.setDrawSelectorOnTop(true);
    gridView.setHorizontalSpacing(
        getResources().getDimensionPixelOffset(R.dimen.grid_item_spacing));
    gridView.setVerticalSpacing(getResources().getDimensionPixelOffset(R.dimen.grid_item_spacing));
  }

  @Override public void onGridItemClick(GridView gridView, View v, int position, long id) {
    SearchMovie movie = (SearchMovie) getGridAdapter().getItem(position);
    Intent movieIntent = new Intent(getActivity(), ViewSearchMovieActivity.class);
    movieIntent.putExtra(ViewSearchMovieActivity.MOVIE_KEY, new Gson().toJson(movie));
    startActivity(movieIntent);
  }

  @Override public void success(MovieSearchResponse movieSearchResponse, Response response) {
    Ln.d("Success %d", movieSearchResponse.movies.size());
    BindingListAdapter adapter =
        new DetailedMovieAdapter(getActivity(), movieSearchResponse.movies);
    setGridAdapter(adapter);
  }

  @Override public void failure(RetrofitError retrofitError) {
    Ln.e(retrofitError.getCause());
    setEmptyText(getResources().getString(R.string.failed));
    BindingListAdapter adapter =
        new DetailedMovieAdapter(getActivity(), new ArrayList<SearchMovie>());
    setGridAdapter(adapter);
  }

  public class DetailedMovieAdapter extends BindingListAdapter<SearchMovie> {
    private final Joiner commaJoiner = Joiner.on(", ");

    // Cached resources
    private final String runtimeTextFormat;
    private final String ratingTextFormat;
    private final StyleSpan boldSpan;
    private final TextAppearanceSpan mediumSizeSpan;
    private final ForegroundColorSpan greenColorSpan;
    private final ForegroundColorSpan redColorSpan;
    private final ImageSpan starSpan;
    private final ImageSpan labelSpan;

    public DetailedMovieAdapter(Context context, List<SearchMovie> list) {
      super(context, list);
      Resources resources = context.getResources();
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

    @Override public View newView(LayoutInflater inflater, int type, ViewGroup parent) {
      View view = inflater.inflate(R.layout.detailed_movie_grid_item, parent, false);
      ViewHolder holder = new ViewHolder(view);
      view.setTag(holder);
      return view;
    }

    @Override public void bindView(int position, int type, View view) {
      super.bindView(position, type, view);
    }

    @Override public void bindView(SearchMovie movie, View view) {
      ViewHolder holder = (ViewHolder) view.getTag();
      Picasso.with(getContext()).load(movie.getPosterUrl()).into(holder.poster);
      holder.title.setText(getTitleText(movie.titles.get(0)));
      // Not enough information, make sure title is displayed fully in it's place
      holder.title.setMaxLines(3);
      holder.runtime.setText(getRuntimeText(movie.runtime));
      holder.genres.setText(getGenreText(movie.genres));
    }

    private Spannable getTitleText(String title) {
      SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(title);
      spannableStringBuilder.setSpan(mediumSizeSpan, 0, title.length(),
          Spannable.SPAN_INCLUSIVE_INCLUSIVE);
      spannableStringBuilder.setSpan(boldSpan, 0, title.length(),
          Spannable.SPAN_INCLUSIVE_INCLUSIVE);
      return spannableStringBuilder;
    }

    private Spanned getRuntimeText(int time) {
      int hours = time / 60;
      int minutes = time % 60;
      return Html.fromHtml(String.format(runtimeTextFormat, hours, minutes));
    }

    private Spanned getGenreText(List<String> genres) {
      if (genres == null || genres.size() == 0) {
        return new SpannableStringBuilder("  ");
      }
      SpannableStringBuilder spannableStringBuilder =
          new SpannableStringBuilder("  " + commaJoiner.join(genres));
      spannableStringBuilder.setSpan(labelSpan, 0, 1, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
      return spannableStringBuilder;
    }

    class ViewHolder {
      @InjectView(R.id.movie_poster) ImageView poster;
      @InjectView(R.id.movie_title) TextView title;
      @InjectView(R.id.movie_runtime) TextView runtime;
      @InjectView(R.id.movie_rating) TextView rating;
      @InjectView(R.id.movie_genres) TextView genres;

      ViewHolder(View view) {
        Views.inject(this, view);
      }
    }
  }
}
