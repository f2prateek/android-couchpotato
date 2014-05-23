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

package com.f2prateek.couchpotato.ui.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.ViewGroup;
import butterknife.ButterKnife;
import com.f2prateek.couchpotato.CouchPotatoApplication;
import com.f2prateek.couchpotato.data.rx.ActivitySubscriptionManager;
import com.f2prateek.couchpotato.data.rx.SubscriptionManager;
import com.f2prateek.couchpotato.ui.ActivityModule;
import com.f2prateek.couchpotato.ui.AppContainer;
import com.f2prateek.couchpotato.ui.ScopedBus;
import com.f2prateek.dart.Dart;
import dagger.ObjectGraph;
import hugo.weaving.DebugLog;
import java.util.Arrays;
import java.util.List;
import javax.inject.Inject;
import rx.Observable;
import rx.Observer;
import rx.Subscription;

@SuppressLint("Registered") public class BaseActivity extends Activity {
  @Inject AppContainer appContainer;
  @Inject ScopedBus bus;

  private ViewGroup container;
  private ObjectGraph activityGraph;

  private SubscriptionManager<Activity> subscriptionManager;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    buildActivityGraphAndInject();

    // Inject any extras
    Dart.inject(this);
  }

  @DebugLog
  private void buildActivityGraphAndInject() {
    // Create the activity graph by .plus-ing our modules onto the application graph.
    CouchPotatoApplication app = CouchPotatoApplication.get(this);
    activityGraph = app.getApplicationGraph().plus(getModules().toArray());

    // Inject ourselves so subclasses will have dependencies fulfilled when this method returns.
    activityGraph.inject(this);

    container = appContainer.get(this, app);
  }

  /** Inject the given object into the activity graph. */
  public void inject(Object o) {
    activityGraph.inject(o);
  }

  /**
   * A list of modules to use for the individual activity graph. Subclasses can override this
   * method to provide additional modules provided they call and include the modules returned by
   * calling {@code super.getModules()}.
   */
  protected List<Object> getModules() {
    return Arrays.<Object>asList(new ActivityModule(this));
  }

  @Override protected void onResume() {
    super.onResume();
    bus.resumed();
    bus.register(this);
  }

  @Override protected void onPause() {
    bus.unregister(this);
    bus.paused();
    super.onPause();
  }

  @Override protected void onDestroy() {
    // Eagerly clear the reference to the activity graph to allow it to be garbage collected as
    // soon as possible.
    activityGraph = null;

    // Clear any subscriptions
    if (subscriptionManager != null) {
      subscriptionManager.unsubscribeAll();
    }

    super.onDestroy();
  }

  protected void inflateLayout(int layoutResID) {
    getLayoutInflater().inflate(layoutResID, container);
    // Inject Views
    ButterKnife.inject(this);
  }

  public static BaseActivity get(Fragment fragment) {
    return (BaseActivity) fragment.getActivity();
  }

  protected <O> Subscription subscribe(final Observable<O> source, final Observer<O> observer) {
    if (subscriptionManager == null) {
      subscriptionManager = new ActivitySubscriptionManager(this);
    }
    return subscriptionManager.subscribe(source, observer);
  }
}
