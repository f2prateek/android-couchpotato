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

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import butterknife.Views;
import com.f2prateek.couchpotato.AppConstants;
import com.f2prateek.couchpotato.CouchPotatoApplication;
import com.google.gson.Gson;
import com.squareup.otto.Bus;
import javax.inject.Inject;

/**
 * A base fragment class that automatically injects itself into the {@link dagger.ObjectGraph}.
 * Does NOT setup for ButterKnife.
 */
public abstract class BaseFragment extends Fragment {
  @Inject Bus bus;
  public static final Gson gson = AppConstants.gson;
  protected Context activityContext;

  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    // Perform injection so that when this call returns all dependencies will be available for use.
    ((CouchPotatoApplication) getActivity().getApplication()).inject(this);
    activityContext = getActivity();
  }

  @Override public void onResume() {
    super.onResume();
    bus.register(this);
  }

  @Override public void onPause() {
    bus.unregister(this);
    super.onPause();
  }

  protected void showIndeterminateBar(boolean show) {
    getActivity().setProgressBarIndeterminate(show);
    getActivity().setProgressBarVisibility(show);
  }

  @Override public void onViewCreated(View view, Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    Views.inject(this, view);
  }
}