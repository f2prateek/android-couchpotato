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
import android.widget.AbsListView;
import android.widget.ListAdapter;
import android.widget.ProgressBar;
import butterknife.InjectView;
import com.f2prateek.couchpotato.ForActivity;
import com.f2prateek.couchpotato.R;
import com.f2prateek.couchpotato.ui.widget.BetterViewAnimator;
import javax.inject.Inject;

/**
 * A custom replacement for {@link android.app.ListFragment}.
 * Abstraction allows us to use a {@link android.widget.GridView} with {@link
 * com.f2prateek.couchpotato.ui.fragments.BaseGridFragment} and a {@link android.widget.ListView}
 * with {@link com.f2prateek.couchpotato.ui.fragments.BaseListFragment} with the same API.
 */
public abstract class BaseCollectionFragment extends BaseFragment {
  @InjectView(R.id.fragment_root) BetterViewAnimator root;
  @InjectView(R.id.collection_view) AbsListView collectionView;
  @InjectView(android.R.id.progress) ProgressBar progressBar;

  @Inject @ForActivity Context activityContext;

  public void showLoadingView() {
    show(progressBar);
  }

  public void showCollectionView() {
    show(collectionView);
  }

  private void show(View view) {
    root.setDisplayedChild(view);
  }

  @Override public void onViewCreated(View view, Bundle inState) {
    super.onViewCreated(view, inState);
    showLoadingView();
  }

  public View setExtraView(int viewResourceId) {
    View view = LayoutInflater.from(activityContext).inflate(viewResourceId, root, false);
    root.addView(view);
    root.setDisplayedChild(view);
    return view;
  }

  protected void setAdapter(ListAdapter adapter) {
    collectionView.setAdapter(adapter);
    showCollectionView();
  }

  public AbsListView getCollectionView() {
    return collectionView;
  }
}
