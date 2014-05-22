/*
 * Copyright 2014 Prateek Srivastava (@f2prateek)
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

import android.app.Fragment;
import android.os.Bundle;
import android.view.View;
import butterknife.ButterKnife;
import com.f2prateek.couchpotato.data.rx.FragmentSubscriptionManager;
import com.f2prateek.couchpotato.data.rx.SubscriptionManager;
import com.f2prateek.couchpotato.ui.ScopedBus;
import com.f2prateek.couchpotato.ui.activities.BaseActivity;
import com.f2prateek.dart.Dart;
import javax.inject.Inject;
import rx.Observable;
import rx.Observer;
import rx.Subscription;

/**
 * A base fragment that ties into the activity graph, sets up the event bus and other utilities.
 * Dependencies are not available until {@link #onActivityCreated(android.os.Bundle)} returns.
 */
public abstract class BaseFragment extends Fragment {
  @Inject ScopedBus bus;

  private SubscriptionManager<Fragment> subscriptionManager;

  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    // Inject extras
    Dart.inject(this);
  }

  @Override public void onViewCreated(View view, Bundle inState) {
    super.onViewCreated(view, inState);
    // Inject views
    ButterKnife.inject(this, view);
  }

  @Override public void onActivityCreated(Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
    // Inject ourselves into the activity graph
    BaseActivity.get(this).inject(this);
  }

  @Override public void onResume() {
    super.onResume();
    // Register with the bus
    bus.register(this);
  }

  @Override public void onPause() {
    // Unregister with the bus
    bus.unregister(this);
    super.onPause();
  }

  @Override public void onDetach() {
    super.onDetach();

    // Clear any subscriptions
    if (subscriptionManager != null) {
      subscriptionManager.unsubscribeAll();
    }
  }

  @Override public void onDestroyView() {
    // Clear view references
    ButterKnife.reset(this);
    super.onDestroyView();
  }

  protected <O> Subscription subscribe(final Observable<O> source, final Observer<O> observer) {
    if (subscriptionManager == null) {
      subscriptionManager = new FragmentSubscriptionManager(this);
    }
    return subscriptionManager.subscribe(source, observer);
  }
}
