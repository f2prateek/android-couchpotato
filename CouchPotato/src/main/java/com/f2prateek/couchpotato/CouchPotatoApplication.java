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
import android.content.Intent;
import com.crashlytics.android.Crashlytics;
import com.f2prateek.couchpotato.services.MovieDBService;
import com.google.analytics.tracking.android.GoogleAnalytics;
import com.squareup.picasso.Picasso;
import dagger.ObjectGraph;
import java.util.Arrays;
import java.util.List;

public class CouchPotatoApplication extends Application {
  private ObjectGraph applicationGraph;

  @Override public void onCreate() {
    super.onCreate();
    applicationGraph = ObjectGraph.create(getModules().toArray());

    Picasso.with(this).setDebugging(BuildConfig.DEBUG);
    GoogleAnalytics.getInstance(this).setDryRun(BuildConfig.DEBUG);

    if (BuildConfig.RELEASE) {
      // Release specific stuff goes here.
      Crashlytics.start(this);
    }

    getMovieDbConfiguration();
  }

  private void getMovieDbConfiguration() {
    Intent intent = new Intent(this, MovieDBService.class);
    intent.setAction(MovieDBService.ACTION_GET_CONFIGURATION);
    startService(intent);
  }

  protected List<Object> getModules() {
    return Arrays.<Object>asList(new AndroidModule(this), new CouchPotatoModule(this));
  }

  public void inject(Object object) {
    applicationGraph.inject(object);
  }
}