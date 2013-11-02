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
import com.f2prateek.couchpotato.services.BaseApiService;
import com.f2prateek.couchpotato.services.CouchPotatoApi;
import com.f2prateek.couchpotato.services.CouchPotatoService;
import com.f2prateek.couchpotato.services.MovieDBApi;
import com.f2prateek.couchpotato.services.MovieDBService;
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
import com.f2prateek.couchpotato.ui.fragments.MovieInfoFragment;
import com.squareup.otto.Bus;
import dagger.Module;
import dagger.Provides;
import javax.inject.Singleton;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;

@Module(
    injects = {
        BaseActivity.class, BaseAuthenticatedActivity.class, MainActivity.class,
        ViewMovieActivity.class, BaseFragment.class, BaseProgressFragment.class,
        BaseProgressGridFragment.class, DetailedMovieGridFragment.class, ServerSetupActivity.class,
        MovieCastFragment.class, MovieInfoFragment.class, BaseApiService.class,
        CouchPotatoService.class, MovieDBService.class
    },
    complete = false)
public class CouchPotatoModule {
  @Provides @Singleton Bus provideOttoBus() {
    return new Bus();
  }

  @Provides UserConfig provideUserConfig(SharedPreferences sharedPreferences) {
    return new UserConfig(sharedPreferences);
  }

  @Provides CouchPotatoApi provideCouchPotatoApi(UserConfig userConfig) {
    return new RestAdapter.Builder().setServer(userConfig.getServerUrl())
        .build()
        .create(CouchPotatoApi.class);
  }

  @Provides MovieDBApi provideMovieDBApi() {
    RequestInterceptor movieDbRequestInterceptor = new RequestInterceptor() {
      @Override public void intercept(RequestFacade request) {
        request.addQueryParam("api_key", "c820209625cf108a92f8e4192ec26a7f");
      }
    };
    return new RestAdapter.Builder().setServer("http://api.themoviedb.org/3/")
        .setRequestInterceptor(movieDbRequestInterceptor)
        .build()
        .create(MovieDBApi.class);
  }
}