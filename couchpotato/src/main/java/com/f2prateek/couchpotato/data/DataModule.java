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

package com.f2prateek.couchpotato.data;

import android.app.Application;
import android.content.SharedPreferences;
import android.net.Uri;
import com.f2prateek.couchpotato.data.api.ApiModule;
import com.f2prateek.ln.Ln;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.okhttp.HttpResponseCache;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.otto.Bus;
import com.squareup.picasso.OkHttpDownloader;
import com.squareup.picasso.Picasso;
import dagger.Module;
import dagger.Provides;
import java.io.File;
import java.io.IOException;
import javax.inject.Singleton;

import static android.content.Context.MODE_PRIVATE;

@Module(
    includes = {
        ApiModule.class, UserPreferencesModule.class
    },
    complete = false,
    library = true)
public final class DataModule {
  static final int DISK_CACHE_SIZE = 50 * 1024 * 1024; // 50MB

  @Provides @Singleton SharedPreferences provideSharedPreferences(Application app) {
    return app.getSharedPreferences("couchpotato", MODE_PRIVATE);
  }

  @Provides @Singleton OkHttpClient provideOkHttpClient(Application app) {
    return createOkHttpClient(app);
  }

  @Provides @Singleton Picasso providePicasso(Application app, OkHttpClient client) {
    return new Picasso.Builder(app).downloader(new OkHttpDownloader(client))
        .listener(new Picasso.Listener() {
          @Override public void onImageLoadFailed(Picasso picasso, Uri uri, Exception e) {
            Ln.e(e, "Failed to load image: %s", uri);
          }
        })
        .build();
  }

  static OkHttpClient createOkHttpClient(Application app) {
    OkHttpClient client = new OkHttpClient();

    // Install an HTTP cache in the application cache directory.
    try {
      File cacheDir = new File(app.getCacheDir(), "http");
      HttpResponseCache cache = new HttpResponseCache(cacheDir, DISK_CACHE_SIZE);
      client.setResponseCache(cache);
    } catch (IOException e) {
      Ln.e(e, "Unable to install disk cache.");
    }

    return client;
  }

  @Provides @Singleton Bus provideBus() {
    return new Bus();
  }

  @Provides @Singleton Gson provideGson() {
    return new GsonBuilder().create();
  }
}