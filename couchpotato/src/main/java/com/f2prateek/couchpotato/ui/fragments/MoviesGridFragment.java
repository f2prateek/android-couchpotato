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

package com.f2prateek.couchpotato.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import butterknife.InjectView;
import com.f2prateek.couchpotato.ForActivity;
import com.f2prateek.couchpotato.R;
import com.f2prateek.couchpotato.ui.views.MovieGridAdapter;
import com.f2prateek.couchpotato.ui.widget.BetterViewAnimator;
import com.squareup.picasso.Picasso;
import javax.inject.Inject;

/**
 * A base fragment for displaying a grid of movies.
 */
public abstract class MoviesGridFragment extends BaseFragment {
  @InjectView(R.id.root) protected BetterViewAnimator root;
  @InjectView(R.id.extra) FrameLayout extra;
  @InjectView(R.id.grid) protected AbsListView grid;
  @InjectView(R.id.progress) protected ProgressBar progressBar;

  @Inject Picasso picasso;
  @Inject @ForActivity Context activityContext;

  protected MovieGridAdapter adapter;

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    return inflater.inflate(R.layout.fragment_movies_grid, container, false);
  }

  @Override public void onActivityCreated(Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
    adapter = new MovieGridAdapter(activityContext, picasso, bus);
    grid.setAdapter(adapter);
  }

  protected View setExtraView(int viewResourceId) {
    View view = LayoutInflater.from(activityContext).inflate(viewResourceId, extra);
    root.setDisplayedChildView(extra);
    return view;
  }
}
