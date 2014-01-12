/*
 * Copyright 2014 Prateek Srivastava (@f2prateek)
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.f2prateek.couchpotato;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.preference.PreferenceManager;
import com.f2prateek.couchpotato.model.moviedb.MovieDbConfiguration;
import com.f2prateek.couchpotato.services.BaseApiService;
import com.f2prateek.couchpotato.services.CouchPotatoService;
import com.f2prateek.couchpotato.services.FilePreference;
import com.f2prateek.couchpotato.services.MovieDBApi;
import com.f2prateek.couchpotato.services.MovieDBService;
import com.f2prateek.couchpotato.services.couchpotato.CouchPotatoAppApi;
import com.f2prateek.couchpotato.services.couchpotato.CouchPotatoLoggingApi;
import com.f2prateek.couchpotato.services.couchpotato.CouchPotatoManageApi;
import com.f2prateek.couchpotato.services.couchpotato.CouchPotatoMiscApi;
import com.f2prateek.couchpotato.services.couchpotato.CouchPotatoMovieApi;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.otto.Bus;
import dagger.Module;
import dagger.Provides;
import javax.inject.Singleton;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.converter.GsonConverter;

@Module(
    injects = {
        CouchPotatoApplication.class,
        // Service
        BaseApiService.class, CouchPotatoService.class, MovieDBService.class
    },
    library = true)
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

  @Provides MovieDbConfiguration provideConfiguration(Gson gson) {
    FilePreference<MovieDbConfiguration> configurationFilePreference =
        new FilePreference<MovieDbConfiguration>(gson, application.getFilesDir(),
            MovieDbConfiguration.class);
    return configurationFilePreference.get();
  }

  @Provides UserConfig provideUserConfig(SharedPreferences sharedPreferences) {
    return new UserConfig(sharedPreferences);
  }

  @Provides @Singleton @CouchPotato
  RestAdapter provideCouchPotatoRestAdapter(UserConfig userConfig) {
    return new RestAdapter.Builder().setServer(userConfig.getServerUrl()).build();
  }

  @Provides @Singleton
  CouchPotatoAppApi provideCouchPotatoAppApi(@CouchPotato RestAdapter restAdapter) {
    return restAdapter.create(CouchPotatoAppApi.class);
  }

  @Provides @Singleton
  CouchPotatoLoggingApi provideCouchPotatoLoggingApi(@CouchPotato RestAdapter restAdapter) {
    return restAdapter.create(CouchPotatoLoggingApi.class);
  }

  @Provides @Singleton
  CouchPotatoManageApi provideCouchPotatoManageApi(@CouchPotato RestAdapter restAdapter) {
    return restAdapter.create(CouchPotatoManageApi.class);
  }

  @Provides @Singleton
  CouchPotatoMiscApi provideCouchPotatoMiscApi(@CouchPotato RestAdapter restAdapter) {
    return restAdapter.create(CouchPotatoMiscApi.class);
  }

  @Provides @Singleton
  CouchPotatoMovieApi provideCouchPotatoMovieApi(@CouchPotato RestAdapter restAdapter) {
    return restAdapter.create(CouchPotatoMovieApi.class);
  }

  @Provides MovieDBApi provideMovieDBApi(Gson gson) {
    RequestInterceptor movieDbRequestInterceptor = new RequestInterceptor() {
      @Override public void intercept(RequestFacade request) {
        request.addQueryParam("api_key", "c820209625cf108a92f8e4192ec26a7f");
      }
    };
    return new RestAdapter.Builder().setConverter(new GsonConverter(gson))
        .setServer("http://api.themoviedb.org/3/")
        .setRequestInterceptor(movieDbRequestInterceptor)
        .build()
        .create(MovieDBApi.class);
  }

  @Provides @Singleton @ForApplication Context provideApplicationContext() {
    return application;
  }

  @Provides @Singleton SharedPreferences provideSharePreferences(@ForApplication Context context) {
    return PreferenceManager.getDefaultSharedPreferences(context);
  }

  @Provides @Singleton Resources provideResources(@ForApplication Context context) {
    return context.getResources();
  }
}