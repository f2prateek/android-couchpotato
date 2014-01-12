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
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import com.f2prateek.couchpotato.R;
import com.f2prateek.couchpotato.model.couchpotato.movie.CouchPotatoMovie;
import com.f2prateek.couchpotato.services.CouchPotatoService;
import com.f2prateek.couchpotato.ui.activities.ViewMovieActivity;
import com.f2prateek.couchpotato.ui.util.BindingListAdapter;
import com.google.common.base.Joiner;
import com.squareup.otto.Subscribe;
import com.squareup.picasso.Picasso;
import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;
import java.util.ArrayList;
import java.util.List;

/**
 * A {@link BaseProgressGridFragment} that displays a detailed view of movies in the user's
 * library.
 */
public class DetailedMovieGridFragment extends BaseProgressGridFragment
    implements AbsListView.MultiChoiceModeListener {

  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setHasOptionsMenu(true);
    refreshMovies(false);
    setRetainInstance(true);
  }

  @Override public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
    super.onCreateOptionsMenu(menu, inflater);
    inflater.inflate(R.menu.movie_grid_fragment, menu);
  }

  @Override public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case R.id.action_refresh_movies:
        refreshMovies(true);
        break;
    }
    return super.onOptionsItemSelected(item);
  }

  /**
   * Refresh the list of movies.
   *
   * @param triggeredByUser true if user has initiated the refresh.
   */
  private void refreshMovies(boolean triggeredByUser) {
    if (triggeredByUser) {
      showIndeterminateBar(true);
      Crouton.makeText(getActivity(), R.string.refreshing, Style.INFO).show();
    }
    Intent intent = new Intent(activityContext, CouchPotatoService.class);
    intent.setAction(CouchPotatoService.ACTION_GET_MOVIES);
    activityContext.startService(intent);
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
    gridView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
    gridView.setMultiChoiceModeListener(this);
  }

  @Override public void onGridItemClick(GridView gridView, View v, int position, long id) {
    CouchPotatoMovie CouchPotatoMovie = (CouchPotatoMovie) getGridAdapter().getItem(position);
    Intent movieIntent = new Intent(getActivity(), ViewMovieActivity.class);
    movieIntent.putExtra(ViewMovieActivity.MOVIE_KEY, gson.toJson(CouchPotatoMovie));
    startActivity(movieIntent);
  }

  @Subscribe
  public void onMoviesReceived(ArrayList<CouchPotatoMovie> movies) {
    ListAdapter adapter = new DetailedMovieAdapter(activityContext, movies);
    setGridAdapter(adapter);
  }

  @Override public boolean onCreateActionMode(ActionMode mode, Menu menu) {
    mode.setTitle(R.string.select_items_action);
    mode.setSubtitle(getResources().getQuantityString(R.plurals.selected_items_count, 1));
    return true;
  }

  @Override public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
    // TODO : add actions
    return true;
  }

  @Override
  public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
    int selectCount = getGridView().getCheckedItemCount();
    mode.setSubtitle(
        getResources().getQuantityString(R.plurals.selected_items_count, selectCount, selectCount));
  }

  @Override public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
    return true;
  }

  @Override public void onDestroyActionMode(ActionMode mode) {

  }

  class DetailedMovieAdapter extends BindingListAdapter<CouchPotatoMovie> {
    private final Joiner commaJoiner = Joiner.on(", ");

    // Cached resources
    private final String titleTextFormat;
    private final String runtimeTextFormat;
    private final String ratingTextFormat;
    private final StyleSpan boldSpan;
    private final TextAppearanceSpan mediumSizeSpan;
    private final ForegroundColorSpan greenColorSpan;
    private final ForegroundColorSpan redColorSpan;
    private final ImageSpan starSpan;
    private final ImageSpan labelSpan;
    private final int itemPadding;

    DetailedMovieAdapter(Context context, List<CouchPotatoMovie> list) {
      super(context, list);
      Resources resources = context.getResources();
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
      itemPadding = resources.getDimensionPixelOffset(R.dimen.movie_grid_item_padding);
    }

    @Override public View newView(LayoutInflater inflater, int type, ViewGroup parent) {
      View view = inflater.inflate(R.layout.detailed_movie_info, parent, false);
      view.setPadding(itemPadding, itemPadding, itemPadding, itemPadding);
      ViewHolder holder = new ViewHolder(view);
      view.setTag(holder);
      return view;
    }

    @Override public void bindView(CouchPotatoMovie movie, View view) {
      ViewHolder holder = (ViewHolder) view.getTag();
      Picasso.with(activityContext).load(movie.getPosterUrl()).into(holder.poster);
      holder.title
          .setText(getTitleText(movie.library.titles.get(0).title, movie.library.info.year));
      holder.runtime.setText(getRuntimeText(movie.library.info.runtime));
      holder.rating.setText(getRatingText(movie.library.info.rating.imdb.get(0)));
      holder.genres.setText(getGenreText(movie.library.info.genres));
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

    private Spanned getGenreText(List<String> genres) {
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
        ButterKnife.inject(this, view);
      }
    }
  }
}
