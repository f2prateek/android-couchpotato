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

package com.f2prateek.couchpotato;

import android.app.Application;
import com.crashlytics.android.Crashlytics;
import com.f2prateek.couchpotato.model.moviedb.MovieDbConfiguration;
import com.f2prateek.couchpotato.services.FilePreference;
import com.f2prateek.couchpotato.services.MovieDBApi;
import com.f2prateek.couchpotato.util.Ln;
import com.google.analytics.tracking.android.GoogleAnalytics;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import dagger.ObjectGraph;
import java.util.Arrays;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Provider;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class CouchPotatoApplication extends Application implements Callback<MovieDbConfiguration> {
  private ObjectGraph applicationGraph;

  @Inject Provider<MovieDBApi> movieDBApiProvider;
  @Inject Gson gson;

  @Override public void onCreate() {
    super.onCreate();
    applicationGraph = ObjectGraph.create(getModules().toArray());
    applicationGraph.inject(this);

    Picasso.with(this).setDebugging(BuildConfig.DEBUG);
    GoogleAnalytics.getInstance(this).setDryRun(BuildConfig.DEBUG);

    if (BuildConfig.RELEASE) {
      // Release specific stuff goes here.
      Crashlytics.start(this);
    }

    movieDBApiProvider.get().get_configuration(this);
  }

  protected List<Object> getModules() {
    return Arrays.<Object>asList(new AndroidModule(this), new CouchPotatoApplicationModule(this));
  }

  public void inject(Object object) {
    applicationGraph.inject(object);
  }

  public ObjectGraph getApplicationGraph() {
    return applicationGraph;
  }

  @Override public void success(MovieDbConfiguration movieDbConfiguration, Response response) {
    movieDbConfiguration.timestamp = System.currentTimeMillis();
    FilePreference<MovieDbConfiguration> configurationFilePreference =
        new FilePreference<MovieDbConfiguration>(gson, getFilesDir(), MovieDbConfiguration.class);
    configurationFilePreference.save(movieDbConfiguration);
  }

  @Override public void failure(RetrofitError error) {
    Ln.e(error);
  }
}