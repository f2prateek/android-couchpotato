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
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.ProgressBar;
import butterknife.InjectView;
import com.f2prateek.couchpotato.ForActivity;
import com.f2prateek.couchpotato.R;
import com.f2prateek.couchpotato.ui.widget.BetterViewAnimator;
import javax.inject.Inject;

/**
 * A custom replacement for {@link android.app.ListFragment}.
 */
public abstract class BaseGridFragment extends BaseFragment {
  @InjectView(R.id.fragment_root) BetterViewAnimator root;
  @InjectView(R.id.grid) GridView gridView;
  @InjectView(android.R.id.progress) ProgressBar progressBar;

  @Inject @ForActivity Context activityContext;

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    return inflater.inflate(R.layout.fragment_grid, container, false);
  }

  @Override public void onViewCreated(View view, Bundle inState) {
    super.onViewCreated(view, inState);
    showLoadingView();
  }

  protected void showLoadingView() {
    show(progressBar);
  }

  private void showGrid() {
    show(gridView);
  }

  private void show(View view) {
    root.setDisplayedChild(view);
  }

  protected View setExtraView(int viewResourceId) {
    View view = LayoutInflater.from(activityContext).inflate(viewResourceId, root);
    root.setDisplayedChild(view);
    return view;
  }

  protected void setAdapter(ListAdapter adapter) {
    gridView.setAdapter(adapter);
    showGrid();
  }

  public GridView getGridView() {
    return gridView;
  }
}
