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
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import butterknife.ButterKnife;
import com.f2prateek.couchpotato.ForActivity;
import com.f2prateek.couchpotato.ui.ScopedBus;
import com.f2prateek.couchpotato.ui.activities.BaseActivity;
import com.f2prateek.dart.Dart;
import javax.inject.Inject;

public class BaseFragment extends Fragment {
  @Inject ScopedBus bus;
  @Inject @ForActivity Context activityContext;

  @Override public void onActivityCreated(Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
    BaseActivity.get(this).inject(this);
  }

  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    Dart.inject(this);
  }

  @Override public void onStart() {
    super.onStart();
    bus.register(this);
  }

  @Override public void onPause() {
    super.onPause();
    bus.unregister(this);
  }

  @Override public void onViewCreated(View view, Bundle inState) {
    super.onViewCreated(view, inState);
    ButterKnife.inject(this, view);
  }

  @Override public void onDestroyView() {
    ButterKnife.reset(this);
    super.onDestroyView();
  }
}
