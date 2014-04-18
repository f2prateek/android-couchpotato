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

package com.f2prateek.couchpotato.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.ViewGroup;
import butterknife.ButterKnife;
import com.f2prateek.couchpotato.CouchPotatoApplication;
import com.f2prateek.dart.Dart;
import com.squareup.otto.Bus;
import javax.inject.Inject;

public abstract class BaseActivity extends Activity {
  @Inject AppContainer appContainer;
  @Inject Bus bus;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    CouchPotatoApplication app = CouchPotatoApplication.get(this);
    app.inject(this);

    // Inflate into our container
    inflateLayout(appContainer.get(this, app));

    ButterKnife.inject(this);
    Dart.inject(this);
  }

  protected abstract void inflateLayout(ViewGroup container);

  @Override protected void onResume() {
    super.onResume();
    bus.register(this);
  }

  @Override protected void onPause() {
    super.onPause();
    bus.unregister(this);
  }
}
