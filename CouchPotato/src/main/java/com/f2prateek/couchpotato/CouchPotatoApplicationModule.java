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

import android.content.SharedPreferences;
import com.f2prateek.couchpotato.model.moviedb.MovieDbConfiguration;
import com.f2prateek.couchpotato.services.CouchPotatoApi;
import com.f2prateek.couchpotato.services.FilePreference;
import com.f2prateek.couchpotato.services.MovieDBApi;
import com.f2prateek.couchpotato.ui.activities.BaseActivity;
import com.f2prateek.couchpotato.ui.activities.BaseAuthenticatedActivity;
import com.f2prateek.couchpotato.ui.activities.MainActivity;
import com.f2prateek.couchpotato.ui.activities.ServerSetupActivity;
import com.f2prateek.couchpotato.ui.activities.ViewMovieActivity;
import com.f2prateek.couchpotato.ui.fragments.BaseFragment;
import com.f2prateek.couchpotato.ui.fragments.BaseProgressFragment;
import com.f2prateek.couchpotato.ui.fragments.BaseProgressGridFragment;
import com.f2prateek.couchpotato.ui.fragments.DetailedMovieGridFragment;
import com.f2prateek.couchpotato.ui.fragments.MovieCastFragment;
import com.f2prateek.couchpotato.ui.fragments.MovieCrewFragment;
import com.f2prateek.couchpotato.ui.fragments.MovieInfoFragment;
import com.f2prateek.couchpotato.ui.fragments.SimpleMovieGridFragment;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.otto.Bus;
import dagger.Module;
import dagger.Provides;
import javax.inject.Named;
import javax.inject.Singleton;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.converter.GsonConverter;

@Module(
    injects = {
        CouchPotatoApplication.class, BaseActivity.class, BaseAuthenticatedActivity.class,
        MainActivity.class, ViewMovieActivity.class, BaseFragment.class, BaseProgressFragment.class,
        BaseProgressGridFragment.class, DetailedMovieGridFragment.class,
        SimpleMovieGridFragment.class, ServerSetupActivity.class, MovieCastFragment.class,
        MovieCrewFragment.class, MovieInfoFragment.class
    },
    complete = false)
public class CouchPotatoApplicationModule {

  private final CouchPotatoApplication application;

  public CouchPotatoApplicationModule(CouchPotatoApplication application) {
    this.application = application;
  }

  @Provides @Singleton Bus provideOttoBus() {
    return new Bus();
  }

  @Provides @Singleton Gson provideGson() {
    return new GsonBuilder().setDateFormat("yyyy-mm-dd").create();
  }

  @Provides UserConfig provideUserConfig(SharedPreferences sharedPreferences) {
    return UserConfig.fromSharedPreferences(sharedPreferences);
  }

  @Provides @Named("CouchPotato")
  RestAdapter provideCouchPotatoApiRestAdapter(UserConfig userConfig) {
    return new RestAdapter.Builder().setServer(userConfig.getAuthenticatedServerUrl()).build();
  }

  @Provides CouchPotatoApi provideCouchPotatoApi(@Named("CouchPotato") RestAdapter restAdapter) {
    return restAdapter.create(CouchPotatoApi.class);
  }

  @Provides MovieDbConfiguration provideConfiguration(Gson gson) {
    FilePreference<MovieDbConfiguration> configurationFilePreference =
        new FilePreference<MovieDbConfiguration>(gson, application.getFilesDir(),
            MovieDbConfiguration.class);
    return configurationFilePreference.get();
  }

  @Provides @Named("MovieDB") RestAdapter provideMovieDBRestAdapter(Gson gson) {
    RequestInterceptor requestInterceptor = new RequestInterceptor() {
      @Override public void intercept(RequestFacade request) {
        request.addQueryParam("api_key", "c820209625cf108a92f8e4192ec26a7f");
      }
    };
    return new RestAdapter.Builder().setConverter(new GsonConverter(gson))
        .setServer("http://api.themoviedb.org/3/")
        .setRequestInterceptor(requestInterceptor)
        .build();
  }

  @Provides MovieDBApi provideMovieDBApi(@Named("MovieDB") RestAdapter restAdapter) {
    return restAdapter.create(MovieDBApi.class);
  }
}