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
import com.f2prateek.couchpotato.ui.activities.MainActivity;
import com.f2prateek.couchpotato.ui.activities.MovieSearchActivity;
import com.f2prateek.couchpotato.ui.activities.ServerSetupActivity;
import com.f2prateek.couchpotato.ui.activities.ViewMovieActivity;
import com.f2prateek.couchpotato.ui.activities.ViewSearchMovieActivity;
import com.f2prateek.couchpotato.ui.base.BaseActivity;
import com.f2prateek.couchpotato.ui.base.BaseAuthenticatedActivity;
import com.f2prateek.couchpotato.ui.base.BaseFragment;
import com.f2prateek.couchpotato.ui.base.BaseProgressFragment;
import com.f2prateek.couchpotato.ui.base.BaseProgressGridFragment;
import com.f2prateek.couchpotato.ui.fragments.DetailedMovieGridFragment;
import com.f2prateek.couchpotato.ui.fragments.MovieCastFragment;
import com.f2prateek.couchpotato.ui.fragments.MovieInfoFragment;
import com.f2prateek.couchpotato.ui.fragments.MovieSearchGridFragment;
import com.squareup.otto.Bus;
import dagger.Module;
import dagger.Provides;
import javax.inject.Singleton;
import retrofit.RestAdapter;

@Module(
    injects = {
        BaseActivity.class, BaseAuthenticatedActivity.class, MainActivity.class,
        ViewMovieActivity.class, MovieSearchActivity.class, ViewSearchMovieActivity.class,
        BaseFragment.class, BaseProgressFragment.class, BaseProgressGridFragment.class,
        DetailedMovieGridFragment.class, MovieSearchGridFragment.class, ServerSetupActivity.class,
        MovieCastFragment.class, MovieInfoFragment.class
    },
    complete = false)
public class CouchPotatoModule {
  @Provides @Singleton Bus provideOttoBus() {
    return new Bus();
  }

  @Provides UserConfig provideUserConfig(SharedPreferences sharedPreferences) {
    return new UserConfig(sharedPreferences);
  }

  @Provides RestAdapter provideRestAdapter(UserConfig userConfig) {
    return new RestAdapter.Builder().setServer(userConfig.getServerUrl()).build();
  }

  @Provides CouchPotatoApi provideCouchPotatoApi(RestAdapter restAdapter) {
    return restAdapter.create(CouchPotatoApi.class);
  }
}