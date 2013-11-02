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

package com.f2prateek.couchpotato.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import com.f2prateek.couchpotato.CouchPotatoApplication;
import com.squareup.otto.Bus;
import javax.inject.Inject;

public abstract class BaseApiService extends Service {
  @Inject Bus bus;

  @Override public void onCreate() {
    super.onCreate();
    ((CouchPotatoApplication) getApplication()).inject(this);
    bus.register(this);
  }

  @Override public void onDestroy() {
    bus.unregister(this);
    super.onDestroy();
  }

  @Override public IBinder onBind(Intent intent) {
    return null;
  }
}