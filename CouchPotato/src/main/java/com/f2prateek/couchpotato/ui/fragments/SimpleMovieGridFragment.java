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
import android.os.Bundle;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.ListView;
import butterknife.InjectView;
import butterknife.Views;
import com.f2prateek.couchpotato.R;
import com.f2prateek.couchpotato.model.couchpotato.movie.CouchPotatoMovie;
import com.f2prateek.couchpotato.services.CouchPotatoApi;
import com.f2prateek.couchpotato.services.CouchPotatoService;
import com.f2prateek.couchpotato.ui.activities.ViewMovieActivity;
import com.f2prateek.couchpotato.ui.util.BindingListAdapter;
import com.f2prateek.couchpotato.ui.widgets.AspectRatioImageView;
import com.squareup.otto.Subscribe;
import com.squareup.picasso.Picasso;
import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;

/**
 * A {@link com.f2prateek.couchpotato.ui.fragments.BaseProgressGridFragment} that displays a
 * detailed view of movies in the user's
 * library.
 */
public class SimpleMovieGridFragment extends BaseProgressGridFragment
    implements AbsListView.MultiChoiceModeListener {

  @Inject CouchPotatoApi couchPotatoApi;

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
        getResources().getDimensionPixelOffset(R.dimen.simple_movie_grid_width));
    gridView.setFastScrollEnabled(true);
    gridView.setDrawSelectorOnTop(true);
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

    DetailedMovieAdapter(Context context, List<CouchPotatoMovie> list) {
      super(context, list);
    }

    @Override public View newView(LayoutInflater inflater, int type, ViewGroup parent) {
      View view = inflater.inflate(R.layout.simple_movie_grid_item, parent, false);
      ViewHolder holder = new ViewHolder(view);
      view.setTag(holder);
      return view;
    }

    @Override public void bindView(CouchPotatoMovie movie, View view) {
      ViewHolder holder = (ViewHolder) view.getTag();
      Picasso.with(getContext()).load(movie.getPosterUrl()).fit().centerCrop().into(holder.poster);
    }

    class ViewHolder {
      @InjectView(R.id.movie_poster) AspectRatioImageView poster;

      ViewHolder(View view) {
        Views.inject(this, view);
      }
    }
  }
}