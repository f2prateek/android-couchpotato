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

package com.f2prateek.couchpotato.ui.fragments.movie;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.ButterKnife;
import com.f2prateek.couchpotato.R;
import com.f2prateek.couchpotato.data.api.Movie;
import com.f2prateek.couchpotato.ui.fragments.BaseGridFragment;
import com.f2prateek.couchpotato.ui.widget.HeaderGridView;
import com.f2prateek.dart.InjectExtra;

/**
 * Common functionality for {@link com.f2prateek.couchpotato.ui.fragments.BaseGridFragment} that is
 * contained in a {@link com.f2prateek.couchpotato.ui.activities.MovieActivity}.
 */
public abstract class MovieInfoGridFragment extends BaseGridFragment {
  public static final int KEY_PAGE = R.id.key_page;
  private static final String ARGS_PAGE = "page";
  private static final String ARGS_MOVIE = "movie";

  @InjectExtra(ARGS_MOVIE) Movie minifiedMovie;
  @InjectExtra(ARGS_PAGE) int page;
  AbsListView.OnScrollListener scrollListener;

  public static Bundle newInstanceArgs(Movie movie, int page) {
    Bundle args = new Bundle();
    args.putParcelable(ARGS_MOVIE, movie);
    args.putInt(ARGS_PAGE, page);
    return args;
  }

  @Override public void onAttach(Activity activity) {
    super.onAttach(activity);
    scrollListener = (AbsListView.OnScrollListener) activity;
  }

  @Override public void onActivityCreated(Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
    getCollectionView().setOnScrollListener(scrollListener);

    fetch(minifiedMovie);
  }

  // Hook for fetching data for the given movie.
  protected abstract void fetch(Movie minifiedMovie);

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View root = inflater.inflate(R.layout.fragment_header_grid, container, false);
    HeaderGridView gridView = ButterKnife.findById(root, R.id.collection_view);
    View placeholder = inflater.inflate(R.layout.movie_header_placeholder, gridView, false);
    gridView.addHeaderView(placeholder);
    gridView.setTag(KEY_PAGE, page);
    return root;
  }

  public void setEmptyView(CharSequence emptyText) {
    LinearLayout container = new LinearLayout(activityContext);
    container.setOrientation(LinearLayout.VERTICAL);
    container.addView(inflate(R.layout.movie_header_placeholder));
    TextView emptyView = (TextView) inflate(R.layout.partial_extra_text);
    emptyView.setText(emptyText);
    container.addView(emptyView);

    setExtraView(container);
  }
}
