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

package com.f2prateek.couchpotato.ui.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import butterknife.Views;
import com.f2prateek.couchpotato.AppConstants;
import com.f2prateek.couchpotato.CouchPotatoApplication;
import com.google.analytics.tracking.android.EasyTracker;
import com.google.gson.Gson;
import com.squareup.otto.Bus;
import javax.inject.Inject;

/**
 * A Base {@link android.app.Activity} that injects itself into the {@link
 * dagger.ObjectGraph} and performs view injection
 */
public abstract class BaseActivity extends Activity {

  @Inject public Bus bus;
  public static final Gson gson = AppConstants.gson;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    // Perform injection so that when this call returns all dependencies will be available for use.
    ((CouchPotatoApplication) getApplication()).inject(this);

    // All activities will require this
    requestWindowFeature(Window.FEATURE_PROGRESS);
  }

  @Override protected void onStart() {
    super.onStart();
    EasyTracker.getInstance(this).activityStart(this);
  }

  @Override public void setContentView(int layoutResID) {
    super.setContentView(layoutResID);
    // Perform "view injection"
    Views.inject(this);
  }

  @Override protected void onResume() {
    super.onResume();
    bus.register(this);
  }

  @Override protected void onPause() {
    bus.unregister(this);
    super.onPause();
  }

  @Override
  public void onStop() {
    EasyTracker.getInstance(this).activityStop(this);
    super.onStop();
  }

  protected void showIndeterminateBar(boolean show) {
    setProgressBarIndeterminate(show);
    setProgressBarVisibility(show);
  }
}
