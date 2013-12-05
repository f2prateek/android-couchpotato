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
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import com.f2prateek.couchpotato.CouchPotatoApplication;
import com.f2prateek.couchpotato.util.Ln;
import com.f2prateek.couchpotato.util.SafeAsyncTask;
import com.google.gson.Gson;
import com.squareup.otto.Bus;
import javax.inject.Inject;

public abstract class BaseApiService extends Service {
  @Inject Bus bus;
  @Inject Gson gson;
  private static final Handler MAIN_THREAD = new Handler(Looper.getMainLooper());

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

  /**
   * A task that fetches T, and posts to the event bus.
   */
  public abstract class DataEventTask<T> extends SafeAsyncTask<T> {

    @Override protected void onSuccess(T data) throws Exception {
      super.onSuccess(data);
      if (data != null) {
        post(data);
      } else {
        // TODO : throw Exception!
      }
    }

    @Override protected void onException(Exception e) throws RuntimeException {
      super.onException(e);
      // TODO : propagate exception
      Ln.e(e);
    }

    public void post(T data) {
      bus.post(data);
    }
  }

  /**
   * A task that saves the data to the FileSystem. It fetches the data from the file first, then
   * switches to the network call.
   */
  public abstract class SaveableDataEventTask<T> extends DataEventTask<T> {
    private final FilePreference<T> filePreference;

    public SaveableDataEventTask(Class<T> typeParameterClass) {
      filePreference = new FilePreference<T>(gson, getFilesDir(), typeParameterClass);
    }

    @Override public T call() throws Exception {
      final T data = filePreference.get();
      if (data != null) {
        MAIN_THREAD.post(new Runnable() {
          @Override public void run() {
            post(data);
          }
        });
      }
      return get();
    }

    public abstract T get() throws Exception;

    @Override protected void onSuccess(T data) throws Exception {
      super.onSuccess(data);
      // Super throws an exception if data is null, so we can be sure it's not null here
      filePreference.save(data);
    }
  }
}