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

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import butterknife.InjectView;
import com.f2prateek.couchpotato.R;
import com.f2prateek.couchpotato.ui.widget.BetterViewAnimator;

/**
 * A custom replacement for {@link android.app.ListFragment}.
 */
public abstract class BaseListFragment extends BaseFragment {
  @InjectView(R.id.fragment_root) BetterViewAnimator betterViewAnimator;
  @InjectView(android.R.id.list) ListView listView;
  @InjectView(android.R.id.progress) ProgressBar progressBar;

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    return inflater.inflate(R.layout.fragment_list, container, false);
  }

  @Override public void onViewCreated(View view, Bundle inState) {
    super.onViewCreated(view, inState);
    showLoadingView();
  }

  protected void showLoadingView() {
    show(progressBar);
  }

  private void showList() {
    show(listView);
  }

  private void show(View view) {
    betterViewAnimator.setDisplayedChild(view);
  }

  protected void setAdapter(ListAdapter adapter) {
    listView.setAdapter(adapter);
    showList();
  }
}
